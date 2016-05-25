package com.freeappmobile.profile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.freeappmobile.AppController;
import com.freeappmobile.R;
import com.freeappmobile.custom.CustomProgressDialog;
import com.freeappmobile.model.DynamicContentDTO;
import com.freeappmobile.model.SaveStudentDTO;
import com.freeappmobile.model.SaveStudentListDTO;
import com.freeappmobile.model.SchoolDTO;
import com.freeappmobile.preferences.FeeAppPreferences;
import com.freeappmobile.profile.adapter.StudentListAdapter;
import com.freeappmobile.utils.BaseActivity;
import com.freeappmobile.utils.Utils;
import com.freeappmobile.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserDetailsActivity extends BaseActivity implements SwipeMenuListView.OnMenuItemClickListener {


    private Context mActivity;
    private SwipeMenuListView listView;
    private ArrayList<SaveStudentListDTO> saveStudentList;
    private StudentListAdapter studentListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        init();
    }


    private void init() {
        setHeader("User Details");
        setLeftClick();
        mActivity = UserDetailsActivity.this;
        listView = (SwipeMenuListView) findViewById(R.id.list_students);
        createSwipeMenu();

        if (!FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {
            setTextViewText(R.id.txt_phone_nmber, FeeAppPreferences.getPhoneNumber(mActivity));
        }
        if (!FeeAppPreferences.getEmailId(mActivity).equals("")) {
            setTextViewText(R.id.txt_email, FeeAppPreferences.getEmailId(mActivity));
        }

        if (FeeAppPreferences.getPhoneNumber(mActivity) != null && !FeeAppPreferences.getPhoneNumber(mActivity).equals("")) {
            getSaveStudentDetails(FeeAppPreferences.getUDID(mActivity), FeeAppPreferences.getPhoneNumber(mActivity));
        }


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
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


    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                        SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity);
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                                0x00, 0x00)));
                        deleteItem.setWidth(convert_dp_to_px(90));

                        deleteItem.setTitle("Delete");
                        // set item title fontsize
                        deleteItem.setTitleSize(18);
                        // set item title font color
                        deleteItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(deleteItem);


                        break;


                }
            }
        };

// set creator
        listView.setMenuCreator(creator);


    }

    private int convert_dp_to_px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

        switch (menu.getViewType()) {
            case 0:
                deleteSaveData(FeeAppPreferences.getUDID(mActivity), FeeAppPreferences.getPhoneNumber(mActivity), saveStudentList.get(position).getId(), position);
                break;

        }

        return false;
    }


    private void getSaveStudentDetails(String deviceID, String mobilenumber) {
        if (Utils.isOnline(mActivity)) {
            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getSavedStudents(deviceID, mobilenumber), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {

                                    SaveStudentDTO saveStudentDTO = new Gson().fromJson(response.getJSONObject("results").toString(), SaveStudentDTO.class);

                                    setData(saveStudentDTO);
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


    private void deleteSaveData(String deviceID, String mobilenumber, String id, final int position) {
        if (Utils.isOnline(mActivity)) {
            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.deleteSavedStudent(deviceID, mobilenumber, id), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    saveStudentList.remove(position);
                                    studentListAdapter.notifyDataSetChanged();
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


    private void setData(SaveStudentDTO saveStudentDTO) {

        saveStudentList = saveStudentDTO.getStudent_details();
        studentListAdapter = new StudentListAdapter(mActivity, saveStudentList);
        listView.setAdapter(studentListAdapter);
        listView.setOnMenuItemClickListener(this);
        setListViewHeightBasedOnChildren(listView);

    }

}
