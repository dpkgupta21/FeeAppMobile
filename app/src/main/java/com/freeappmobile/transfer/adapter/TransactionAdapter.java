package com.freeappmobile.transfer.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freeappmobile.R;
import com.freeappmobile.model.TransactionDTO;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<TransactionDTO> transactionList;
    private static MyClickListener myClickListener;

    public TransactionAdapter(Context context, List<TransactionDTO> transactionList) {
        this.context = context;
        this.transactionList = transactionList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.transaction_row_layout, parent, false);
        viewHolder = new TransactionViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        TransactionViewHolder passPortViewHolder = (TransactionViewHolder) holder;
        configureTransactionViewHolder(passPortViewHolder, position);


    }


    public static class TransactionViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        TextView txt_date, txt_price, txt_name, txt_id, txt_institute_name, txt_fees_type, txt_repeat;


        public TransactionViewHolder(View itemView) {

            super(itemView);

            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_id = (TextView) itemView.findViewById(R.id.txt_id);
            txt_institute_name = (TextView) itemView.findViewById(R.id.txt_institute_name);
            txt_fees_type = (TextView) itemView.findViewById(R.id.txt_fees_type);
            txt_repeat = (TextView) itemView.findViewById(R.id.txt_repeat);
            txt_repeat.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


    private void configureTransactionViewHolder(TransactionViewHolder holder, int position) {

        holder.txt_date.setText(transactionList.get(position).getTxn_date());
        holder.txt_fees_type.setText(transactionList.get(position).getFee_types());
        holder.txt_id.setText("(" + transactionList.get(position).getInvoice_id() + ")");
        holder.txt_institute_name.setText(transactionList.get(position).getInstitute());
        holder.txt_name.setText(transactionList.get(position).getStudent());
        holder.txt_price.setText("Rs. " + transactionList.get(position).getFees() + "/-");

    }


    @Override
    public int getItemCount() {
        //return transactionList.size();
        return transactionList.size();
    }


}
