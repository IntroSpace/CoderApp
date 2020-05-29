package com.diamond.avenue.ui.more;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.diamond.avenue.R;
import com.diamond.avenue.ui.more.LessonItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LessonListAdapter extends ArrayAdapter<LessonItem> {
    public LessonItem lesson;

    public LessonListAdapter(Context context, LessonItem[] lessons) {
        super(context, R.layout.more_item, lessons);
    }

    @NonNull
    @SuppressLint({"ResourceAsColor", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        lesson = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.more_item, null);
        }

// Заполняем адаптер
        assert lesson != null;
        ((TextView) convertView.findViewById(R.id.name)).setText(lesson.name);
        ((TextView) convertView.findViewById(R.id.desc)).setText(lesson.desc);

        //if (lesson.image == null)
            new DownloadImage((ImageView) convertView.findViewById(R.id.imageView))
                .execute(lesson.img);
        //else
        //    ((ImageView) convertView.findViewById(R.id.imageView)).setImageBitmap(lesson.image);

        //if (lesson.checked) convertView.setBackgroundColor(R.color.colorAccent);

        //convertView.setBackgroundColor(R.color.lessonBackgroundColor);

        return convertView;
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImg;

        public DownloadImage(ImageView bmImage) {
            this.bmImg = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception ignored) { }
            return icon;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                bmImg.setImageBitmap(result);
                //lesson.image = result;
            }
        }
    }
}
