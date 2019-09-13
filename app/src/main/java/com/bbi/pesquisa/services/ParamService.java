package com.bbi.pesquisa.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.embarcadero.android.DAOComponent;
import com.embarcadero.android.SqlCommandBuilder;
import com.embarcadero.android.TDBXReader;


public class ParamService extends IntentService {

    private static final String ACTION = "ParamService";

    public ParamService() {
        super(ACTION);
    }

    public static void start( Context context ) {
        Intent intent = new Intent(context, ParamService.class);
        intent.setAction(ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {

            final String action = intent.getAction();

            if ( ACTION.equals(action) )
            {

                SqlCommandBuilder command = SqlCommandBuilder
                    .create("SELECT VALOR FROM TBPARAMETRO WHERE PARAMETRO = 'PRODUTO_BRINDE_PESQUISA'");

                String param ="";
                try
                {
                    TDBXReader reader = new DAOComponent().getMethod().ExecuteQuery( command.toJson() );

                    while ( reader.next() ) {
                        param = reader.getValue("VALOR").GetAsString();
                    }

                } catch (Exception e) {
                    Log.e(ACTION, e.getMessage());
                }

                sendParam(param);
            }
        }
    }

    private void sendParam(String param) {
        Intent intent = new Intent(ACTION);
        intent.putExtra("param", param);
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }

}
