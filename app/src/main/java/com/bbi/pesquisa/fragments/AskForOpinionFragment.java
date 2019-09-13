package com.bbi.pesquisa.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.util.UIManager;


public class AskForOpinionFragment extends Fragment {

    private Answer answer;
    private Bundle bundle;

    private LinearLayout layout;
    private ProgressBar progressBar;

    public AskForOpinionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.message, container, false);

        TextView headerTitle = getActivity().findViewById(R.id.title);
        headerTitle.setText(R.string.app_name);

        TextView message = fragmentView.findViewById(R.id.message);
        message.setText(R.string.acceptSurvey);

        bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");

        layout      = fragmentView.findViewById(R.id.layout);
        progressBar = fragmentView.findViewById(R.id.progressBar);

        Button yesButton = fragmentView.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(new OpinionFragment());
            }
        });

        Button noButton = fragmentView.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(new ToastFragment());
            }
        });

        return fragmentView;
    }


    private void getFragment(Fragment fragment) {

        new UIManager().showProgressBar(layout, progressBar);

        fragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

}
