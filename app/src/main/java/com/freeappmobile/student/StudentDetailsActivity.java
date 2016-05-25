package com.freeappmobile.student;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freeappmobile.AppController;
import com.freeappmobile.R;
import com.freeappmobile.custom.CustomProgressDialog;
import com.freeappmobile.model.LocationDTO;
import com.freeappmobile.model.SchoolDTO;
import com.freeappmobile.model.StudentDTO;
import com.freeappmobile.utils.BaseActivity;
import com.freeappmobile.utils.FetchPopUpSelectValue;
import com.freeappmobile.utils.PopUpFragment;
import com.freeappmobile.utils.Utils;
import com.freeappmobile.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class StudentDetailsActivity extends BaseActivity implements FetchPopUpSelectValue {

    private Context mActivity;

    private LocationDTO locationDTO;
    private SchoolDTO schoolDTO;
    private StudentDTO studentDTO;
    private ArrayList<SchoolDTO> instituteList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        init();
    }


    private void init() {
        setHeader("Student Details");
        setLeftClick();
        mActivity = StudentDetailsActivity.this;
        setClick(R.id.btn_confirm_student);
        setClick(R.id.txt_change);
        setClick(R.id.txt_student_dob);


        locationDTO = (LocationDTO) getIntent().getSerializableExtra("locationDTO");
        schoolDTO = (SchoolDTO) getIntent().getSerializableExtra("instituteDTO");

        instituteList = (ArrayList<SchoolDTO>) getIntent().getSerializableExtra("instituteList");
        setTextViewText(R.id.txt_institute_name, schoolDTO.getName());
        setTextViewText(R.id.txt_city_name, locationDTO.getValue());


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm_student:
                Intent intent = new Intent(mActivity, FeeStructureActivity.class);
                intent.putExtra("userName", getEditTextText(R.id.txt_student_enrollment_number));
                intent.putExtra("dob", getTextViewText(R.id.txt_student_dob));
                intent.putExtra("id", schoolDTO.getId());
                intent.putExtra("studentDTO", studentDTO);
                intent.putExtra("saveStudent", isCheckboxChecked(R.id.chk_save_details) ? "1" : "0");
                startActivity(intent);


                break;
            case R.id.txt_change:

                PopUpFragment dialogFragment = new PopUpFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("popUpList", instituteList);
                bundle.putString("title", "Select Institutes");
                dialogFragment.setArguments(bundle);
                dialogFragment.setFetchSelectedInterface(mActivity);
                dialogFragment.show(getFragmentManager(), "");

                break;

            case R.id.txt_student_dob:
                showCalendarDialog();
                break;

            case R.id.back_btn:
                onBackPressed();
                break;
        }
    }


    public void showCalendarDialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = 0;
        int mMonth = 0;
        int mDay = 0;
        if (getTextViewText(R.id.txt_student_dob).equals("")) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String date[] = getTextViewText(R.id.txt_student_dob).split("-");
            mYear = Integer.parseInt(date[2]);
            mMonth = Integer.parseInt(date[0]) - 1;
            mDay = Integer.parseInt(date[1]);

        }
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        setViewText(R.id.txt_student_dob, (monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        Utils.hideKeyboard((Activity) mActivity);
                        getStudentDetails(schoolDTO.getId(), getEditTextText(R.id.txt_student_enrollment_number), getTextViewText(R.id.txt_student_dob), locationDTO.getType());


                    }
                }, mYear, mMonth, mDay);

        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();


    }

    @Override
    public void selectedValue(int position, String tag) {

        schoolDTO = instituteList.get(position);
        setTextViewText(R.id.txt_institute_name, schoolDTO.getName());
        setEditText(R.id.txt_student_enrollment_number, "");
        setTextViewText(R.id.txt_student_dob, "");
        setViewVisibility(R.id.ll_institute_details, View.GONE);


    }


    private void getStudentDetails(String instituteId, String userName, String dob, String type) {

        if (validateForm(userName, dob)) {

            if (Utils.isOnline(mActivity)) {

                CustomProgressDialog.showProgDialog((Activity) mActivity, null);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getStudentDetails(instituteId, userName, dob, type), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    if (Utils.getWebServiceStatus(response) && Utils.getWebServiceMessage(response).equals("")) {
                                        studentDTO = new Gson().fromJson(response.getJSONObject("results").toString(), StudentDTO.class);

                                        setViewVisibility(R.id.ll_institute_details, View.VISIBLE);

                                        setTextViewText(R.id.txt_student_name, studentDTO.getName());
                                        setTextViewText(R.id.txt_student_class, studentDTO.getClass_division());

                                    } else {
                                        setEditText(R.id.txt_student_enrollment_number, "");
                                        setTextViewText(R.id.txt_student_dob, "");
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
                        setEditText(R.id.txt_student_enrollment_number, "");
                        setTextViewText(R.id.txt_student_dob, "");
                        Utils.showDialog(mActivity, "Error", "Invalid Student Details.");


                    }
                });
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            } else {
                Utils.showNoNetworkDialog(mActivity);
            }

        } else {
            Utils.showDialog(mActivity, "Warning", "Please enter fields ");
        }
    }


    private boolean validateForm(String userName, String dob) {


        if (userName.equals("")) {
            Utils.showDialog(mActivity, "Error", "Please enter student enrollment number.");
            return false;

        } else if (dob.equals("")) {
            Utils.showDialog(mActivity, "Error", "Please enter student DOB.");
            return false;
        }
        return true;
    }
}
