package com.bbi.pesquisa.fragments;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import android.app.Fragment;
//import android.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.NetworkConfiguration;

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
                        .replace( R.id.frameLayout, new FirstFragment() )
                        .commit();
            }
        });

        return v;
    }

}
