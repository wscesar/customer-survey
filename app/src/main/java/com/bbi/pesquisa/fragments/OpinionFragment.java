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
    private UIManager uiManager;
    private EditText customerOpinionInput;

    public OpinionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_curstomer_opinion, container, false);

        uiManager = new UIManager(getActivity());

        TextView headerTitle = getActivity().findViewById(R.id.title);
        headerTitle.setText("Você gostaria de deixar algum comentário?");

        customerOpinionInput = fragmentView.findViewById(R.id.customerOpinionInput);

        uiManager.showFocusOn(customerOpinionInput);

        Button noButton = fragmentView.findViewById(R.id.noButton);
        Button yesButton = fragmentView.findViewById(R.id.yesButton);

        yesButton.setText("Enviar");
        noButton.setText("Pular");

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCustomerOpinion(view);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCustomerOpinion(view);
            }
        });

        return fragmentView;
    }

    private void saveCustomerOpinion(View view) {
        uiManager.hideKeyboard(view);

        String customerOpinion = customerOpinionInput.getText().toString().trim();

        Bundle bundle = getArguments();
        Answer answer = (Answer) bundle.getSerializable("answer");
        answer.setCustomerOpinion(customerOpinion);

        Fragment fragment = new ToastFragment();
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
