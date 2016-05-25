package com.freeappmobile.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.freeappmobile.R;
import com.freeappmobile.home.HomeActivity;
import com.freeappmobile.profile.adapter.UserProfileListAdapter;
import com.freeappmobile.utils.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends BaseFragment {

    private View view;
    private Context mActivity;
    private ListView userProfileListView;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

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
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init() {
        mActivity = getActivity();

        userProfileListView = (ListView) view.findViewById(R.id.list_user_profile);
        UserProfileListAdapter userProfileListAdapter = new UserProfileListAdapter(mActivity, getProfileValues());
        userProfileListView.setAdapter(userProfileListAdapter);

        userProfileListView.setOnItemClickListener(userProfileSelect);
    }


    private List<String> getProfileValues() {

        List<String> profileValues = new ArrayList<>();

        profileValues.add("User Details");
        profileValues.add("About");

        return profileValues;
    }


    AdapterView.OnItemClickListener userProfileSelect = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            switch (position) {


                case 0:
                    intent = new Intent(mActivity, UserDetailsActivity.class);
                    startActivity(intent);
                    break;

                case 1:
                    intent = new Intent(mActivity, AboutActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    };


    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId())
        {

        }
    }
}
