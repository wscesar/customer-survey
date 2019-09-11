package com.bbi.pesquisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.util.InterfaceManager;

public class CustomerOpinionFragment extends Fragment {
    private InterfaceManager interfaceManager = new InterfaceManager();
    private EditText customerOpinionInput;
    private View fragmentView;

    public CustomerOpinionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_curstomer_opinion, container, false);

        TextView headerTitle = getActivity().findViewById(R.id.title);
        headerTitle.setText(R.string.app_name);

        customerOpinionInput = fragmentView.findViewById(R.id.customerOpinionInput);

        interfaceManager.showFocusOn(getActivity(), getContext(), customerOpinionInput);

        Button saveCustomerOpinionButton = fragmentView.findViewById(R.id.saveCustomerOpinionButton);
        saveCustomerOpinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                interfaceManager.hideKeyboard(getActivity(), getContext(), view);

                String customerOpinion = customerOpinionInput.getText().toString().trim();

                Bundle bundle = getArguments();
                Answer answer = (Answer) bundle.getSerializable("answer");
                answer.setCustomerOpinion(customerOpinion);

                Fragment fragment = new ToastFragment();
                bundle.putSerializable("answer", answer);
                fragment.setArguments(bundle);

                LinearLayout layout= fragmentView.findViewById(R.id.customerOpinionLayout);
                ProgressBar progressBar = fragmentView.findViewById(R.id.progressBar);
                interfaceManager.showProgressBar(layout, progressBar);

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
