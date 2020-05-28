package com.diamond.avenue;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.diamond.avenue.ui.LessonItem;
import com.diamond.avenue.ui.LessonListAdapter;
import com.diamond.avenue.ui.ScreenOne;
import com.diamond.avenue.ui.ScreenTwo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_second);
        final int id1 = getIntent().getIntExtra("id", 1);
        this.setTitle("" + id1 + ". " + getIntent().getStringExtra("name"));
        //((TextView) findViewById(R.id.text)).setText(""+getIntent().getIntExtra("id", 0));
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

        JSONObject json;
        LessonItem[] items = new LessonItem[0];

        try {
            json = (new JSONObject(wr.toString())).getJSONObject("articles").getJSONObject(""+id1);
            items = new LessonItem[json.length()];
            for (int i=0; i<json.length(); i++) {
                items[i] = new LessonItem();
                items[i].id = i+1;
                items[i].name = json.getString(""+(i+1));
            }
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
        }

        LessonListAdapter adapter = new LessonListAdapter(LessonActivity.this, items);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(LessonActivity.this, ArticleActivity.class);
                i.putExtra("id1", id1);
                i.putExtra("id2", ((LessonItem) parent.getItemAtPosition(position)).id);
                i.putExtra("name", ((LessonItem) parent.getItemAtPosition(position)).name);
                startActivity(i);
            }
        });
    }
}