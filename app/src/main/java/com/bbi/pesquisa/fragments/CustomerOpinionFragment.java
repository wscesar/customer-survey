package com.bbi.pesquisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;

public class CustomerOpinionFragment extends Fragment {
    private EditText customerOpinionInput;

    private InputMethodManager inputManager;

    public CustomerOpinionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View fragmentView = inflater.inflate(R.layout.fragment_curstomer_opinion, container, false);

        inputManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        customerOpinionInput = fragmentView.findViewById(R.id.customerOpinionInput);

        showFocusOn(customerOpinionInput);

        Button saveCustomerOpinionButton = fragmentView.findViewById(R.id.saveCustomerOpinionButton);
        saveCustomerOpinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String customerOpinion = customerOpinionInput.getText().toString().trim();

                Bundle bundle = getArguments();
                Answer answer = (Answer) bundle.getSerializable("answer");
                answer.setCustomerOpinion(customerOpinion);

//                Fragment fragment = new SurveyFragment();
                Fragment fragment = new ToastFragment();
                bundle.putSerializable("answer", answer);
                fragment.setArguments(bundle);

                getFragment(fragment);

            }
        });

        return fragmentView;
    }

    private void showFocusOn( EditText editText ) {
        inputManager.showSoftInput(editText, 0);
        editText.requestFocus();
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void getFragment(Fragment fragment) {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();

    }
}
