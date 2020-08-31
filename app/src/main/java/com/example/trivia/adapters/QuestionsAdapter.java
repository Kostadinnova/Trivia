package com.example.trivia.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trivia.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {
    private List<String> Questions;

    QuestionsAdapter(List<String> questions) {
        Questions = questions;
    }

    @NonNull
    @Override
    public QuestionsAdapter.QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new QuestionsAdapter.QuestionsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mutual, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.QuestionsViewHolder questionsViewHolder, int i) {
        String qus ="Q."+(i + 1);
        String question = Questions.get(i);

        questionsViewHolder.question.setText(qus);
        questionsViewHolder.answered.setText(question);
        questionsViewHolder.correct.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return Questions.size();
    }

    class QuestionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_question)
        TextView question;

        @BindView(R.id.tv_answered)
        TextView answered;

        @BindView(R.id.tv_correct)
        TextView correct;

        QuestionsViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

