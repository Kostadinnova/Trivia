package com.example.trivia;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.trivia.adapters.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SolutionActivity extends AppCompatActivity {

    static ArrayList<Integer> Answers;
    static ArrayList<String> Question;
    static ArrayList<String> optA;
    static ArrayList<String> optB;
    static ArrayList<String> optC;
    static ArrayList<String> optD;
    static ArrayList<Integer> Answer;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        ButterKnife.bind(this);

        Answers = getIntent().getIntegerArrayListExtra("Answer");
        Answer = getIntent().getIntegerArrayListExtra("Answers");
        Question = getIntent().getStringArrayListExtra("Question");
        optA = getIntent().getStringArrayListExtra("optA");
        optB = getIntent().getStringArrayListExtra("optB");
        optC = getIntent().getStringArrayListExtra("optC");
        optD = getIntent().getStringArrayListExtra("optD");

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onSupportNavigateUp(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }


    public static ArrayList<Integer> getAnswer() {
        return Answers;
    }

    public static ArrayList<Integer> getAnswers() {
        return Answer;
    }

    public static ArrayList<String> getQuestion() {
        return Question;
    }

    public static ArrayList<String> getOptA() {
        return optA;
    }

    public static ArrayList<String> getOptB() {
        return optB;
    }

    public static ArrayList<String> getOptC() {
        return optC;
    }

    public static ArrayList<String> getOptD() {
        return optD;
    }


    }





