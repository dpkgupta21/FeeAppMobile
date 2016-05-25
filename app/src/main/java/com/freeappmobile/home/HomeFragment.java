package com.freeappmobile.home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.freeappmobile.AppController;
import com.freeappmobile.R;
import com.freeappmobile.custom.CustomProgressDialog;
import com.freeappmobile.model.LocationDTO;
import com.freeappmobile.model.SchoolDTO;
import com.freeappmobile.model.TransactionListDTO;
import com.freeappmobile.preferences.FeeAppPreferences;
import com.freeappmobile.profile.UserDetailsActivity;
import com.freeappmobile.student.StudentDetailsActivity;
import com.freeappmobile.home.adapter.FrequentTransactionAdapter;
import com.freeappmobile.transfer.adapter.TransactionAdapter;
import com.freeappmobile.utils.BaseFragment;
import com.freeappmobile.utils.FetchPopUpSelectValue;
import com.freeappmobile.utils.PopUpFragment;
import com.freeappmobile.utils.Utility;
import com.freeappmobile.utils.Utils;
import com.freeappmobile.utils.adapter.LocationAdapter;
import com.freeappmobile.utils.adapter.PopUpAdapter;
import com.freeappmobile.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends BaseFragment implements FetchPopUpSelectValue {


    private View view;
    private Context mActivity;
    private AutoCompleteTextView autoCompleteTextView;
    private ListView listViewFrequentTransactions;
    private ArrayList<SchoolDTO> institutesList;
    private LocationDTO locationDTO;
    private SchoolDTO schoolDTO;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init() {
        mActivity = getActivity();

        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.txt_location);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                callWebservice(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof LocationDTO) {
                    locationDTO = (LocationDTO) item;
                    autoCompleteTextView.setText(((LocationDTO) item).getName());
                    Utils.hideKeyboard((Activity) mActivity);
                    getInstituteList(locationDTO.getType(), locationDTO.getValue());
                }
            }
        });
        listViewFrequentTransactions = (ListView) view.findViewById(R.id.list_view_frequent_transaction);


        if (FeeAppPreferences.getPhoneNumber(mActivity) != null && !FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {

            getAllTransactionList(FeeAppPreferences.getUDID(mActivity), FeeAppPreferences.getPhoneNumber(mActivity), "frequent");
        } else {
            setViewVisibility(R.id.ll_frequent, view, View.GONE);
        }


        setClick(R.id.txt_change_city, view);
        setClick(R.id.txt_institute_name, view);
        setClick(R.id.btn_proceed, view);
        setClick(R.id.txt_save_student, view);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);

        PopUpFragment dialogFragment;
        switch (arg0.getId()) {
            case R.id.txt_institute_name:

                if (institutesList != null && institutesList.size() > 0) {
                    dialogFragment = new PopUpFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("popUpList", institutesList);
                    bundle.putString("title", "Select Institutes");
                    dialogFragment.setArguments(bundle);
                    dialogFragment.setFetchSelectedInterface(HomeFragment.this);
                    dialogFragment.show(HomeFragment.this.getActivity().getFragmentManager(), "");
                }
                break;

            case R.id.txt_change_city:
                autoCompleteTextView.setText("");
                setViewText(R.id.txt_institute_name, "", view);
                institutesList = null;
                break;

            case R.id.btn_proceed:


                if (validateForm()) {
                    Intent intent = new Intent(mActivity, StudentDetailsActivity.class);
                    intent.putExtra("locationDTO", locationDTO);
                    intent.putExtra("instituteDTO", schoolDTO);
                    intent.putExtra("instituteList", institutesList);
                    startActivity(intent);
                }
                break;
            case R.id.txt_save_student:
                startActivity(new Intent(mActivity, UserDetailsActivity.class));
                break;

        }

    }


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public void selectedValue(int position, String tag) {
        switch (tag) {

            case "Select Institutes":

                schoolDTO = institutesList.get(position);
                setViewText(R.id.txt_institute_name, schoolDTO.getName(), view);
                break;
        }

    }


    private void callWebservice(String term) {
        if (!term.equals("")) {
            if (Utils.isOnline(mActivity)) {
                CustomJsonRequest postReq = new CustomJsonRequest(
                        Utils.createLocationUrl(term), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog("Nicky", "Response -> " + response.toString());
                                try {

                                    Type type = new TypeToken<ArrayList<LocationDTO>>() {
                                    }.getType();
                                    List<LocationDTO> locationList = new Gson().fromJson(response.getJSONArray("results").toString(), type);
                                    LocationAdapter locationAdapter = new LocationAdapter(mActivity, locationList);
                                    autoCompleteTextView.setAdapter(locationAdapter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
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
    }


    private void getInstituteList(String type, String location) {
        if (Utils.isOnline(mActivity)) {
            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getInstituteUrl(type, location), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Type type = new TypeToken<ArrayList<SchoolDTO>>() {
                                    }.getType();
                                    institutesList = new Gson().fromJson(response.getJSONArray("results").toString(), type);
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


    private boolean validateForm() {
        if (getViewText(R.id.txt_institute_name, view).equals("")) {
            Utils.showDialog(mActivity, "Error", "Please enter location.");
            return false;
        } else if (getViewText(R.id.txt_location, view).equals("")) {
            Utils.showDialog(mActivity, "Error", "Please select Institute name.");
            return false;
        }

        return true;
    }


    private void getAllTransactionList(String deviceID, String mobileNumber, String type) {
        if (Utils.isOnline(mActivity)) {
            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getTransactions(deviceID, mobileNumber, type), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {

                                    TransactionListDTO transactionListDTO = new Gson().fromJson(response.getJSONObject("results").toString(), TransactionListDTO.class);

                                    setData(transactionListDTO);
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


    private void setData(TransactionListDTO transactionListDTO) {

        if (transactionListDTO.getPayment_details() != null && transactionListDTO.getPayment_details().size() > 0)

        {
            setViewVisibility(R.id.ll_frequent, view, View.VISIBLE);
            FrequentTransactionAdapter frequentTransactionAdapter = new FrequentTransactionAdapter(mActivity, transactionListDTO.getPayment_details());
            listViewFrequentTransactions.setAdapter(frequentTransactionAdapter);
            setListViewHeightBasedOnChildren(listViewFrequentTransactions);
        } else {
            setViewVisibility(R.id.ll_frequent, view, View.GONE);
        }
    }


}

