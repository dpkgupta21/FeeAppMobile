package com.freeappmobile.profile.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.freeappmobile.R;
import com.freeappmobile.model.SaveStudentListDTO;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {

    private Context context;
    private List<SaveStudentListDTO> profileValues;

    public StudentListAdapter(Context context, List<SaveStudentListDTO> profileValues) {
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
                view = (View) layoutInflater.inflate(R.layout.layout_students_row, parent, false);
                holder = new ViewHolder();
                holder.txt_school_name = (TextView) view.findViewById(R.id.txt_school_name);
                holder.txt_student_id = (TextView) view.findViewById(R.id.txt_student_id);
                holder.txt_student_name = (TextView) view.findViewById(R.id.txt_student_name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.txt_student_name.setText(profileValues.get(position).getStudent());
            holder.txt_student_id.setText("(ID : " + profileValues.get(position).getEnrollement_no() + ")");
            holder.txt_school_name.setText(profileValues.get(position).getInstitute() + " " + profileValues.get(position).getArea() + " " + profileValues.get(position).getCity());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }


    public static class ViewHolder {
        TextView txt_student_name, txt_student_id, txt_school_name;
    }
}
