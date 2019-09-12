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
import android.widget.TextView;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.model.ValidatedForm;
import com.bbi.pesquisa.util.FormValidator;
import com.bbi.pesquisa.util.UIManager;
import com.bbi.pesquisa.util.Mask;

public class PersonalDataFragment extends Fragment {

    private Answer answer;
    private TextView inputDay, inputMonth;
    private EditText inputName, inputEmail, inputCity, inputPhone, setFocusOn;
    private String name, phone, email, city, day, month, birthday;
    private LinearLayout birthdayPicker, alertLayout;
    private int hasToast;

    private UIManager UIManager = new UIManager();

    private NumberPicker dayPicker, monthPicker;

    public PersonalDataFragment() {
        // Required empty public constructor
    }

    private View fragmentView;
    private Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();

        fragmentView = inflater.inflate(R.layout.fragment_personal_data, container, false);

        birthdayPicker = activity.findViewById(R.id.birthdayPicker);
        alertLayout = activity.findViewById(R.id.alertLayout);

        Bundle bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");
        hasToast =  bundle.getInt("hasToast", 0);

        TextView headerTitle = activity.findViewById(R.id.title);
        headerTitle.setText("Dados para cadastro");

        inputName  = fragmentView.findViewById(R.id.inputName);
        inputPhone = fragmentView.findViewById(R.id.inputPhone);
        inputEmail = fragmentView.findViewById(R.id.inputEmail);
        inputCity  = fragmentView.findViewById(R.id.inputCity);
        inputMonth = fragmentView.findViewById(R.id.inputMonth);
        inputDay   = fragmentView.findViewById(R.id.inputDay);

        dayPicker   = activity.findViewById(R.id.np1);
        monthPicker = activity.findViewById(R.id.np2);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        inputPhone.addTextChangedListener(Mask.insert(Mask.PHONE_MASK, inputPhone));

        inputDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.showModal(activity, birthdayPicker);
            }
        });

        inputMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.showModal(activity, birthdayPicker);
            }
        });

        Button yesButton = activity.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( setFocusOn != null )
                    UIManager.hideModal(activity, setFocusOn);

                else
                    UIManager.showModal(activity, birthdayPicker);

            }
        });

        Button noButton = activity.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.hideModal(activity);
                UIManager.saveData(activity, answer);
            }
        });

        Button confirmBirthdayButton = activity.findViewById(R.id.confirmBirthday);
        confirmBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmBirthday();
            }
        });

        Button saveButton = fragmentView.findViewById(R.id.savePersonalData);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.hideKeyboard(activity, view);
                getAnswers();
            }
        });

        return fragmentView;
    }

    private void confirmBirthday() {
        int day = dayPicker.getValue();
        int month = monthPicker.getValue();

        inputDay.setText(String.valueOf(day));
        inputMonth.setText(String.valueOf(month));

        UIManager.hideModal(activity);
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
            UIManager.saveData(getActivity(), answer);
        }

        else {
            UIManager.showAlert(activity, alertLayout, message, changeButtons);
        }
    }

}
