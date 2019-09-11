package com.bbi.pesquisa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bbi.pesquisa.fragments.LastFragment;
import com.bbi.pesquisa.fragments.OrderFragment;
import com.bbi.pesquisa.model.NetworkConfiguration;
import com.bbi.pesquisa.util.InterfaceManager;
import com.bbi.pesquisa.util.NetworkManager;


public class MainActivity extends AppCompatActivity {
    boolean isLongPress = false;

    private InterfaceManager interfaceManager = new InterfaceManager();

    private NetworkManager networkManager;

    private ImageView logo;

    private EditText inputOrderId, authPassword, inputIp, inputPort, inputSsid, inputPass;

    private LinearLayout modal, authForm, configForm, orderForm;

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
    long then = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        // init global vars
        logo        = findViewById(R.id.logo);
        modal       = findViewById(R.id.modal);

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

        if ( network != null ) {

            if ( network.getId() == 1 )
                interfaceManager.getLogo(getApplicationContext()); //getLogo();
            else
                showModal(configForm);


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
                                    interfaceManager.showFocusOn(MainActivity.this, getApplicationContext(), inputOrderId);
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
                    interfaceManager.showFocusOn(MainActivity.this, getApplicationContext(), authPassword);
                    return false;
                }
            });


            Button configNetwork = findViewById(R.id.networkConfigButton);
            configNetwork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveNetwork();
                    interfaceManager.getLogo(getApplicationContext());
                    hideModal(view);
                }
            });

            Button authButton = findViewById(R.id.authButton);
            authButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText authPassword = findViewById(R.id.authPassword);
                    interfaceManager.showFocusOn(MainActivity.this, getApplicationContext(), authPassword);

                    if(authPassword.getText().toString().trim().equals("")) {
                        showModal(configForm);
                    } else {
                        authPassword.setText("");
                        hideModal(view);
                    }
                }
            });

            getFragment( new OrderFragment() );
//            getFragment( new PromptFragment() );
        }
    }


    private void displayNetworkConfig() {
        NetworkConfiguration config = networkManager.getConfiguration();
        inputIp.setText( config.getIp() );
        inputPort.setText( config.getPort()+"" );
        inputSsid.setText( config.getSsid() );
        inputPass.setText( config.getPass() );
    }

    private void saveNetwork() {
        String ip   = inputIp.getText().toString().trim();
        String ssid = inputSsid.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        int port = Integer.parseInt( inputPort.getText().toString().trim() );

        wifiConnect(ssid, pass);

        if ( network != null && network.getId() == 1 )
            networkManager.updateConfiguration(ip, port, ssid, pass);
        else if ( network != null )
            networkManager.insertConfiguration(ip, port, ssid, pass);
    }

    private void showModal(LinearLayout form) {
        hideForms();
        modal.setVisibility(View.VISIBLE);
        form.setVisibility(View.VISIBLE);
    }

    private void hideModal(View view) {
        hideForms();
        modal.setVisibility(View.GONE);
        interfaceManager.hideKeyboard(MainActivity.this, getApplicationContext(), view);
    }

    private void hideForms() {
        authForm.setVisibility(View.GONE);
        orderForm.setVisibility(View.GONE);
        configForm.setVisibility(View.GONE);
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();
    }


    private void wifiConnect(String networkSSID, String networkPass)
    {
        /* Cria um objeto com os dados da conexão wifi do aparelho */
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wm.setWifiEnabled(true);

        /* Cria configuração do Wireless */
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"".concat(networkSSID).concat("\"");
        wifiConfig.status = WifiConfiguration.Status.DISABLED;
        wifiConfig.priority = 40;

        /* WPA/WPA2 Security */
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfig.preSharedKey = "\"".concat(networkPass).concat("\"");

        /* Adiciona a rede Wireless */
        int networkID = wm.addNetwork(wifiConfig);

        /* Conecta a rede Wireless, caso falhe, exibe uma mensagem de erro. */
        if(!wm.enableNetwork(networkID, true))
        {
            Toast.makeText(getApplicationContext(), "Erro ao conectar", Toast.LENGTH_SHORT).show();
        }else{
            interfaceManager.getLogo(getApplicationContext());
            alert(networkSSID);
        }

    }


    private void alert(String networkSSID ){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conectando")
                .setMessage("Conectando na rede: " + networkSSID + ".")
                .setNeutralButton("OK", null)
                .show();
    }

} //end Activity
