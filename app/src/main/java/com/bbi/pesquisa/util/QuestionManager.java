package com.bbi.pesquisa.util;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Alternative;
import com.bbi.pesquisa.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionManager extends AppCompatActivity {

    private int color = Color.parseColor("#2d2d2d");

    public void showQuestion(int position, List<Question> questionList, Activity activity) {

        int questionNumber = position + 1;

        Question currentQuestion = questionList.get(position);
        List<Alternative> currentAlternatives = currentQuestion.getAlternativeList();

        TextView headerTitle = activity.findViewById(R.id.title);
        headerTitle.setText( questionNumber + ") "+ currentQuestion.getQuestion() );

        final Button nextButton = activity.findViewById(R.id.nextButton);
        nextButton.setVisibility(View.INVISIBLE);

        RadioGroup rGroup = activity.findViewById(R.id.rGroup);
        rGroup.removeAllViews();
        rGroup.clearCheck();

        final List<RadioButton> radioButtons = new ArrayList<>();

        for ( int i = 0; i < currentAlternatives.size(); i++ )
        {
            RadioButton rButton = new RadioButton( activity.getApplicationContext() );
            Alternative a = currentAlternatives.get(i);

            rButton.setText( a.getDescricao() ); // 22 28
            rButton.setTextSize( 26 );
            rButton.setTextColor(color);
            rButton.setPadding(32,16,32,16);

            rButton.setHighlightColor(color);

            rButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT ));

            radioButtons.add(rButton);

            rGroup.addView( rButton );
        }


        for (final RadioButton r : radioButtons) {
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextButton.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    public Alternative getSelectedAlternative(int position, List<Question> questionList, RadioGroup rGroup, RadioButton rbSelected)
    {
        Question currentQuestion = questionList.get(position);
        List<Alternative> currentQuestionAlternatives =  currentQuestion.getAlternativeList();

        int i = rGroup.indexOfChild(rbSelected);
        Alternative a = currentQuestionAlternatives.get(i);

        return a;
    }

}
