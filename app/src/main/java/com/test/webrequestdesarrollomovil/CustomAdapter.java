package com.test.webrequestdesarrollomovil;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//@SuppressWarnings("serial")
public class CustomAdapter extends ArrayAdapter<Search> implements Serializable{

    public CustomAdapter(Context context, ArrayList<Search> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Search search = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movies_omdb, parent, false);
        }

        // Tag is expected to be an Object (Integer is an object)
        // our data are in String array - that's why we need to convert it into Integer
        // I assume here, your ID number is first item (No.0) in the array of data
        convertView.setTag(search.getImdbID() + "\n"+ search.getTitle() +"\n"+search.getType() + "\n" + search.getYear());


        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHometown);
        // Populate the data into the template view using the data object
        tvName.setText(search.getTitle());
        tvHome.setText(search.getYear());
        // Return the completed view to render on screen
        return convertView;
    }
}