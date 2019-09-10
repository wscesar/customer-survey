package com.bbi.pesquisa.util;

import android.app.Activity;
import android.graphics.Color;
import android.service.autofill.FieldClassification;
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
    private int gray = Color.parseColor("#EEEEEE");

    public int transparentColor = Color.parseColor("#00FFFFFF");

    public void showQuestion(int position, List<Question> questionList, Activity activity) {


        int questionNumber = position + 1;

        Question currentQuestion = questionList.get(position);
        List<Alternative> currentAlternatives = currentQuestion.getAlternativeList();

        TextView headerTitle = activity.findViewById(R.id.headerTitle);
        headerTitle.setText( questionNumber + ") "+ currentQuestion.getQuestion() );

        final Button nextButton = activity.findViewById(R.id.nextButton);
        nextButton.setVisibility(View.INVISIBLE);
//        nextButton.setBackgroundColor(gray);

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
            //                rButton.setButtonDrawable(R.color.gray);

            rButton.setHighlightColor(color);




            rButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT ));

//            if(i%2 == 0){
//                rButton.setBackgroundColor(gray);
//            }

            radioButtons.add(rButton);

            rGroup.addView( rButton );
        }


        for (final RadioButton r : radioButtons) {

//            r.setTextSize(12);
//            r.setBackgroundColor(transparentColor);

            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    r.setTextSize(12);

                    nextButton.setVisibility(View.VISIBLE);
//
//
//                    for (RadioButton r : radioButtons) {
//                        r.setBackgroundColor(transparentColor);
//                    }

//                    view.setBackgroundColor(color);
//                    r.setBackgroundColor(color);
//                    r.setBackgroundColor(getResources().getColor(R.color.colorAccent));
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
