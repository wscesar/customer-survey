package com.bbi.pesquisa.util;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.widget.Toast;

import static android.content.Context.WIFI_SERVICE;

public class WifiManager  {

    public boolean isWifiConnected(Context context ) {

        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return wifi.isConnected();

    }


    public boolean wifiConnect(Activity activity, String networkSSID, String networkPass)
    {
        Context context = activity.getApplicationContext();

        /* Cria um objeto com os dados da conexão wifi do aparelho */
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) context.getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);

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
        int networkID = wifiManager.addNetwork(wifiConfig);


        /* Conecta a rede Wireless, caso falhe, exibe uma mensagem de erro. */
        if( wifiManager.enableNetwork(networkID, true)) {
            Toast.makeText(context, "Conectando...", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            Toast.makeText(context, "Erro ao conectar", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}