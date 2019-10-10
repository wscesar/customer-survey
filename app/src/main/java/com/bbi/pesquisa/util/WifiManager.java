package com.bbi.pesquisa.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.widget.Toast;

import static android.content.Context.WIFI_SERVICE;

public class WifiManager  {

    private Context context;

    public WifiManager(Context context) {
        this.context = context;
    }

    public boolean isWifiConnected( Context context ) {

        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected();

    }


    public void wifiConnect(String networkSSID, String networkPass)
    {
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) context.getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"".concat(networkSSID).concat("\"");
        wifiConfig.status = WifiConfiguration.Status.DISABLED;
        wifiConfig.priority = 40;

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

        int networkID = wifiManager.addNetwork(wifiConfig);

        if( wifiManager.enableNetwork(networkID, true))
            Toast.makeText(context, "Buscando rede...", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Erro ao Conectar Wifi", Toast.LENGTH_SHORT).show();

    }

}