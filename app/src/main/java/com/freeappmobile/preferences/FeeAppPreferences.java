package com.freeappmobile.preferences;


import android.content.Context;
import android.content.SharedPreferences;

public class FeeAppPreferences {

    public static final String PREF_NAME = "FEE_PREFERENCES";

    public static String PHONE_NUMBER = "PHONENUMBER";
    public static String EMAIL_ID = "EMAILID";
    public static String UDID = "UDID";


    public static String getPhoneNumber(Context context) {
        String countryCode = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                PHONE_NUMBER, "");
        return countryCode;

    }

    public static void setPhoneNumber(Context context, String phoneNumber) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PHONE_NUMBER, String.valueOf(phoneNumber));
        editor.apply();
    }


    public static String getEmailId(Context context) {
        String countryCode = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                EMAIL_ID, "");
        return countryCode;

    }

    public static void setEmailId(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL_ID, String.valueOf(email));
        editor.apply();
    }


    public static String getUDID(Context context) {
        String countryCode = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                UDID, "");
        return countryCode;

    }

    public static void setUDID(Context context, String udid) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UDID, String.valueOf(udid));
        editor.apply();
    }


}
