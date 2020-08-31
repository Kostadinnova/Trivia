package com.example.trivia.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trivia.R;
import com.example.trivia.SolutionActivity;
import com.example.trivia.questions.Questions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnswersFragment extends Fragment {

    @BindView(R.id.rv_answers)
    RecyclerView solutions;

    Questions q;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answers, container, false);

        ButterKnife.bind(this, view);

        ArrayList<Integer> Answers = SolutionActivity.getAnswer();

        AnswerAdapter answerAdapter = new AnswerAdapter(Answers,
                SolutionActivity.getAnswers(),
                SolutionActivity.getOptA(),
                SolutionActivity.getOptB(),
                SolutionActivity.getOptC(),
                SolutionActivity.getOptD());
        solutions.setAdapter(answerAdapter);

        return view;
    }
}
