package com.bbi.pesquisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.util.UIManager;

public class OpinionFragment extends Fragment {
    private UIManager UIManager = new UIManager();
    private EditText customerOpinionInput;
    private View fragmentView;

    public OpinionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_curstomer_opinion, container, false);

        TextView headerTitle = getActivity().findViewById(R.id.title);
        headerTitle.setText(R.string.app_name);

        customerOpinionInput = fragmentView.findViewById(R.id.customerOpinionInput);

        UIManager.showFocusOn(getActivity(), customerOpinionInput);

        Button saveCustomerOpinionButton = fragmentView.findViewById(R.id.saveCustomerOpinionButton);
        saveCustomerOpinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UIManager.hideKeyboard(getActivity(), getContext(), view);

                String customerOpinion = customerOpinionInput.getText().toString().trim();

                Bundle bundle = getArguments();
                Answer answer = (Answer) bundle.getSerializable("answer");
                answer.setCustomerOpinion(customerOpinion);

                Fragment fragment = new ToastFragment();
                bundle.putSerializable("answer", answer);
                fragment.setArguments(bundle);

                getFragment(fragment);

            }
        });

        return fragmentView;
    }

    private void getFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

}
