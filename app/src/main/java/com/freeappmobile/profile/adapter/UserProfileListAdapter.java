package com.freeappmobile.profile.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.freeappmobile.R;

import java.util.List;

public class UserProfileListAdapter extends BaseAdapter {

    private Context context;
    private List<String> profileValues;

    public UserProfileListAdapter(Context context, List<String> profileValues) {
        this.context = context;
        this.profileValues = profileValues;
    }

    @Override
    public int getCount() {
        return profileValues.size();
    }

    @Override
    public Object getItem(int position) {
        return profileValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        try {

            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) ((Activity) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (View) layoutInflater.inflate(R.layout.user_profile_row_layout, parent, false);
                holder = new ViewHolder();
                holder.txt_title = (TextView) view.findViewById(R.id.txt_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.txt_title.setText(profileValues.get(position));

            Log.d("Text Size", holder.txt_title.getTextSize() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }


    public static class ViewHolder {
        TextView txt_title;
    }
}
