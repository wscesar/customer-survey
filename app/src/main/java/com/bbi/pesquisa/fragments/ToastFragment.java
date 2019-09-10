package com.bbi.pesquisa.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbi.pesquisa.services.ParamService;
import com.bbi.pesquisa.services.SaveDataService;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;

public class ToastFragment extends Fragment {
    private View fragmentView;
    private TextView message;

    private Answer answer;
    private Bundle bundle;

    private LinearLayout layout;
    private ProgressBar progressBar;

    public ToastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        LocalBroadcastManager
                .getInstance(getActivity().getApplicationContext())
                .registerReceiver(paramReceiver, new IntentFilter("ParamService"));
    }

    private BroadcastReceiver paramReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String param  = intent.getStringExtra("param");

            if ( param != null && !param.isEmpty() ) {
                message = fragmentView.findViewById(R.id.message);

                int alert = bundle.getInt("alert", 0);

                if ( alert > 0 )
                    message.setText(R.string.alert);
                else
                    message.setText(R.string.hasToast);

            } else {
                message.setText(R.string.noToast);
            }

            hideProgressBar();

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_prompt, container, false);
        message = fragmentView.findViewById(R.id.message);

        ParamService service = new ParamService();
        service.start(getActivity().getApplicationContext());

        bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");

        layout      = fragmentView.findViewById(R.id.layout);
        progressBar = getActivity().findViewById(R.id.progressBar);

        showProgressBar();

        TextView headerTitle = getActivity().findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.app_name);

        Button yesButton = fragmentView.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForm();
            }
        });

        Button noButton = fragmentView.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        return fragmentView;
    }

    private void goToForm() {
        PersonalDataFragment fragment = new PersonalDataFragment();
        fragment.setArguments(bundle);
        getFragment(fragment);
    }

    private void getFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

    private void saveData() {
        showProgressBar();
        SaveDataService service = new SaveDataService();
        service.start(getActivity().getApplicationContext(), answer);
    }

    private void showProgressBar(){
        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}
