package com.freeappmobile.student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.billdesk.sdk.PaymentOptions;
import com.freeappmobile.AppController;
import com.freeappmobile.R;
import com.freeappmobile.custom.CustomProgressDialog;
import com.freeappmobile.home.HomeActivity;
import com.freeappmobile.model.FeeDurationDTO;
import com.freeappmobile.model.FeeStructureDTO;
import com.freeappmobile.model.PaymentDTO;
import com.freeappmobile.model.SchoolDTO;
import com.freeappmobile.model.StudentDTO;
import com.freeappmobile.preferences.FeeAppPreferences;
import com.freeappmobile.utils.BaseActivity;
import com.freeappmobile.utils.Constant;
import com.freeappmobile.utils.FetchPopUpSelectValue;
import com.freeappmobile.utils.PopUpFragment;
import com.freeappmobile.utils.Utils;
import com.freeappmobile.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeeStructureActivity extends BaseActivity implements FetchPopUpSelectValue {

    private Context mActivity;
    private ArrayList<FeeDurationDTO> feeDurationList;
    private String id;
    private String userName;
    private String dob;
    private String slug;
    private String saveStudent;
    private EditText editText;
    private String studentName;
    private String studentClass;
    private String instituteName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_structure);
        init();
    }


    private void init() {
        mActivity = FeeStructureActivity.this;
        setHeader("Fee Structure");
        setLeftClick();
        setClick(R.id.txt_change);
        setClick(R.id.txt_phone_change);
        setClick(R.id.btn_proceed_pay);
        setClick(R.id.txt_email_change);
        studentName = getIntent().getStringExtra("studentName");
        studentClass = getIntent().getStringExtra("studentClass");
        instituteName = getIntent().getStringExtra("instituteName");
        userName = getIntent().getStringExtra("userName");
        dob = getIntent().getStringExtra("dob");
        id = getIntent().getStringExtra("id");
        saveStudent = getIntent().getStringExtra("saveStudent");
        Utils.hideKeyboard((Activity) mActivity);
        setTextViewText(R.id.txt_student_name, studentName);
        setTextViewText(R.id.txt_student_id, "(ID : " + userName + ")");
        setTextViewText(R.id.txt_class_school, "Class " + studentClass + ", " + instituteName);

        if (FeeAppPreferences.getEmailId(mActivity) != null && !FeeAppPreferences.getEmailId(mActivity).equals("")) {

            setViewVisibility(R.id.txt_email_id, View.VISIBLE);
            setViewVisibility(R.id.txt_email_change, View.VISIBLE);
            setViewVisibility(R.id.email_id, View.GONE);
            setTextViewText(R.id.txt_email_id, FeeAppPreferences.getEmailId(mActivity));

        } else {
            setViewVisibility(R.id.txt_email_id, View.GONE);
            setViewVisibility(R.id.txt_email_change, View.GONE);
            setViewVisibility(R.id.email_id, View.VISIBLE);

        }


        if (FeeAppPreferences.getPhoneNumber(mActivity) != null && !FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {
            setViewVisibility(R.id.txt_phone_number, View.VISIBLE);
            setViewVisibility(R.id.txt_phone_change, View.VISIBLE);
            setViewVisibility(R.id.et_phone_number, View.GONE);
            setTextViewText(R.id.txt_phone_number, "+91 - " + FeeAppPreferences.getPhoneNumber(mActivity));

        } else {
            setViewVisibility(R.id.txt_phone_number, View.GONE);
            setViewVisibility(R.id.txt_phone_change, View.GONE);
            setViewVisibility(R.id.et_phone_number, View.VISIBLE);
        }


        editText = (EditText) findViewById(R.id.et_phone_number);
        editText.setText("+91 - " + FeeAppPreferences.getPhoneNumber(mActivity));
        Selection.setSelection(editText.getText(), editText.getText().length());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().contains("+91 - ")) {
                    editText.setText("+91 - " + FeeAppPreferences.getPhoneNumber(mActivity));
                    Selection.setSelection(editText.getText(), editText.getText().length());

                }
            }
        });
        slug = "yearly";
        getFeeStructure(id, userName, dob, slug);
        getTenureDetails(id);


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {

            case R.id.txt_change:

                if (feeDurationList != null && feeDurationList.size() > 0) {

                    PopUpFragment dialogFragment = new PopUpFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("popUpList", feeDurationList);
                    bundle.putString("title", "Select Tenure");
                    dialogFragment.setArguments(bundle);
                    dialogFragment.setFetchSelectedInterface(mActivity);
                    dialogFragment.show(getFragmentManager(), "");

                } else {
                    getTenureDetails(id);
                }

                break;

            case R.id.txt_phone_change:

                setViewVisibility(R.id.txt_phone_number, View.GONE);
                setViewVisibility(R.id.et_phone_number, View.VISIBLE);
                setViewVisibility(R.id.txt_phone_change, View.GONE);
                setTextViewText(R.id.et_phone_number, FeeAppPreferences.getPhoneNumber(mActivity));
                break;

            case R.id.txt_email_change:
                setViewVisibility(R.id.txt_email_id, View.GONE);
                setViewVisibility(R.id.email_id, View.VISIBLE);
                setViewVisibility(R.id.txt_email_change, View.GONE);
                setTextViewText(R.id.email_id, FeeAppPreferences.getEmailId(mActivity));
                break;

            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.btn_proceed_pay:

                 doTxn();

//                Utils.hideKeyboard((Activity) mActivity);
//                if (validateForm()) {
//                    payment();
//                }
                break;


        }


    }


    @Override
    public void selectedValue(int position, String tag) {
        switch (tag) {

            case "Select Tenure":
                setTextViewText(R.id.txt_fee_tenure_type, feeDurationList.get(position).getName());
                getFeeStructure(id, userName, dob, feeDurationList.get(position).getSlug());
                slug = feeDurationList.get(position).getSlug();
                break;
        }

    }


    private void getFeeStructure(String id, String userName, String dob, String type) {


        if (Utils.isOnline(mActivity)) {

            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getFeeStructure(id, userName, dob, type), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {


                                FeeStructureDTO feeStructureDTO = new Gson().fromJson(response.getJSONObject("results").toString(), FeeStructureDTO.class);

                                setValues(feeStructureDTO);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();

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


    private void setValues(FeeStructureDTO feeStructureDTO) {
        LinearLayout parentLinearLayout = (LinearLayout) findViewById(R.id.linear_fee_ll);
        parentLinearLayout.removeAllViews();


        for (int i = 0; i < feeStructureDTO.getFees_details().size(); i++) {

            RelativeLayout.LayoutParams paramsFirst = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            paramsFirst.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView txtView = new TextView(this);
            txtView.setId(i);
            txtView.setTextAppearance(mActivity, R.style.txt_black_color);
            txtView.setText(feeStructureDTO.getFees_details().get(i).getName() + " : " +
                    feeStructureDTO.getFees_details().get(i).getAmount());


            parentLinearLayout.addView(txtView, paramsFirst);

        }

        RelativeLayout.LayoutParams convenienceParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        convenienceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextView convenienceTxtView = new TextView(this);
        convenienceTxtView.setId(R.id.txt_con);
        convenienceTxtView.setTextAppearance(mActivity, R.style.txt_black_color);
        convenienceTxtView.setText("Convenience : " + feeStructureDTO.getConvenience_fee());

        parentLinearLayout.addView(convenienceTxtView, convenienceParams);


//        RelativeLayout.LayoutParams totalParams = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        totalParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        CustomTextViewBold totalTxtView= new CustomTextViewBold(this);
//        totalTxtView.setId(R.id.txt_total_col);
//        totalTxtView.setTextSize((float) 16);
//        totalTxtView.setTextAppearance(mActivity, R.style.txt_black_color);
//        totalTxtView.setText(feeStructureDTO.getTotal_fee());

        RelativeLayout relativeLayout = new RelativeLayout(mActivity);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );


        //for total value
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.txt_con);

        TextView totalval = new TextView(this);
        totalval.setId(R.id.txt_tatal_val);
        totalval.setTextAppearance(mActivity, R.style.txt_btn_bg_color_bold);
        totalval.setText(feeStructureDTO.getTotal_fee());
        totalval.setTextSize(18f);
        relativeLayout.addView(totalval, layoutParams);

        // for tatal colon
        RelativeLayout.LayoutParams colParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        colParams.addRule(RelativeLayout.LEFT_OF, R.id.txt_tatal_val);
        colParams.addRule(RelativeLayout.BELOW, R.id.txt_con);
        colParams.setMargins(0, 0, 10, 0);
        TextView totalcol = new TextView(this);
        totalcol.setText(":");
        totalcol.setId(R.id.txt_total_col);
        totalcol.setTextAppearance(mActivity, R.style.txt_black_color);
        totalcol.setTextSize(18f);
        relativeLayout.addView(totalcol, colParams);

        //for total text
        RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        txtParams.addRule(RelativeLayout.LEFT_OF, R.id.txt_total_col);
        txtParams.addRule(RelativeLayout.BELOW, R.id.txt_con);
        txtParams.setMargins(0, 0, 10, 0);
        TextView total = new TextView(this);
        total.setTextAppearance(mActivity, R.style.txt_black_color);
        total.setText("Total");
        total.setTextSize(18f);
        relativeLayout.addView(total, txtParams);

        parentLinearLayout.addView(relativeLayout, relativeLayoutParams);


    }

    private void getTenureDetails(String id) {
        if (Utils.isOnline(mActivity)) {
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getFeeDuration(id), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Type type = new TypeToken<ArrayList<FeeDurationDTO>>() {
                                }.getType();

                                feeDurationList = new Gson().fromJson(response.getJSONArray("results").toString(), type);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
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


    private void payment() {
        if (Utils.isOnline(mActivity)) {

            final String emailID;
            if (!getEditTextText(R.id.email_id).equals("")) {
                emailID = getEditTextText(R.id.email_id);
            } else {
                emailID = FeeAppPreferences.getEmailId(mActivity);
            }

            final String phoneNumber;
            if (getEditTextText(R.id.et_phone_number).length() > 6) {
                phoneNumber = getEditTextText(R.id.et_phone_number).substring(6, 16);
            } else {
                phoneNumber = FeeAppPreferences.getPhoneNumber(mActivity);
            }

            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            Map<String, String> params = new HashMap<>();
            params.put("institute_id", id);
            params.put("username", userName);
            params.put("dob", dob);
            params.put("payer_email", emailID);
            params.put("payer_mobile", phoneNumber);
            params.put("device_id", FeeAppPreferences.getUDID(mActivity));
            params.put("fees_type", slug);
            params.put("save_student", saveStudent);

            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constant.BASE_URL + Constant.MAKE_PAYMENT + "?token=" + Constant.TOKEN, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {

                                    FeeAppPreferences.setEmailId(mActivity, emailID);
                                    FeeAppPreferences.setPhoneNumber(mActivity, phoneNumber);
                                    PaymentDTO paymentDTO = new Gson().fromJson(response.getJSONObject("results").toString(), PaymentDTO.class);
                                    callPaymentDetails(paymentDTO);
                                } else {
                                    Utils.customDialog(Utils.getWebServiceMessage(response), mActivity);
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


        if (getEditTextText(R.id.email_id).equals("") && FeeAppPreferences.getEmailId(mActivity).equals("")) {
            Utils.showDialog(mActivity, "Error", "Please enter email id.");
            return false;
        } else if (getEditTextText(R.id.et_phone_number).length() < 16 && FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {
            Utils.showDialog(mActivity, "Error", "Please enter phone number");
            return false;
        }


        return true;
    }


    private void callPaymentDetails(PaymentDTO paymentDTO) {

        if (Utils.isOnline(mActivity)) {

            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getTransactionDetails(FeeAppPreferences.getUDID(mActivity), FeeAppPreferences.getPhoneNumber(mActivity), paymentDTO.getTxn_id(), paymentDTO.getInvoice_id()), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                if (Utils.getWebServiceStatus(response)) {

                                    Intent intent = new Intent(mActivity, HomeActivity.class);
                                    intent.putExtra("fragmentNumber", 1);
                                    startActivity(intent);
                                    // Toast.makeText(mActivity, "Payment done successfully.", Toast.LENGTH_LONG).show();
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


    private void doTxn() {

        SampleCallBack callbackObj = new SampleCallBack(); //callback instance

        String msg = Utils.getMsg("1", Utils.randomTxnNumber(mActivity));
        Intent intent = new Intent(mActivity, PaymentOptions.class);
        intent.putExtra("msg", msg); //pg_msg
        intent.putExtra("user-email", "nickygupta02@gmail.com");
        intent.putExtra("user-mobile", "9530299738");
        intent.putExtra("callback", callbackObj);
        startActivity(intent);
    }


}




