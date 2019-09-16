package com.bbi.pesquisa.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.services.SaveDataService;

public class UIManager {

    private Activity activity;
    private Context context;
    private View contextView;
    private LinearLayout modal;

    public UIManager(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.contextView = activity.findViewById(R.id.mainActivity);
        this.modal = activity.findViewById(R.id.modal);
    }


    public void showProgressBar(FrameLayout layout, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
    }

    public void hideProgressBar(FrameLayout layout, ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    public void showProgressBar(LinearLayout layout, ProgressBar progressBar) {
        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(LinearLayout layout, ProgressBar progressBar) {
        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void showFocusOn( EditText editText ) {
        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(activity.getApplicationContext().INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(editText, 0);

        if ( editText != null ) {
            editText.requestFocus();
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void hideKeyboard( View view ){
        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void saveData(Answer answer) {

        LinearLayout layout = activity.findViewById(R.id.layout);
        ProgressBar progressBar = activity.findViewById(R.id.progressBar);
        showProgressBar(layout, progressBar);

        SaveDataService service = new SaveDataService();
        service.start(activity.getApplicationContext(), answer);
    }


    public void showModal(LinearLayout modalElement) {
        hideModalElements(activity);
        modal.setVisibility(View.VISIBLE);
        modalElement.setVisibility(View.VISIBLE);
    }

    public void showModal(LinearLayout modalElement, EditText editText) {

        hideModalElements(activity);
        modal.setVisibility(View.VISIBLE);
        modalElement.setVisibility(View.VISIBLE);

        showFocusOn(editText);
    }

    public void showAlert(LinearLayout modalElement, String message, boolean changeButtons) {
        hideModalElements(activity);

        Button noButton = activity.findViewById(R.id.noButton);
        Button yesButton = activity.findViewById(R.id.yesButton);
        TextView alertMessage = activity.findViewById(R.id.alertMessage);

        if (changeButtons) {
            yesButton.setText("OK");
            noButton.setVisibility(View.GONE);
        } else {
            yesButton.setText("SIM");
            noButton.setVisibility(View.VISIBLE);
        }

        modal.setVisibility(View.VISIBLE);
        modalElement.setVisibility(View.VISIBLE);
        alertMessage.setText(message);
    }

    public void hideModal() {
        modal.setVisibility(View.GONE);
        hideModalElements(activity);
    }

    public void hideModal(EditText editText) {
        modal.setVisibility(View.GONE);
        hideModalElements(activity);

        showFocusOn(editText);
    }

    public void hideModal(View view) {
        modal.setVisibility(View.GONE);
        hideModalElements(activity);

        hideKeyboard(view);
    }

    private void hideModalElements(Activity activity) {
        LinearLayout authForm   = activity.findViewById(R.id.authForm);
        LinearLayout orderForm  = activity.findViewById(R.id.orderForm);
        LinearLayout configForm = activity.findViewById(R.id.configForm);
        LinearLayout alertLayout = activity.findViewById(R.id.alertLayout);
        LinearLayout birthdayPicker = activity.findViewById(R.id.birthdayPicker);

        authForm.setVisibility(View.GONE);
        orderForm.setVisibility(View.GONE);
        configForm.setVisibility(View.GONE);
        alertLayout.setVisibility(View.GONE);
        birthdayPicker.setVisibility(View.GONE);
    }

    public void toast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
