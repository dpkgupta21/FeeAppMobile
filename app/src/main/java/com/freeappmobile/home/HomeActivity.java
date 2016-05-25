package com.freeappmobile.home;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.freeappmobile.R;
import com.freeappmobile.profile.ProfileFragment;
import com.freeappmobile.transfer.TransactionFragment;
import com.freeappmobile.utils.BaseActivity;

public class HomeActivity extends BaseActivity {
    private Context mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

    }


    private void init() {
        mActivity = HomeActivity.this;
        setClick(R.id.rr_home);
        setClick(R.id.rr_profile);
        setClick(R.id.rr_transaction);

        openFragment(0);
    }


    private void openFragment(int fragmentNumber) {

        Fragment fragment = null;

        switch (fragmentNumber) {
            case 0:
                setHeaderTransparent();
                show(true, false, false);
                fragment = HomeFragment.newInstance();
                break;

            case 1:
                setHeader("Transactions");
                setLeftClick();
                show(false, true, false);
                fragment = TransactionFragment.newInstance();
                break;

            case 2:
                setHeader("User Profile");
                setLeftClick();
                show(false, false, true);
                fragment = ProfileFragment.newInstance();
                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            // fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame_lay, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
            fragmentTransaction.commit();

        }


    }


    private void show(boolean home, boolean transaction, boolean profile) {
        if (home) {


            setImageResourseBackground(R.id.img_home, R.drawable.home_btn_select);
            setImageResourseBackground(R.id.img_transaction, R.drawable.transactions_btn);
            setImageResourseBackground(R.id.img_profile, R.drawable.profile_btn);

        } else if (transaction)

        {

            setImageResourseBackground(R.id.img_home, R.drawable.home_btn);
            setImageResourseBackground(R.id.img_transaction, R.drawable.transactions_btn_select);
            setImageResourseBackground(R.id.img_profile, R.drawable.profile_btn);


        } else {

            setImageResourseBackground(R.id.img_home, R.drawable.home_btn);
            setImageResourseBackground(R.id.img_transaction, R.drawable.transactions_btn);
            setImageResourseBackground(R.id.img_profile, R.drawable.profile_btn_select);


        }


    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rr_home:
                openFragment(0);
                break;

            case R.id.rr_transaction:
                openFragment(1);
                break;

            case R.id.rr_profile:
                openFragment(2);
                break;

            case R.id.back_btn:
                onBackPressed();
                break;

        }

    }

}


