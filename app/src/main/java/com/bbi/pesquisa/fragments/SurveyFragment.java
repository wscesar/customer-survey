package com.bbi.pesquisa.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bbi.pesquisa.services.GetQuestionsService;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Alternative;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.model.Question;
import com.bbi.pesquisa.util.QuestionManager;

import java.util.ArrayList;
import java.util.List;

public class SurveyFragment extends Fragment {
    private Answer answer = new Answer();

    private Bundle bundle;

    private List<Question> questionList = new ArrayList<>();
    private List<Alternative> alternativeList = new ArrayList<>();

    private Button nextButton;
    private RadioGroup rGroup;

    private int position = 0;
    private int total;

    private ProgressBar progressBar;
    private QuestionManager questionManager = new QuestionManager();

    private View fragmentView;

    private boolean mountQuestionList = true;

    private BroadcastReceiver questionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            questionList = (List<Question>) intent.getSerializableExtra("questionList");

            total = questionList.size();

            if ( questionList != null && questionList.size() > 0 && mountQuestionList)
            {
                questionManager.showQuestion(0, questionList, getActivity());
                progressBar.setVisibility(View.INVISIBLE);
                rGroup.setVisibility(View.VISIBLE);

                mountQuestionList = false;
            }
        }
    };


    public SurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_survey, container, false);


        ScrollView scrollView = fragmentView.findViewById(R.id.scrollView);
        scrollView.setScrollbarFadingEnabled(false);

//        scrollView.setBackgroundColor(R.color.colorAccent);

        if ( mountQuestionList ) {
            LocalBroadcastManager
                    .getInstance(getContext())
                    .registerReceiver(questionReceiver, new IntentFilter("ServiceMessage3"));
        }


        progressBar = fragmentView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        rGroup      = fragmentView.findViewById(R.id.rGroup);

        bundle = getArguments();
        answer = (Answer) bundle.getSerializable("answer");

        GetQuestionsService service = new GetQuestionsService();
        service.start(getContext());

        nextButton = fragmentView.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        return fragmentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void nextQuestion()
    {
        RadioButton rbSelected = fragmentView.findViewById(rGroup.getCheckedRadioButtonId());

        if ( rbSelected == null )
        {
            Toast.makeText(getActivity(), "Escolha uma opção!", Toast.LENGTH_SHORT).show();
            return;
        }

        Alternative selectedAlternative = questionManager.getSelectedAlternative(position, questionList, rGroup, rbSelected);
        alternativeList.add(selectedAlternative);
        answer.setAlternativeList(alternativeList);

        position++;

        if ( position == (total - 1) )
        {
            nextButton.setText("Finalizar Pesquisa");
//            nextButton.setBackgroundColor(getActivity().getColor(R.color.green));
        }

        if ( position < total )
        {
            questionManager.showQuestion(position, questionList, getActivity());
        }
        else
        {
//            Fragment fragment = new ToastFragment();
            Fragment fragment = new AskForOpinionFragment();

            bundle.putSerializable("answer", answer);
            fragment.setArguments(bundle);

            getFragment(fragment);
        }

    }

    private void getFragment(Fragment fragment) {

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.frameLayout, fragment )
                .commit();

    }




}
