package com.bbi.pesquisa.util;

import android.view.View;
import android.widget.EditText;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.model.ValidatedForm;

public class FormValidator {

    public ValidatedForm validateFields(View v, Answer answer, int hasToast) {
        String message = "";
        EditText setFocusOn = null;
        boolean readyToSave = false;
        boolean changeButtons = false;

        EditText inputName  = v.findViewById(R.id.inputName);
        EditText inputPhone = v.findViewById(R.id.inputPhone);
        EditText inputEmail = v.findViewById(R.id.inputEmail);
        EditText inputCity  = v.findViewById(R.id.inputCity);

        String name = answer.getName();
        String phone = answer.getPhone();
        String email = answer.getEmail();
        String city = answer.getCity();
        String birthday = answer.getBirthday();


        if( name.length() > 0 && name.length() < 3 ) {
            setFocusOn = inputName;
            message = "Informe um nome válido";
            changeButtons = true;
        }

        else if( phone.length() > 0 && phone.length() < 15 ) {
            setFocusOn = inputPhone;
            message = "Informe um telefone válido";
            changeButtons = true;
        }

        else if( email.length() > 0 && !validateEmail(email)) {
            setFocusOn = inputEmail;
            message = "Informe um email válido";
            changeButtons = true;
        }

        else if( city.length() > 0 && city.length() < 3 ) {
            setFocusOn = inputCity;
            message = "Informe uma cidade válida";
            changeButtons = true;
        }

        else if( hasToast > 0 && name.isEmpty() || phone.isEmpty() || email.isEmpty() || city.isEmpty() ) {
            message = "Pra receber seu brinde é necessario preencher todos os campos"
                    + "\nGostaria de terminar seu cadastro?";

            if ( name.isEmpty() )
                setFocusOn = inputName;

            else if ( phone.isEmpty() )
                setFocusOn = inputPhone;

            else if ( email.isEmpty() )
                setFocusOn = inputEmail;

            else if ( city.isEmpty() )
                setFocusOn = inputCity;

        }

        else if( hasToast > 0 && birthday.equals("/") ) {
            message = "Informe sua data de aniversário";
            changeButtons = true;
        }

        else {
            readyToSave = true;
        }

        ValidatedForm validatedForm = new ValidatedForm();
        validatedForm.setMessage(message);
        validatedForm.setChangeButtons(changeButtons);
        validatedForm.setSetFocusOn(setFocusOn);
        validatedForm.setReadyToSave(readyToSave);

        return validatedForm;

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
}
