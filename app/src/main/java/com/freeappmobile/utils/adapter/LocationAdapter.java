package com.freeappmobile.utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.freeappmobile.R;
import com.freeappmobile.model.LocationDTO;
import com.freeappmobile.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends BaseAdapter implements Filterable{

    private Context context;
    private List<LocationDTO> popUpList;
    private List<LocationDTO> suggestList = new ArrayList<>();
    private Filter filter = new CustomFilter();


    public LocationAdapter(Context context, List<LocationDTO> popUpList) {
        this.context = context;
        this.popUpList = popUpList;

    }

    @Override
    public int getCount() {
        return popUpList.size();
    }

    @Override
    public Object getItem(int position) {
        return popUpList.get(position);
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
                view = (View) layoutInflater.inflate(R.layout.layout_popup_row, parent, false);
                holder = new ViewHolder();
                holder.txt_title = (TextView) view.findViewById(R.id.txt_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.txt_title.setText(popUpList.get(position).getName());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    public static class ViewHolder {
        TextView txt_title;
    }


    /**
     * Our Custom Filter Class.
     */
    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestList.clear();

            if (popUpList != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < popUpList.size(); i++) {
                    if (popUpList.get(i).getValue().toLowerCase().contains(constraint)) { // Compare item in original list if it contains constraints.
                        suggestList.add(popUpList.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = suggestList;
            results.count = suggestList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


}
