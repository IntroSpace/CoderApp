package com.diamond.avenue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.diamond.avenue.AvenueResult;
import com.diamond.avenue.LessonActivity;
import com.diamond.avenue.R;
import com.diamond.avenue.language.AvenueSystem;
import com.diamond.avenue.language.Lexer;
import com.diamond.avenue.language.Parser;
import com.diamond.avenue.language.Token;
import com.diamond.avenue.language.expr.BlockStatement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

public final class ScreenOne extends androidx.fragment.app.Fragment {
    public ScreenOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.screen_second, container,
                false);

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
            json = new JSONObject(wr.toString());
            items = new LessonItem[json.getJSONArray("id").length()];
            for (int i=0; i<json.getJSONArray("id").length(); i++) {
                items[i] = new LessonItem();
                items[i].id = i+1;
                items[i].name = json.getJSONArray("names").getString(i);
            }
        } catch (JSONException ignored) {

        }

        LessonListAdapter adapter = new LessonListAdapter(getActivity(), items);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), LessonActivity.class);
                i.putExtra("id", ((LessonItem) parent.getItemAtPosition(position)).id);
                i.putExtra("name", ((LessonItem) parent.getItemAtPosition(position)).name);
                startActivity(i);
            }
        });

        return rootView;
    }
}
