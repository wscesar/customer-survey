package com.bbi.pesquisa.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbi.pesquisa.services.SaveDataService;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.util.Mask;
import com.embarcadero.javaandroid.DAOComponent;
import com.embarcadero.javaandroid.SqlCommandBuilder;
import com.embarcadero.javaandroid.TDBXReader;

public class PersonalDataFragment extends Fragment {

    private View fragmentView;
    private Answer answer;
    private TextView inputDay, inputMonth;
    private EditText inputName, inputEmail, inputCity, inputPhone;
    private InputMethodManager inputManager;
    private String name, phone, email, city, day, month, birthday;
    private LinearLayout modal, birthdayPicker;

    private NumberPicker dayPicker, monthPicker;

    public PersonalDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        replaceValue(inputCity, answer.getCity());
        replaceValue(inputEmail, answer.getEmail());
        replaceValue(inputPhone, answer.getPhone());
        replaceValue(inputName, answer.getName());

//        LocalBroadcastManager
//                .getInstance(getActivity().getApplicationContext())
//                .registerReceiver(paramReceiver, new IntentFilter("ParamService"));
    }

    private BroadcastReceiver paramReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String param  = intent.getStringExtra("param");
//            if ( !param.isEmpty() )
//                validateFields();
//            else
//                saveData();

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        modal = getActivity().findViewById(R.id.modal);
        birthdayPicker = getActivity().findViewById(R.id.birthdayPicker);

        fragmentView = inflater.inflate(R.layout.fragment_personal_data, container, false);
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        Bundle bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");

        TextView headerTitle = getActivity().findViewById(R.id.headerTitle);
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
        dayPicker.setWrapSelectorWheel(false);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setWrapSelectorWheel(false);

        inputPhone.addTextChangedListener(Mask.insert(Mask.PHONE_MASK, inputPhone));

        inputDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBirthdayPicker();
            }
        });

        inputMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBirthdayPicker();
            }
        });

        Button confirmBirthdayButton = getActivity().findViewById(R.id.confirmBirthday);
        confirmBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = dayPicker.getValue();
                int month = monthPicker.getValue();

                inputDay.setText(String.valueOf(day));
                inputMonth.setText(String.valueOf(month));

                modal.setVisibility(View.GONE);
                birthdayPicker.setVisibility(View.GONE);
            }
        });

        Button yesButton = getActivity().findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAlert();
            }
        });

        Button noButton = getActivity().findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAlert();
                saveData();
            }
        });

        final Button btn = fragmentView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                btn.setVisibility(View.GONE);
//                showProgressBar();
                inputManager.hideSoftInputFromWindow(fragmentView.getWindowToken(), 0);
                createAnswer();
            }
        });

        return fragmentView;
    }

    private void showBirthdayPicker() {
        modal.setVisibility(View.VISIBLE);
        birthdayPicker.setVisibility(View.VISIBLE);
    }

    private void replaceValue(EditText editText, String value) {
        if ( value != null ) {
            if ( value.isEmpty() )
                showFocusOn(editText);
            else
                editText.setText( value );
        }
    }

    private void showFocusOn( EditText editText ) {
        inputManager.showSoftInput(editText, 0);
        editText.requestFocus();
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void showProgressBar()
    {
        ProgressBar progressBar =  getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ScrollView layout =  getActivity().findViewById(R.id.scrollView);
        layout.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBar()
    {
        ProgressBar progressBar =  getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        ScrollView layout =  getActivity().findViewById(R.id.scrollView);
        layout.setVisibility(View.VISIBLE);
    }

    private void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void createAnswer()
    {
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

        new IsParamToastActive().execute();

//        ParamService service = new ParamService();
//        service.start(getActivity().getApplicationContext());


    }

    private class IsParamToastActive extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            SqlCommandBuilder command =
                    SqlCommandBuilder
                            .create("SELECT VALOR FROM TBPARAMETRO WHERE PARAMETRO = 'PRODUTO_BRINDE_PESQUISA'");

            String param = "";
            try
            {
                TDBXReader reader = new DAOComponent().getMethod().ExecuteQuery( command.toJson() );

                while ( reader.next() ) {
                    param = reader.getValue("VALOR").GetAsString();
                }

            } catch (Exception e) {
                Log.e("teste", e.getMessage());
            }

            return param;
        }

        @Override
        protected void onPostExecute(String param) {

            if ( !param.isEmpty() )
                validateFields();
            else
                saveData();

        }
    }

    private void validateFields() {

        if( name.length() > 0 && name.length() < 3 ){
//            hideProgressBar();
            toast("Informe um nome valido");
        }


        else if( phone.length() > 0 && phone.length() < 15 ) {
//            hideProgressBar();
            toast("Informe um telefone valido");
        }

        else if( email.length() > 0 && validateEmail(email)){
//            hideProgressBar();
            toast("Informe um email valido");
        }

        else if( city.length() > 0 && city.length() < 3 ){
//            hideProgressBar();
            toast("Informe uma cidade valida");
        }

        else if( name.isEmpty() || phone.isEmpty() || email.isEmpty() || city.isEmpty() || day.isEmpty() || month.isEmpty())
            showAlert();////setBundle();//

        else
            saveData();

    }

    private boolean validateEmail(String email) {
        String[] arr = email.split("@");
        String[] domain;

        if( arr.length > 1 && arr[0].length() >= 3 ) {

            domain = arr[1].split(".");

            if(domain.length > 1)
                return true;

        }

        return false;

    }

    private void setBundle() {
//        showProgressBar();
        Fragment fragment = new ToastFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("alert", 1);
        bundle.putSerializable("answer", answer);
        fragment.setArguments(bundle);

        getFragment(fragment);
    }

    private void getFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout,  fragment)
                .commit();
    }

    private void saveData() {
        showProgressBar();
        SaveDataService service = new SaveDataService();
        service.start(getActivity().getApplicationContext(), answer);
    }

    private void showAlert() {
        modal.setVisibility(View.VISIBLE);

        LinearLayout layout = getActivity().findViewById(R.id.alert);
        layout.setVisibility(View.VISIBLE);
//
        TextView message  = getActivity().findViewById(R.id.message);
        message.setText(R.string.alert);
    }

    private void hideAlert() {
//        if(inputName.getText().equals(""))
//            showFocusOn(inputName);
//
//        else if(inputPhone.getText().equals(""))
//            showFocusOn(inputPhone);
//
//        else if(inputEmail.getText().equals(""))
//            showFocusOn(inputEmail);
//
//        else if(inputCity.getText().equals(""))
//            showFocusOn(inputCity);

        LinearLayout layout = getActivity().findViewById(R.id.alert);
        layout.setVisibility(View.GONE);

        hideProgressBar();
        modal.setVisibility(View.GONE);
    }



}
