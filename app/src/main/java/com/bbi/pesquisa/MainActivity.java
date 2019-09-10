package com.bbi.pesquisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbi.pesquisa.fragments.LastFragment;
import com.bbi.pesquisa.model.NetworkConfiguration;
import com.bbi.pesquisa.fragments.FirstFragment;
import com.bbi.pesquisa.services.GetLogoService;
import com.bbi.pesquisa.util.NetworkManager;


public class MainActivity extends AppCompatActivity {
    boolean isLongPress = false;

    private TextView message;
    private LinearLayout layout;
    private ProgressBar progressBar;

    private NetworkManager networkManager;

    private InputMethodManager inputManager;

    private ImageView logo;

    private EditText inputOrderId, inputIp, inputPort, authPassword ;

    private LinearLayout modal, authForm, configForm, orderForm;

    private NetworkConfiguration network;

    private BroadcastReceiver idReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int surveyId  = intent.getIntExtra("surveyId", 0);

            if ( surveyId > 0 ) {
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
                getFragment(new LastFragment());
            }
        }
    };


    private BroadcastReceiver bitmapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bitmap bitmap = intent.getParcelableExtra("bitmap");

            if ( bitmap != null )
                logo.setImageBitmap(bitmap);

        }
    };

    @Override
    protected void onResume() {

        super.onResume();

        if(network != null){

            LocalBroadcastManager
                    .getInstance(getApplicationContext())
                    .registerReceiver(idReceiver, new IntentFilter("SaveDataService"));

            LocalBroadcastManager
                    .getInstance(getApplicationContext())
                    .registerReceiver(bitmapReceiver, new IntentFilter("GetLogoService"));
        }


    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
    long then = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        layout      = findViewById(R.id.layout);
        message     = findViewById(R.id.message);
        progressBar = findViewById(R.id.progressBar);

        // init global vars
        logo = findViewById(R.id.headerLogo);

        modal = findViewById(R.id.modal);
        authForm =  findViewById(R.id.authForm);
        orderForm =  findViewById(R.id.orderForm);
        configForm = findViewById(R.id.configForm);
        inputOrderId = findViewById(R.id.orderId);



        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        networkManager = new NetworkManager(this);
        network = networkManager.getConfiguration();

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //ConnectivityManager.NetworkCallback w =  new ConnectivityManager.NetworkCallback();


        String networkSSID = "test";
        String networkPass = "pass";

//        WifiConfiguration conf = new WifiConfiguration();
//        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
//
//
//        if(wifi.isConnected()){
//            TextView title = findViewById(R.id.headerTitle);
//            title.setText("Wifi desconectado");
//        }

        if ( network != null ) {
            if ( network.getId() == 1 ) {
                GetLogoService service = new GetLogoService();
                service.start(getApplicationContext());
            }
            else {
                showModal(configForm);
            }

            modal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideModal(view.getRootView());
                }
            });


//            logo.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    showModal(orderForm);
//                    showFocusOn(inputOrderId);
//                    return false;
//                }
//            });



            logo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        isLongPress = true;
                        new Handler().postDelayed(new Runnable()
                        {

                            @Override
                            public void run() {
                                if (isLongPress) {
                                    showModal(orderForm);
                                    showFocusOn(inputOrderId);
                                }
                            }

                        }, 5000);
                    }
                    else if ( event.getAction() == MotionEvent.ACTION_UP )
                    {
                        isLongPress = false;
                    }

                    return true;
                }
            });



            LinearLayout logo_bbi = findViewById(R.id.footer);
            logo_bbi.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    authPassword = findViewById(R.id.authPassword);
                    displayNetworkConfig();
                    showModal(authForm);
                    showFocusOn(authPassword);
                    return false;
                }
            });


            Button configNetwork = findViewById(R.id.networkConfigButton);
            configNetwork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveNetwork();
                    hideModal(view);
                }
            });

            Button authButton = findViewById(R.id.authButton);
            authButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText authPassword = findViewById(R.id.authPassword);
                    showFocusOn(authPassword);

                    if(authPassword.getText().toString().trim().equals("bbifood")) {
                        showModal(configForm);
                    } else {
                        authPassword.setText("");
                        hideModal(view);
                    }
                }
            });

            getFragment( new FirstFragment() );
        }
    }

    private void displayNetworkConfig() {
        NetworkConfiguration config = networkManager.getConfiguration();

        inputIp =  findViewById(R.id.ip);
        inputPort =  findViewById(R.id.port);

        inputIp.setText( config.getIp() );
        inputPort.setText( config.getPort()+"" );
    }

    private void showFocusOn(EditText inputField) {
        inputManager.showSoftInput(inputField, 0);
        inputField.requestFocus();
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void saveNetwork() {
        EditText inputIp =  findViewById(R.id.ip);
        EditText inputPort =  findViewById(R.id.port);

        String ip = inputIp.getText().toString().trim();
        int port = Integer.parseInt( inputPort.getText().toString().trim() );

        //network = networkManager.getConfiguration();

        if ( network != null) {
            if ( network.getId() == 1 )
                networkManager.updateConfiguration(ip, port);
            else
                networkManager.insertConfiguration(ip, port);
        }
    }

    private void showModal(LinearLayout form) {
        hideForms();
        modal.setVisibility(View.VISIBLE);
        form.setVisibility(View.VISIBLE);
    }

    private void hideModal(View view) {
        modal.setVisibility(View.GONE);
        hideForms();
        hideKeyboard(view);
    }

    private void hideForms() {
        authForm.setVisibility(View.GONE);
        orderForm.setVisibility(View.GONE);
        configForm.setVisibility(View.GONE);
    }



    private void hideKeyboard(View view) {
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .add( R.id.frameLayout, fragment )
                .commit();

    }

    private void hideProgressBar() {
        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }




} //end Activity
