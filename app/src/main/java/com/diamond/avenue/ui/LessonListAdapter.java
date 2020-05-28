package com.diamond.avenue.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.diamond.avenue.R;

public class LessonListAdapter extends ArrayAdapter<LessonItem> {
    public LessonListAdapter(Context context, LessonItem[] lessons) {
        super(context, R.layout.lesson_list_item, lessons);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LessonItem lesson = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lesson_list_item, null);
        }

// Заполняем адаптер
        assert lesson != null;
        ((TextView) convertView.findViewById(R.id.name)).setText(lesson.id + ". " + lesson.name);

        //if (lesson.checked) convertView.setBackgroundColor(R.color.colorAccent);

        //convertView.setBackgroundColor(R.color.lessonBackgroundColor);

        return convertView;
    }
}
