package com.bbi.pesquisa.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bbi.pesquisa.model.Alternative;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.util.NetworkManager;
import com.embarcadero.android.DAOComponent;
import com.embarcadero.android.DBXException;
import com.embarcadero.android.SqlCommandBuilder;

import java.util.List;


public class SaveDataService extends IntentService {

    private static final String ACTION = "SaveDataService";

    public static String getAction() {
        return ACTION;
    }

    public SaveDataService() {
        super("SaveDataService");
    }

    public static void start( Context context, Answer answer ) {
        Intent intent = new Intent(context, SaveDataService.class);
        intent.setAction(ACTION);
        intent.putExtra("answer", answer);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if ( ACTION.equals(action) )
            {
                Answer answer = (Answer) intent.getSerializableExtra("answer");

                String name = answer.getName();
                String phone = answer.getPhone();
                String email = answer.getEmail();
                String city = answer.getCity();
                String birthday = answer.getBirthday();
                String customerOpinion = answer.getCustomerOpinion();
                String orderId = answer.getOrderId();

                if(name == null)
                    name = "";

                if(phone == null)
                    phone = "";

                if(email == null)
                    email = "";

                if(city == null)
                    city = "";

                if(birthday == null)
                    birthday = "";

                if(customerOpinion == null)
                    customerOpinion = "";


                int surveyId;
                try {
                    surveyId = Integer.valueOf(NetworkManager.method.InserirPesquisa(name, phone, email, city, birthday));

                    List<Alternative> alternativeList = answer.getAlternativeList();

                    if( alternativeList != null) {
                        for ( Alternative a : alternativeList )
                        {
                            int idPergunta = a.getIdPergunta();
                            int idAlternativa = a.getIdAlternativa();

                            Log.d(ACTION, "salvando resposta para " + idPergunta + "a pergunta ");
                            NetworkManager.method.InserirResposta(surveyId, idPergunta, idAlternativa);
                        }
                    }

                } catch ( DBXException | NumberFormatException e ) {
                    surveyId = 0;
                    Log.e(ACTION, e.getMessage());
                }

                updateOrder(surveyId, Integer.parseInt(orderId), customerOpinion);

            }
        }
    }

    private void updateOrder(int surveyId, int orderId, String customerOpinion) {
        if( surveyId > 0 )
        {
            try
            {
                String currentTime = NetworkManager.method.GetHora();
                String currentDate = NetworkManager.method.GetData();

                SqlCommandBuilder command =
                        SqlCommandBuilder.create("UPDATE tbpesquisa set observacao='"
                                +customerOpinion+"', data='"+currentDate+"', hora='"+currentTime
                                +"', comanda="+orderId+" WHERE id_pesquisa="+surveyId);

                new DAOComponent().getMethod().ExecuteQuery( command.toJson() );
            }
            catch (DBXException e)
            {
                e.printStackTrace();
            }
        }

        sendSurveyId(surveyId);
    }

    private void sendSurveyId(int surveyId) {
        Intent intent = new Intent(ACTION);
        intent.putExtra("surveyId", surveyId);
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }

}
