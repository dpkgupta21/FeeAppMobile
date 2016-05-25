package com.freeappmobile.home;


import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freeappmobile.AppController;
import com.freeappmobile.utils.Utils;
import com.freeappmobile.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstituteDetailsHandler implements Runnable {
    private static final String TAG = "Institutes";
    private Context mActivity;
    String type;
    String location;

    public InstituteDetailsHandler(Context mActivity, String type, String location) {
        this.mActivity = mActivity;
        this.type = type;
        this.location = location;
    }


    @Override
    public void run() {
        getInstituteList();

    }

    private void getInstituteList() {
        if (Utils.isOnline(mActivity)) {

            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.GET, Utils.getInstituteUrl(type, location), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
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

        }

    }

}
