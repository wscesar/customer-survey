package com.bbi.pesquisa.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bbi.pesquisa.model.Alternative;
import com.bbi.pesquisa.model.Question;
import com.bbi.pesquisa.util.NetworkManager;
import com.embarcadero.android.TDBXReader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetQuestionsService extends IntentService {

    private static final String ACTION = "GetQuestionsService";

    public GetQuestionsService() {
        super(ACTION);
    }

    public static void start( Context context ) {
        Intent intent = new Intent(context, GetQuestionsService.class);
        intent.setAction(ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {

            final String action = intent.getAction();

            if (ACTION.equals(action))
            {
                List<Question> questionList = new ArrayList<>();

                try
                {
                    TDBXReader getPerguntas = NetworkManager.method.GetPerguntas();

                    while ( getPerguntas.next() )
                    {
                        final int idPergunta = getPerguntas.getValue("ID_PERGUNTA").GetAsInt32();
                        final String pergunta = getPerguntas.getValue("PERGUNTA").GetAsString();

                        Question question = new Question();
                        question.setId(idPergunta);
                        question.setQuestion(pergunta);

                        Log.d("instentService", question.getQuestion());

                        List<Alternative> alternativeListMaker = new ArrayList<>();

                        TDBXReader getAlternativas  = NetworkManager.method.GetAlternativas(idPergunta);

                        while ( getAlternativas.next() )
                        {
                            final String descricao = getAlternativas.getValue("DESCRICAO").toString();
                            final int idAlternativa = getAlternativas.getValue("ALTERNATIVA").GetAsInt32();

                            Alternative a = new Alternative();
                            a.setIdPergunta(idPergunta);
                            a.setIdAlternativa(idAlternativa);
                            a.setDescricao(descricao);

                            alternativeListMaker.add(a);
                            question.setAlternativeList(alternativeListMaker);

                        }

                        questionList.add(question);

                    } // end while getPerguntas()

                    sendQuestions(questionList);

                }
                catch (Exception e)
                {
                    Log.d("getQuestions", e.getMessage());
                    //System.exit(0);
                }

            }
        }
    }

    private void sendQuestions(List<Question> questionList)
    {
        Intent intent = new Intent("ServiceMessage3");
        intent.putExtra("questionList", (Serializable) questionList);
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }


}
