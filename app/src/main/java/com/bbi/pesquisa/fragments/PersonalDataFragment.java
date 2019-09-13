package com.bbi.pesquisa.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.model.ValidatedForm;
import com.bbi.pesquisa.util.FormValidator;
import com.bbi.pesquisa.util.UIManager;
import com.bbi.pesquisa.util.Mask;

public class PersonalDataFragment extends Fragment {

    private int hasToast;
    private Answer answer;
    private TextView inputDay, inputMonth;
    private LinearLayout birthdayPicker, alertLayout;
    private String name, phone, email, city, day, month, birthday;
    private Button yesButton, noButton, saveButton, confirmBirthdayButton;
    private EditText inputName, inputEmail, inputCity, inputPhone, setFocusOn;

    private UIManager uiManager;

    private NumberPicker dayPicker, monthPicker;

    public PersonalDataFragment() {
        // Required empty public constructor
    }

    private View fragmentView;
    private Activity activity;

    private Bundle bundle;

    private TextView headerTitle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_personal_data, container, false);

        initGlobalVars();

        uiManager = new UIManager(getActivity());

        bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");
        hasToast =  bundle.getInt("hasToast", 0);

        headerTitle.setText("Dados para cadastro");

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        inputPhone.addTextChangedListener(Mask.insert(Mask.PHONE_MASK, inputPhone));

        inputDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiManager.showModal(birthdayPicker);
            }
        });

        inputMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiManager.showModal(birthdayPicker);
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( setFocusOn != null )
                    uiManager.hideModal(setFocusOn);
                else
                    uiManager.showModal(birthdayPicker);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        confirmBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmBirthday();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uiManager.hideKeyboard(view);
                getAnswers();
            }
        });

        return fragmentView;
    }

    private void initGlobalVars() {
        activity = getActivity();

        headerTitle = activity.findViewById(R.id.title);

        alertLayout    = activity.findViewById(R.id.alertLayout);
        dayPicker      = activity.findViewById(R.id.np1);
        monthPicker    = activity.findViewById(R.id.np2);
        birthdayPicker = activity.findViewById(R.id.birthdayPicker);

        yesButton  = activity.findViewById(R.id.yesButton);
        noButton   = activity.findViewById(R.id.noButton);
        saveButton = fragmentView.findViewById(R.id.savePersonalData);
        confirmBirthdayButton = activity.findViewById(R.id.confirmBirthday);

        inputName  = fragmentView.findViewById(R.id.inputName);
        inputPhone = fragmentView.findViewById(R.id.inputPhone);
        inputEmail = fragmentView.findViewById(R.id.inputEmail);
        inputCity  = fragmentView.findViewById(R.id.inputCity);
        inputMonth = fragmentView.findViewById(R.id.inputMonth);
        inputDay   = fragmentView.findViewById(R.id.inputDay);
    }

    private void confirmBirthday() {
        int day = dayPicker.getValue();
        int month = monthPicker.getValue();

        inputDay.setText(String.valueOf(day));
        inputMonth.setText(String.valueOf(month));

        uiManager.hideModal();
    }

    private void getAnswers(){
        name = inputName.getText().toString().trim();
        phone = inputPhone.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        city = inputCity.getText().toString().trim();
        day = inputDay.getText().toString();
        month = inputMonth.getText().toString();
        birthday = day + "/" + month;

        createAnswerObject();

    }

    private void createAnswerObject() {
        answer.setName(name);
        answer.setPhone(phone);
        answer.setEmail(email);
        answer.setCity(city);
        answer.setBirthday(birthday);

        saveAnswer();
    }

    private void saveAnswer() {
        FormValidator formValidator = new FormValidator();

        ValidatedForm validatedForm =
                formValidator.validateFields(fragmentView, answer, hasToast);

        String message = validatedForm.getMessage();
        boolean changeButtons = validatedForm.isChangeButtons();
        boolean readyToSave = validatedForm.isReadyToSave();
        setFocusOn = validatedForm.getSetFocusOn();

        if ( readyToSave ) {
            saveData();
        }

        else {
            uiManager.showAlert(alertLayout, message, changeButtons);
        }
    }

    private void saveData() {
        LinearLayout layout = fragmentView.findViewById(R.id.layout);
        ProgressBar progressBar = fragmentView.findViewById(R.id.progressBar);

        uiManager.showProgressBar(layout, progressBar);
        uiManager.hideModal();
        uiManager.saveData(answer);
    }

}
