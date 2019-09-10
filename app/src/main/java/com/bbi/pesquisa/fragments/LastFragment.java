package com.bbi.pesquisa.fragments;



import android.os.Bundle;

import androidx.fragment.app.Fragment;

//import android.app.Fragment;
//import android.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bbi.pesquisa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastFragment extends Fragment {

    public LastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_last, container, false);

        TextView headerTitle = getActivity().findViewById(R.id.headerTitle);
        headerTitle.setText(R.string.app_name);

        Button button = v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
//                System.exit(0);
                getFragmentManager()
                        .beginTransaction()
                        .replace( R.id.frameLayout, new PromptFragment() )
                        .commit();
            }
        });

        return v;
    }

}
