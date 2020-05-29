package com.diamond.avenue.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diamond.avenue.LessonActivity;
import com.diamond.avenue.MoreLessonActivity;
import com.diamond.avenue.R;
import com.diamond.avenue.language.lib.Array;
import com.diamond.avenue.retrofit.MoreLessons;
import com.diamond.avenue.retrofit.MoreLessonsService;
import com.diamond.avenue.retrofit.StaticLessons;
import com.diamond.avenue.ui.more.LessonItem;
import com.diamond.avenue.ui.more.LessonListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ScreenThree extends androidx.fragment.app.Fragment {

    private ListView listView;
    private ProgressDialog pBar;

    public ScreenThree() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.screen_third, container,
                false);

        listView = root.findViewById(R.id.moreLessons);

        if (StaticLessons.lessons == null) {
            pBar = new ProgressDialog(getActivity());
            pBar.setMessage("Получение доп. курсов");
            pBar.setIndeterminate(false);
            pBar.setCancelable(false);
            new LessonsAsyncTask().execute("");
            pBar.show();
        }
        else {
            MoreLessons lessons = StaticLessons.lessons;
            LessonItem[] items = new LessonItem[lessons.names.length];
            for (int i=0; i<lessons.names.length; i++) {
                items[i] = new LessonItem();
                items[i].name = lessons.names[i];
                items[i].desc = lessons.desc[i];
                items[i].img = lessons.img[i];
            }
            if (listView != null)
                listView.setAdapter(new LessonListAdapter(getActivity(), items));
        }

        return root;
    }

    class LessonsAsyncTask extends AsyncTask<String, String, String> {

        LessonItem items[];

        @Override
        protected String doInBackground(String... strings) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://test-vk-auth5.000webhostapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MoreLessonsService service = retrofit.create(MoreLessonsService.class);

            MoreLessons lessons;
            Call<MoreLessons> call = service.getLessons();
            try {
                lessons = call.execute().body();
                StaticLessons.lessons = lessons;
                items = new LessonItem[lessons.names.length];
                for (int i=0; i<lessons.names.length; i++) {
                    items[i] = new LessonItem();
                    items[i].name = lessons.names[i];
                    items[i].desc = lessons.desc[i];
                    items[i].img = lessons.img[i];
                }
            } catch (Exception ignored) { }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (listView != null && items != null) {
                listView.setAdapter(new LessonListAdapter(getActivity(), items));
            } else {
                Toast.makeText(getActivity(), "Не удалось загрузить дополнительные курсы", Toast.LENGTH_LONG).show();
            }
            pBar.dismiss();
        }
    }

}
