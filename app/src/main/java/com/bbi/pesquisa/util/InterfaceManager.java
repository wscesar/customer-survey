package com.bbi.pesquisa.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
import android.app.Fragment;


import com.bbi.pesquisa.R;

import com.bbi.pesquisa.services.GetLogoService;

public class InterfaceManager extends AppCompatActivity{

    public void getFragment(Activity activity, Fragment fragment) {
        activity.getFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

    public void getLogo(Context context) {
        GetLogoService service = new GetLogoService();
        service.start(context);
    }

    public void showProgressBar(LinearLayout layout, ProgressBar progressBar) {
        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(LinearLayout layout, ProgressBar progressBar) {
        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void showFocusOn( Activity activity, Context context, EditText editText ) {
        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(editText, 0);

        editText.requestFocus();

        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideKeyboard( Activity activity, Context context, View view ){
        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void alert(Activity activity, String text, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());
        builder.setTitle(title)
                .setMessage(text)
                .setNeutralButton("Fechar", null)
                .show();
    }

}
