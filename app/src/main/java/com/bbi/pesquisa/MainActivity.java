package com.bbi.pesquisa;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bbi.pesquisa.fragments.LastFragment;
import com.bbi.pesquisa.fragments.PromptFragment;
import com.bbi.pesquisa.model.NetworkConfiguration;
import com.bbi.pesquisa.util.UIManager;
import com.bbi.pesquisa.util.NetworkManager;


public class MainActivity extends AppCompatActivity {
    boolean isLongPress = false;

    private UIManager UIManager = new UIManager();

    private NetworkManager networkManager;

    private ImageView logo;

    private EditText inputOrderId, inputIp, inputPort, inputSsid, inputPass;

    private LinearLayout authForm, configForm, orderForm;
    private FrameLayout mainLayout;

    private NetworkConfiguration network;

    private BroadcastReceiver idReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int surveyId  = intent.getIntExtra("surveyId", 0);

            if ( surveyId > 0 )
                getFragment(new LastFragment());
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
    protected void onStart() {

        super.onStart();

        if ( network != null ) {
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

    Activity activity;
//    long then = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        activity = MainActivity.this;

        mainLayout = findViewById(R.id.frameLayout);

        // init global vars
        logo        = findViewById(R.id.logo);

        // init order form
        orderForm    = findViewById(R.id.orderForm);
        inputOrderId = findViewById(R.id.orderId);

        // init network configuration form
        authForm   = findViewById(R.id.authForm);
        configForm = findViewById(R.id.configForm);
        inputIp    = findViewById(R.id.ip);
        inputPort  = findViewById(R.id.port);
        inputSsid  = findViewById(R.id.ssid);
        inputPass  = findViewById(R.id.pass);

        networkManager = new NetworkManager(this);
        network = networkManager.getConfiguration();

        boolean isConnected = networkManager.isConnected(getApplicationContext());

        if ( !isConnected || network != null && network.getId() != 1) {
            UIManager.showModal(activity, configForm);
            displayNetworkConfig();
            mainLayout.setVisibility(View.GONE);
        } else {
            UIManager.getLogo(getApplicationContext());
        }


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
                                UIManager.showModal(activity, orderForm);
                                UIManager.showFocusOn(activity, inputOrderId);
                            }
                        }

                    }, 3000);
                }
                else if ( event.getAction() == MotionEvent.ACTION_UP )
                {
                    isLongPress = false;
                }

                return true;
            }
        });


        LinearLayout modal = findViewById(R.id.modal);
        modal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIManager.hideModal(activity, view);
            }
        });

        LinearLayout logo_bbi = findViewById(R.id.footer);
        logo_bbi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                EditText authPassword = findViewById(R.id.authPassword);
                UIManager.showModal(activity, authForm);
                UIManager.showFocusOn(activity, authPassword);
                return false;
            }
        });

        Button authButton = findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authUser(view);
            }
        });


        Button saveNetworkButton = findViewById(R.id.saveNetworkButton);
        saveNetworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNetwork();
                UIManager.hideModal(activity, view);
            }
        });


        getFragment( new PromptFragment() );
    }

    private void authUser(View view) {
        EditText authPassword = findViewById(R.id.authPassword);
        UIManager.showFocusOn(activity, authPassword);

        displayNetworkConfig();

        if ( authPassword.getText().toString().trim().equals("") )
            UIManager.showModal(activity, configForm);
        else
            UIManager.hideModal(activity, view);

        authPassword.setText("");
    }

    private void displayNetworkConfig() {
        NetworkConfiguration config = networkManager.getConfiguration();
        inputIp.setText( config.getIp() );
        inputPort.setText( String.valueOf(config.getPort()) );
        inputSsid.setText( config.getSsid() );
        inputPass.setText( config.getPass() );
    }

    private void saveNetwork() {
        String ip   = inputIp.getText().toString().trim();
        String ssid = inputSsid.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        int port = Integer.parseInt( inputPort.getText().toString().trim() );

        // try connect with new credentials
        boolean connected = networkManager.wifiConnect(activity, ssid, pass);

        if (connected) {

            mainLayout.setVisibility(View.VISIBLE);

            boolean isConnected = networkManager.isConnected(getApplicationContext());

            if ( isConnected )
                UIManager.getLogo(getApplicationContext());

        }

        if ( network != null && network.getId() == 1 )
            networkManager.updateConfiguration(ip, port, ssid, pass);

        else if ( network != null )
            networkManager.insertConfiguration(ip, port, ssid, pass);
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }


} //end Activity
