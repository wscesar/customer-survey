package com.bbi.pesquisa.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.util.UIManager;

public class FirstFragment extends Fragment {
    private UIManager uiManager;
    private Bundle bundle = new Bundle();
    private Answer answer = new Answer();
    private Button yesButton, noButton, saveOrderId;

    private LinearLayout layout;
    private ProgressBar progressBar;

    private TextView message;

    private View fragmentView;


    private EditText inputOrderId;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.message, container, false);

        initGlobalVars();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOrderId();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setThanksMessage();
            }
        });

        saveOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiManager.hideModal(view);
            }
        });

        return fragmentView;
    }

    private void initGlobalVars() {
        uiManager = new UIManager(getActivity());

        layout = fragmentView.findViewById(R.id.layout);
        progressBar = fragmentView.findViewById(R.id.progressBar);

        message = fragmentView.findViewById(R.id.message);
        message.setText(R.string.welcome);

        yesButton = fragmentView.findViewById(R.id.yesButton);
        noButton  = fragmentView.findViewById(R.id.noButton);
        saveOrderId = getActivity().findViewById(R.id.saveOrderId);

        inputOrderId = getActivity().findViewById(R.id.orderId);
    }

    private void setOrderId() {

        String orderId = inputOrderId.getText().toString().trim();

        if ( orderId.equals("") )
            orderId = "0";

        answer.setOrderId(orderId);

        bundle.putSerializable("answer", answer);

        Fragment fragment = new SurveyFragment();

        fragment.setArguments(bundle);

        getFragment(fragment);
    }

    private void setThanksMessage() {
        bundle.putString("message", "Obrigado");

        Fragment fragment = new LastFragment();
        fragment.setArguments(bundle);
        getFragment(fragment);
    }

    private void getFragment(Fragment fragment) {
//        uiManager.showProgressBar(layout, progressBar);
        inputOrderId.setText("");
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

    public void setMessage(String value) {
        message.setText(value);
    }
}
