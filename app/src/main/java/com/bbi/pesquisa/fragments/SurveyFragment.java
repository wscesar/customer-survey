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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bbi.pesquisa.services.GetQuestionsService;
import com.bbi.pesquisa.R;
import com.bbi.pesquisa.model.Alternative;
import com.bbi.pesquisa.model.Answer;
import com.bbi.pesquisa.model.Question;
import com.bbi.pesquisa.util.InterfaceManager;
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
    private LinearLayout layout;

    private int position = 0;
    private int total;

    private ProgressBar progressBar;
    private QuestionManager questionManager = new QuestionManager();

    private View fragmentView;

    private boolean mountQuestionList = true;

    public SurveyFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver questionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            questionList = (List<Question>) intent.getSerializableExtra("questionList");

            total = questionList.size();

            if ( questionList.size() > 0 && mountQuestionList)
            {
                questionManager.showQuestion(0, questionList, getActivity());
                new InterfaceManager().hideProgressBar(layout, progressBar);

                mountQuestionList = false;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentView = inflater.inflate(R.layout.fragment_survey, container, false);

        if ( mountQuestionList ) {
            LocalBroadcastManager
                    .getInstance(getContext())
                    .registerReceiver(questionReceiver, new IntentFilter("ServiceMessage3"));
        }

        rGroup      = fragmentView.findViewById(R.id.rGroup);
        layout      =  fragmentView.findViewById(R.id.surveyLayout);
        progressBar = fragmentView.findViewById(R.id.progressBar);

        new InterfaceManager().showProgressBar(layout, progressBar);

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
        RadioButton rbSelected =
                fragmentView.findViewById(rGroup.getCheckedRadioButtonId());

        Alternative selectedAlternative =
                questionManager.getSelectedAlternative(position, questionList, rGroup, rbSelected);

        alternativeList.add(selectedAlternative);
        answer.setAlternativeList(alternativeList);

        position++;

        if ( position == (total - 1) )
            nextButton.setText("Finalizar Pesquisa");

        if ( position < total ) {
            questionManager.showQuestion(position, questionList, getActivity());
        } else {
//            Fragment fragment = new ToastFragment();
//            Fragment fragment = new AskForOpinionFragment();
            Fragment fragment = new CustomerOpinionFragment();

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
