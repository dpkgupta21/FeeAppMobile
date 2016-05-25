package com.freeappmobile.transfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freeappmobile.AppController;
import com.freeappmobile.R;
import com.freeappmobile.custom.CustomProgressDialog;
import com.freeappmobile.home.HomeActivity;
import com.freeappmobile.home.adapter.FrequentTransactionAdapter;
import com.freeappmobile.model.SaveStudentDTO;
import com.freeappmobile.model.TransactionListDTO;
import com.freeappmobile.preferences.FeeAppPreferences;
import com.freeappmobile.transfer.adapter.TransactionAdapter;
import com.freeappmobile.utils.BaseFragment;
import com.freeappmobile.utils.Utils;
import com.freeappmobile.volley.CustomJsonRequest;
import com.google.gson.Gson;

import org.json.JSONObject;


public class TransactionFragment extends BaseFragment {

    private View view;
    private Context mActivity;
    private Button btn_all, btn_frequent;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListView listViewFrequentTransactions;


    public TransactionFragment() {
        // Required empty public constructor
    }


    public static TransactionFragment newInstance() {
        TransactionFragment fragment = new TransactionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideKeyboard((Activity) getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init() {
        mActivity = getActivity();
        btn_all = (Button) view.findViewById(R.id.btn_all);
        btn_frequent = (Button) view.findViewById(R.id.btn_frequent);
        setTouchNClick(R.id.btn_all, view);
        setTouchNClick(R.id.btn_frequent, view);
        btn_all.setSelected(true);
        btn_frequent.setTextColor(mActivity.getResources().getColor(R.color.btn_bg));


        if (FeeAppPreferences.getPhoneNumber(mActivity) != null && !FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {
            getAllTransactionList(FeeAppPreferences.getUDID(mActivity), FeeAppPreferences.getPhoneNumber(mActivity), "all");
        }
        listViewFrequentTransactions = (ListView) view.findViewById(R.id.list_view_frequent_transaction);
        listViewFrequentTransactions.setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyl_transaction_all);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_all:
                btn_all.setSelected(true);
                btn_frequent.setSelected(false);
                btn_all.setTextColor(mActivity.getResources().getColor(R.color.white));
                btn_frequent.setTextColor(mActivity.getResources().getColor(R.color.btn_bg));
                if (FeeAppPreferences.getPhoneNumber(mActivity) != null && !FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {
                    getAllTransactionList(FeeAppPreferences.getUDID(mActivity), FeeAppPreferences.getPhoneNumber(mActivity), "all");
                }
                listViewFrequentTransactions.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_frequent:
                btn_all.setSelected(false);
                btn_frequent.setSelected(true);
                btn_frequent.setTextColor(mActivity.getResources().getColor(R.color.white));
                btn_all.setTextColor(mActivity.getResources().getColor(R.color.btn_bg));
                if (FeeAppPreferences.getPhoneNumber(mActivity) != null && !FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {
                    getAllTransactionList(FeeAppPreferences.getUDID(mActivity), FeeAppPreferences.getPhoneNumber(mActivity), "frequent");
                }

                listViewFrequentTransactions.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                break;

        }
    }


    private void getAllTransactionList(String deviceID, String mobileNumber, final String type) {
        if (Utils.isOnline(mActivity)) {
            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getTransactions(deviceID, mobileNumber, type), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {

                                    TransactionListDTO transactionListDTO = new Gson().fromJson(response.getJSONObject("results").toString(), TransactionListDTO.class);

                                    setData(transactionListDTO, type);
                                } else {
                                    Utils.showDialog(mActivity, "Error", Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(mActivity);

                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            Utils.showNoNetworkDialog(mActivity);
        }

    }


    private void setData(TransactionListDTO transactionListDTO, String type) {


        if (type.equalsIgnoreCase("all")) {
            if (transactionListDTO.getPayment_details() != null && transactionListDTO.getPayment_details().size() > 0)

            {
                mAdapter = new TransactionAdapter(mActivity, transactionListDTO.getPayment_details());
                mRecyclerView.setAdapter(mAdapter);

                ((TransactionAdapter) mAdapter).setOnItemClickListener(new TransactionAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        switch (v.getId()) {
                            case R.id.txt_repeat:

                                break;
                        }
                    }
                });
            }

        } else {

            if (transactionListDTO.getPayment_details() != null && transactionListDTO.getPayment_details().size() > 0)

            {
                FrequentTransactionAdapter frequentTransactionAdapter = new FrequentTransactionAdapter(mActivity, transactionListDTO.getPayment_details());
                listViewFrequentTransactions.setAdapter(frequentTransactionAdapter);

            }
        }
    }

}
