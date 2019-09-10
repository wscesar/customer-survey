package com.bbi.pesquisa.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;

public class PromptFragment extends Fragment {
    private Bundle bundle = new Bundle();
    private Answer answer = new Answer();

    private TextView message;

    private LinearLayout modal;
    private LinearLayout orderForm;
    private LinearLayout configForm;

    private EditText inputOrderId;

    private InputMethodManager inputManager;

    public PromptFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_prompt, container, false);

        message = fragmentView.findViewById(R.id.message);
        message.setText(R.string.welcome);

        modal = getActivity().findViewById(R.id.modal);
        orderForm = getActivity().findViewById(R.id.orderForm);
        configForm = getActivity().findViewById(R.id.configForm);

        inputOrderId = getActivity().findViewById(R.id.orderId);

        inputManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

        Button yesButton = fragmentView.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrderId();
            }
        });

        Button noButton = fragmentView.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragment(new LastFragment());
            }
        });

        Button saveOrderId = getActivity().findViewById(R.id.saveOrderId);
        saveOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideModal(view);
            }
        });

        return fragmentView;
    }

    private void saveOrderId() {
        String orderId = inputOrderId.getText().toString().trim();

        if ( orderId.equals("") )
            orderId = "0";

        answer.setOrderId(orderId);

//        setBundle(new CustomerOpinionFragment());
        setBundle(new SurveyFragment());
    }

    private void setBundle(Fragment fragment) {
        bundle.putSerializable("answer", answer);
        fragment.setArguments(bundle);
        getFragment(fragment);
    }

    private void getFragment(Fragment fragment) {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

    private void hideModal(View view) {
        modal.setVisibility(View.GONE);
        orderForm.setVisibility(View.GONE);
        configForm.setVisibility(View.GONE);

        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setMessage(String value) {
        message.setText(value);
    }
}
