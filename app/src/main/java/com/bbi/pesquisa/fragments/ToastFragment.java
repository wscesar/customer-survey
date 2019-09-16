package com.bbi.pesquisa.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbi.pesquisa.services.ParamService;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.util.UIManager;

public class ToastFragment extends Fragment {
    private UIManager uiManager;
    private View fragmentView;

    private Answer answer;
    private Bundle bundle;

    private LinearLayout layout;
    private ProgressBar progressBar;

    public ToastFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver paramReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String param  = intent.getStringExtra("param");

            TextView message = fragmentView.findViewById(R.id.message);

            if ( param != null && !param.isEmpty() ){
                message.setText(R.string.hasToast);
                bundle.putInt("hasToast", 1);
            }
            else {
                message.setText(R.string.noToast);
                bundle.putInt("hasToast", 0);
            }

            uiManager.hideProgressBar(layout, progressBar);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ParamService service = new ParamService();
        service.start(getActivity().getApplicationContext());

        LocalBroadcastManager
                .getInstance(getActivity().getApplicationContext())
                .registerReceiver(paramReceiver, new IntentFilter("ParamService"));

        fragmentView = inflater.inflate(R.layout.message, container, false);

        uiManager = new UIManager(getActivity());

        bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");

        layout      = fragmentView.findViewById(R.id.layout);
        progressBar = fragmentView.findViewById(R.id.progressBar);

        uiManager.showProgressBar(layout, progressBar);

        TextView headerTitle = getActivity().findViewById(R.id.title);
        headerTitle.setText(R.string.app_name);

        Button yesButton = fragmentView.findViewById(R.id.yesButton);
        Button noButton = fragmentView.findViewById(R.id.noButton);

        yesButton.setText("SIM");
        noButton.setText("NÂO");

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForm();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiManager.showProgressBar(layout, progressBar);
                uiManager.saveData(answer);
            }
        });

        return fragmentView;
    }

    private void goToForm() {
        uiManager.showProgressBar(layout, progressBar);
        PersonalDataFragment fragment = new PersonalDataFragment();
        fragment.setArguments(bundle);
        getFragment(fragment);
    }

    private void getFragment(Fragment fragment) {
        uiManager.showProgressBar(layout, progressBar);
        getFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

}
