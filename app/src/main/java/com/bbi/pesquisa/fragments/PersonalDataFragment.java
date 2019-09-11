package com.bbi.pesquisa.fragments;

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
import com.bbi.pesquisa.util.UIManager;
import com.bbi.pesquisa.util.Mask;

public class PersonalDataFragment extends Fragment {

    private Answer answer;
    private TextView inputDay, inputMonth;
    private EditText inputName, inputEmail, inputCity, inputPhone;
    private String name, phone, email, city, day, month, birthday;
    private LinearLayout birthdayPicker, alertLayout;
    private int hasToast;

    private UIManager UIManager = new UIManager();

    private NumberPicker dayPicker, monthPicker;

    public PersonalDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_personal_data, container, false);

        birthdayPicker = getActivity().findViewById(R.id.birthdayPicker);
        alertLayout = getActivity().findViewById(R.id.alertLayout);

        Bundle bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");
        hasToast =  bundle.getInt("hasToast", 0);

        TextView headerTitle = getActivity().findViewById(R.id.title);
        headerTitle.setText("Dados para cadastro");

        inputName  = fragmentView.findViewById(R.id.inputName);
        inputPhone = fragmentView.findViewById(R.id.inputPhone);
        inputEmail = fragmentView.findViewById(R.id.inputEmail);
        inputCity  = fragmentView.findViewById(R.id.inputCity);
        inputMonth = fragmentView.findViewById(R.id.inputMonth);
        inputDay   = fragmentView.findViewById(R.id.inputDay);

        dayPicker   = getActivity().findViewById(R.id.np1);
        monthPicker = getActivity().findViewById(R.id.np2);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        inputPhone.addTextChangedListener(Mask.insert(Mask.PHONE_MASK, inputPhone));

        inputDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.showModal(getActivity(), birthdayPicker);
            }
        });

        inputMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.showModal(getActivity(), birthdayPicker);
            }
        });



        Button yesButton = getActivity().findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.hideModal(getActivity());
            }
        });

        Button noButton = getActivity().findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.hideModal(getActivity());
                UIManager.saveData(getActivity(), answer);
            }
        });

        Button confirmBirthdayButton = getActivity().findViewById(R.id.confirmBirthday);
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
                UIManager.hideKeyboard(getActivity(), getContext(), view);
                createAnswerObject();
            }
        });

        return fragmentView;
    }

    private void confirmBirthday() {
        int day = dayPicker.getValue();
        int month = monthPicker.getValue();

        inputDay.setText(String.valueOf(day));
        inputMonth.setText(String.valueOf(month));

        UIManager.hideModal(getActivity());
    }

    private void validateFields() {

        if( name.length() > 0 && name.length() < 3 )
        {
            String message = "Informe um nome válido";
            UIManager.showAlert(getActivity(), alertLayout, message, true);
            UIManager.showFocusOn(getActivity(), inputName);
        }
        else if( phone.length() > 0 && phone.length() < 15 )
        {
            String message = "Informe um telefone válido";
            UIManager.showAlert(getActivity(), alertLayout, message, true);
            UIManager.showFocusOn(getActivity(), inputPhone);
        }
        else if( email.length() > 0 && !validateEmail(email))
        {
            String message = "Informe um email válido";
            UIManager.showAlert(getActivity(), alertLayout, message, true);
            UIManager.showFocusOn(getActivity(), inputEmail);
        }
        else if( city.length() > 0 && city.length() < 3 )
        {
            String message = "Informe uma cidade válida";
            UIManager.showAlert(getActivity(), alertLayout, message, true);
            UIManager.showFocusOn(getActivity(), inputCity);
        }
        else if( hasToast > 0
                 && name.isEmpty()
                 || phone.isEmpty()
                 || email.isEmpty()
                 || city.isEmpty()
                 || day.isEmpty()
                 || month.isEmpty() )
        {
            String message = "Pra receber seu brinde é necessario preencher todos os campos\nGostaria de terminar seu cadastro?";
            UIManager.showAlert(getActivity(), alertLayout, message, false);
        }
        else
        {
            UIManager.saveData(getActivity(), answer);
        }

    }

    private boolean validateEmail(String email) {
        String[] arr = email.split("@");
        String[] domain;

        if( arr.length > 1 && arr[0].length() >= 3 ) {

            domain = arr[1].split("\\.");

            if( domain.length > 1 )
                return true;

        }

        return false;

    }

    private void createAnswerObject() {
        name = inputName.getText().toString().trim();
        phone = inputPhone.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        city = inputCity.getText().toString().trim();
        day = inputDay.getText().toString();
        month = inputMonth.getText().toString();
        birthday = day + "/" + month;

        answer.setName(name);
        answer.setPhone(phone);
        answer.setEmail(email);
        answer.setCity(city);
        answer.setBirthday(birthday);

        validateFields();

    }

}
