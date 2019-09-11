package com.bbi.pesquisa.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbi.pesquisa.R;

public class LastFragment extends Fragment {

    public LastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_message, container, false);

        ProgressBar progressBar = fragmentView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        TextView title = getActivity().findViewById(R.id.title);
        TextView message = fragmentView.findViewById(R.id.message);
        LinearLayout yesNoButtons = fragmentView.findViewById(R.id.yesNoButtons);
        Button finishButton = fragmentView.findViewById(R.id.finishButton);

        title.setText(R.string.app_name);

        message.setText("Obrigado");

        yesNoButtons.setVisibility(View.GONE);

        finishButton.setVisibility(View.VISIBLE);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .replace( R.id.frameLayout, new PromptFragment() )
                        .commit();
            }
        });

        return fragmentView;
    }

}
