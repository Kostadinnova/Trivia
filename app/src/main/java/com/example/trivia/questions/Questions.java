package com.example.trivia.questions;

import android.content.Context;

import com.example.trivia.api.Result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Questions implements Serializable {

    public transient Context context;
    public List<Result> results;

    public List<String> questions;


    public List<String> optA;
    public List<String> optB;
    public List<String> optC;
    public List<String> optD;
    public List<Integer> Answer;

    public Questions() {

    }

    public Questions(Context context) {
        this.context = context;
        questions = new ArrayList<>();
        results = new ArrayList<>();
        optA = new ArrayList<>();
        optB = new ArrayList<>();
        optC = new ArrayList<>();
        optD = new ArrayList<>();
        Answer = new ArrayList<>();
    }

}





