package com.siz_kimsiz.inson_psixologiyasi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public CustomList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.listsinglequestion, web);
        this.context = context;

        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.listsinglequestion, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setText(web[position]);

        System.out.println("imageid length"+imageId.length);
        if(imageId.length>0) {
            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            imageView.setImageResource(imageId[position]);
        }

        System.out.println("List create "+position+"  "+web[position]);
        return rowView;
    }
}