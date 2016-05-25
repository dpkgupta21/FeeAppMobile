package com.freeappmobile.profile;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freeappmobile.AppController;
import com.freeappmobile.R;
import com.freeappmobile.custom.CustomProgressDialog;
import com.freeappmobile.model.DynamicContentDTO;
import com.freeappmobile.model.SchoolDTO;
import com.freeappmobile.utils.BaseActivity;
import com.freeappmobile.utils.Utils;
import com.freeappmobile.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AboutActivity extends BaseActivity {


    private Context mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        callWebservice("about_fee_app");
    }


    private void init() {
        mActivity = AboutActivity.this;
        setHeader("About");
        setLeftClick();
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


    private void callWebservice(final String forType) {
        if (Utils.isOnline(mActivity)) {
            CustomProgressDialog.showProgDialog((Activity) mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getDynamicContent(forType), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    DynamicContentDTO dynamicContentDTO = new Gson().fromJson(response.getJSONObject("results").toString(), DynamicContentDTO.class);

                                    setData(dynamicContentDTO, forType);
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

    private void setData(DynamicContentDTO dynamicContentDTO, String forType) {

        if (forType.equals("about_fee_app")) {

            setTextViewText(R.id.txt_title_app, dynamicContentDTO.getTitle());
            setTextViewText(R.id.txt_description_app, dynamicContentDTO.getDescription());
            callWebservice("about_developer");
        } else {
            setTextViewText(R.id.txt_title_developer, dynamicContentDTO.getTitle());
            setTextViewText(R.id.txt_description_developer, dynamicContentDTO.getDescription());

        }

    }
}
