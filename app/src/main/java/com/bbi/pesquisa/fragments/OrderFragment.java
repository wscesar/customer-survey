package com.bbi.pesquisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;

public class OrderFragment extends Fragment {
    private Bundle bundle = new Bundle();
    private Answer answer = new Answer();
    private EditText inputOrderId;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.form_order, container, false);

        LinearLayout orderForm = fragmentView.findViewById(R.id.orderForm);
        orderForm.setVisibility(View.VISIBLE);

        inputOrderId = fragmentView.findViewById(R.id.orderId);

//        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

        Button saveOrderId = fragmentView.findViewById(R.id.saveOrderId);
        saveOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrderId();
            }
        });

        return fragmentView;
    }

    private void saveOrderId() {
        String orderId = inputOrderId.getText().toString().trim();

        if ( orderId.equals("") )
            orderId = "0";

        answer.setOrderId(orderId);

        setBundle(new PromptFragment());
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

}
