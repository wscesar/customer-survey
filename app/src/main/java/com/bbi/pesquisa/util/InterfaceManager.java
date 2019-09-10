package com.bbi.pesquisa.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
//
//import android.app.Fragment;
//import android.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;

import com.bbi.pesquisa.MainActivity;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.fragments.*;
import com.bbi.pesquisa.model.Answer;

public class InterfaceManager extends AppCompatActivity {

    public void getFragment(int layout, Fragment fragment, Answer answer, Bundle bundle) {

        bundle.putSerializable("answer", answer);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add( layout, fragment )
                .commit();

        Log.d("testete","it works!!! moder foca");

    }

//    public void replaceFragment(Activity activity, Fragment fragment) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add( activity.findViewById(R.id.frameLayout), fragment )
//                .commit();
//
//        Log.d("testete","it works!!! moder foca");
//
//    }
}
