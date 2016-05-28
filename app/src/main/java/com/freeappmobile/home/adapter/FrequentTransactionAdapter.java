package com.freeappmobile.home.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.freeappmobile.R;
import com.freeappmobile.model.TransactionDTO;
import com.freeappmobile.student.FeeStructureActivity;

import java.util.List;

public class FrequentTransactionAdapter extends BaseAdapter {


    private Context context;
    private List<TransactionDTO> transactionList;

    public FrequentTransactionAdapter(Context context, List<TransactionDTO> transactionList) {

        this.context = context;
        this.transactionList = transactionList;

    }


    @Override
    public int getCount() {

        return transactionList.size();

    }

    @Override
    public Object getItem(int position) {
        return transactionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        try {

            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) ((Activity) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (View) layoutInflater.inflate(R.layout.frequent_transaction_row_layout, parent, false);
                holder = new ViewHolder();
                holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
                holder.txt_price = (TextView) view.findViewById(R.id.txt_price);
                holder.txt_id = (TextView) view.findViewById(R.id.txt_id);
                holder.txt_institute_name = (TextView) view.findViewById(R.id.txt_institute_name);
                holder.txt_fees_type = (TextView) view.findViewById(R.id.txt_fees_type);
                holder.txt_date = (TextView) view.findViewById(R.id.txt_date);
                holder.txt_repeat = (TextView) view.findViewById(R.id.txt_repeat);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            holder.txt_date.setText("(Last on " + transactionList.get(position).getTxn_date() + ")");
            holder.txt_fees_type.setText(transactionList.get(position).getFee_types());
            holder.txt_id.setText(transactionList.get(position).getInvoice_id());
            holder.txt_institute_name.setText(transactionList.get(position).getInstitute());
            holder.txt_name.setText(transactionList.get(position).getStudent());
            holder.txt_price.setText("RS. " + transactionList.get(position).getFees() + "/-");


            holder.txt_repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, FeeStructureActivity.class);
                    intent.putExtra("userName", transactionList.get(position).getUsername());
                    intent.putExtra("dob", transactionList.get(position).getDob());
                    intent.putExtra("id", transactionList.get(position).getInstitute_id());
                    intent.putExtra("studentName", transactionList.get(position).getStudent());
                    intent.putExtra("studentClass", transactionList.get(position).getClassName());
                    intent.putExtra("instituteName", transactionList.get(position).getInstitute());
                    intent.putExtra("saveStudent", "1");
                    context.startActivity(intent);


                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }


    public static class ViewHolder {
        TextView txt_name, txt_price, txt_id, txt_institute_name, txt_fees_type, txt_date, txt_repeat;
    }
}
