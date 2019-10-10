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
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bbi.pesquisa.fragments.LastFragment;
import com.bbi.pesquisa.fragments.FirstFragment;
import com.bbi.pesquisa.model.NetworkConfiguration;
import com.bbi.pesquisa.services.GetLogoService;
import com.bbi.pesquisa.util.UIManager;
import com.bbi.pesquisa.util.NetworkManager;
import com.bbi.pesquisa.util.WifiManager;


public class MainActivity extends AppCompatActivity {

    private Context context;
    private View activityView;
    private boolean isLongPress = false;

    private UIManager uiManager;
    private NetworkManager networkManager;
    private NetworkConfiguration networkConfiguration;
    private WifiManager wifiManager;

    private ImageView logo;
    private ProgressBar progressBar;
    private FrameLayout layout;
    private Button authButton, saveNetworkButton;
    private LinearLayout authForm, configForm, orderForm, logo_bbi;
    private EditText inputOrderId, inputIp, inputPort, inputSsid, inputPass, authPassword;

    private BroadcastReceiver idReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int surveyId = intent.getIntExtra("surveyId", 0);
            String message = intent.getStringExtra("message");

            Bundle bundle = new Bundle();
            bundle.putString("message", message);

            Fragment fragment = new LastFragment();
            fragment.setArguments(bundle);

            if ( surveyId > 0 )
                getFragment(fragment);
        }
    };


    private BroadcastReceiver bitmapReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            showContent();
            Bitmap bitmap = intent.getParcelableExtra("bitmap");
            if ( bitmap != null ) {
                logo.setImageBitmap(bitmap);
                uiManager.hideProgressBar(layout, progressBar);
                showContent();
            } else {
                uiManager.showModal(configForm);
                displayNetworkConfiguration();
                uiManager.toast("Tempo de conexão esgotado");
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();

        if ( networkConfiguration != null ) {
            LocalBroadcastManager
                    .getInstance(context)
                    .registerReceiver(idReceiver, new IntentFilter("SaveDataService"));

            LocalBroadcastManager
                    .getInstance(context)
                    .registerReceiver(bitmapReceiver, new IntentFilter("GetLogoService"));
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initGlobalVars();
        uiManager.showProgressBar(layout, progressBar);

        if ( !isWifiConnected() || networkConfiguration != null && networkConfiguration.getId() != 1 ) {
            uiManager.showModal(configForm);
            displayNetworkConfiguration();
            layout.setVisibility(View.GONE);
        } else {
            getLogo();
        }

        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                longClick(event, "showOrderForm");
                return true;
            }
        });


        logo_bbi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                longClick(event, "showConfigForm");
                return true;
            }
        });


        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authUser(view);
            }
        });


        saveNetworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNetworkConfiguration();
            }
        });


        getFragment( new FirstFragment() );
    }

    private void initGlobalVars() {
        context = getApplicationContext();
        activityView = findViewById(R.id.mainActivity);

        uiManager = new UIManager(MainActivity.this);
        wifiManager = new WifiManager(context);

        layout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);

        logo = findViewById(R.id.logo);
        logo_bbi = findViewById(R.id.footer);
        authButton = findViewById(R.id.authButton);
        authPassword = findViewById(R.id.authPassword);
        saveNetworkButton = findViewById(R.id.saveNetworkButton);

        // init order form
        orderForm    = findViewById(R.id.orderForm);
        inputOrderId = findViewById(R.id.orderId);

        // init networkConfiguration configuration form
        authForm   = findViewById(R.id.authForm);
        configForm = findViewById(R.id.configForm);
        inputIp    = findViewById(R.id.ip);
        inputPort  = findViewById(R.id.port);
        inputSsid  = findViewById(R.id.ssid);
        inputPass  = findViewById(R.id.pass);

        networkManager = new NetworkManager(this);
        networkConfiguration = networkManager.getConfiguration();
    }

    private void authUser(View view) {
        uiManager.showFocusOn(authPassword);
        displayNetworkConfiguration();

        if ( authPassword.getText().toString().trim().equals("foodbbi") )
            uiManager.showModal(configForm);
        else {
            uiManager.hideModal(view);
            uiManager.toast("Senha incorreta");
        }

    }

    private void displayNetworkConfiguration() {
        NetworkConfiguration config = networkManager.getConfiguration();
        inputIp.setText( config.getIp() );
        inputPort.setText( String.valueOf(config.getPort()) );
        inputSsid.setText( config.getSsid() );
        inputPass.setText( config.getPass() );
    }

    private void saveNetworkConfiguration() {
        String ip   = inputIp.getText().toString().trim();
        String ssid = inputSsid.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        int port = Integer.parseInt( inputPort.getText().toString().trim() );

        // try connect to wifi with new credentials
        wifiManager.wifiConnect(ssid, pass);

        // insert or update local network configuration on sqlite database
        if ( networkConfiguration != null && networkConfiguration.getId() == 1 )
            networkManager.updateConfiguration(ip, port, ssid, pass);
        else if ( networkConfiguration != null )
            networkManager.insertConfiguration(ip, port, ssid, pass);

        verifyConnection();
    }

    private void verifyConnection() {

        uiManager.hideModal(activityView);
        uiManager.showProgressBar(layout, progressBar);

        new android.os.Handler().postDelayed( new Runnable() {
            public void run() {
                if ( isWifiConnected() ) {
                    getLogo();
                } else {
                    uiManager.toast("Erro ao Conectar Wifi!!!");
                    uiManager.hideProgressBar(layout, progressBar);
                    uiManager.showModal(configForm);
                }

            }
        },3000);
    }

    private boolean isWifiConnected() {
        return wifiManager.isWifiConnected(context);
    }

    private void showContent() {
//        uiManager.toast("Conectado!");
        layout.setVisibility(View.VISIBLE);
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }

    private void longClick (MotionEvent event, final String action ) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            isLongPress = true;
            new Handler().postDelayed(new Runnable()
            {

                @Override
                public void run() {
                    if (isLongPress) {

                        if(action.equals("showOrderForm"))
                            uiManager.showModal(orderForm, inputOrderId);
                        else
                            uiManager.showModal(authForm, authPassword);
                    }
                }

            }, 3000);
        }
        else if ( event.getAction() == MotionEvent.ACTION_UP )
        {
            isLongPress = false;
        }
    }

    private void getLogo() {
        GetLogoService service = new GetLogoService();
        service.start(context);
    }


} //end Activity




