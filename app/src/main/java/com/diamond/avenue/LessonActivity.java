package com.diamond.avenue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.diamond.avenue.ui.lessons.LessonItem;
import com.diamond.avenue.ui.lessons.LessonListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_lessons);
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