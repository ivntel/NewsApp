package com.example.ivnte.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ivnte on 2016-10-20.
 */

public class ArticlesAdapter extends ArrayAdapter<Articles> {

    private List<Articles> mArticleList;

    private static class ViewHolder {
        TextView title;
        TextView sectionName;
    }

    public ArticlesAdapter(Context context, List<Articles> articleList) {
        super(context, 0, articleList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Articles news = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_layout, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.article_title);
            viewHolder.sectionName = (TextView) convertView.findViewById(R.id.section_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(news.getWebTitle());
        viewHolder.sectionName.setText(news.getSectionName());

        return convertView;
    }
}
