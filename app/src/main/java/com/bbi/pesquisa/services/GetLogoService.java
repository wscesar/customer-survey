package com.bbi.pesquisa.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bbi.pesquisa.util.NetworkManager;
import com.embarcadero.android.Base64;

import java.io.File;
import java.io.FileOutputStream;

public class GetLogoService extends IntentService {

    private static final String ACTION = "GetLogoService";

    public GetLogoService() {
        super(ACTION);
    }

    public static void start( Context context ) {
        Intent intent = new Intent(context, GetLogoService.class);
        intent.setAction(ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {

            final String action = intent.getAction();

            if ( ACTION.equals(action) )
            {
                Bitmap bitmap = null;

                try
                {
                    String imageString = NetworkManager.method.ImageToBase64("//Pesquisa//logo_cliente.png", true);

                    byte[] byteArray = Base64.decode(imageString);
                    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    String filename = "MyImage.png";
                    File file = new File(Environment.getExternalStorageState());
                    File dest = new File(file, filename);

                    FileOutputStream out = new FileOutputStream(dest);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                }
                catch ( Exception e )
                {
                    Log.d("getLogoError", e.getMessage() );
                }

                sendBitmap(bitmap);
            }


        }
    }


    private void sendBitmap(Bitmap bitmap)
    {
        Intent intent = new Intent(ACTION);
        intent.putExtra("bitmap", bitmap);
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }


}
