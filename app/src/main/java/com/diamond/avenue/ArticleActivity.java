package com.diamond.avenue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.diamond.avenue.ui.LessonItem;
import com.diamond.avenue.ui.LessonListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        int id1 = getIntent().getIntExtra("id1", 1);
        int id2 = getIntent().getIntExtra("id2", 1);
        this.setTitle("" + id1 + "." + id2 + ". " + getIntent().getStringExtra("name"));

        View codePart = findViewById(R.id.codePart);
        DrawerLayout layout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                layout, /* DrawerLayout object */
                new Toolbar(this), /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                View focusView = getCurrentFocus();
                if (view != null) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        layout.setDrawerListener(mDrawerToggle);

        InputStream is = getResources().openRawResource(R.raw.articles);
        Writer wr = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                wr.write(buffer, 0, n);
            }
        } catch (Exception ignored) {

        } finally {
            try {
                is.close();
            } catch (IOException ignored) {

            }
        }

        String textArticle = "DON`T WORK";

        try {
            textArticle = (new JSONObject(wr.toString())).getJSONArray("text").getJSONArray(id1-1).getString(id2-1);
        } catch (JSONException ignored) {

        }

        //start and end index
        int first, second = 0;
        ArrayList<SpannableString> spanStr = new ArrayList<>();
        while (textArticle.contains("<code>")) {
            first = textArticle.indexOf("<code>");
            textArticle = textArticle.replaceFirst("<code>", "");
            spanStr.add((new SpannableString(textArticle.substring(second, first))));
            second = textArticle.indexOf("</code>");
            textArticle = textArticle.replaceFirst("</code>", "");
            SpannableString str = new SpannableString(textArticle.substring(first, second));
            str.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.codeTextColor)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new StyleSpan(Typeface.ITALIC), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanStr.add(str);
        }
        if (second != textArticle.length())
            spanStr.add((new SpannableString(textArticle.substring(second))));

        for (SpannableString text:spanStr) {
            ((TextView) findViewById(R.id.text)).append(text);
        }
    }
}