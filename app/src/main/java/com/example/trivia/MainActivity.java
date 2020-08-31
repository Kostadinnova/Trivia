package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trivia.api.Api;
import com.example.trivia.api.ApiCount;
import com.example.trivia.api.QuizQuestions;
import com.example.trivia.api.Result;
import com.example.trivia.questions.Questions;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button start;
    Button filter;
    ProgressBar progressBar;
    Questions q;
    String difficulty;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFilterDefaultValues();
        start=findViewById(R.id.start_btn);
        filter=findViewById(R.id.home_filter);
        progressBar=findViewById(R.id.progressBar2);
        start.setOnClickListener(onClickListener);
        filter.setOnClickListener(onClickListener);



        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        category=sharedPrefs.getString(
                getString(R.string.category_key),
                getString(R.string.medium_value)
        );

        difficulty=sharedPrefs.getString(
                getString(R.string.difficulty_key),
                getString(R.string.medium_value)
        );
    }

    private void setFilterDefaultValues() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty=sharedPrefs.getString(
                getString(R.string.difficulty_key),
                null
        );
        category=sharedPrefs.getString(
                getString(R.string.category_key),
                null
        );
        if (difficulty == null){
            sharedPrefs
                    .edit()
                    .putString(getString(R.string.difficulty_key),getString(R.string.easy_value))
                    .apply();
        }
        if (category == null){
            sharedPrefs
                    .edit()
                    .putString(getString(R.string.category_key),getString(R.string.any_category_value))
                    .apply();
        }
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.start_btn){
                progressBar.setVisibility(View.VISIBLE);
                q=new Questions(getApplicationContext());
                view.setClickable(false);
                fetchQuestionCount();
            }
            else if(view.getId()==R.id.home_filter){
                Intent intent=new Intent(getApplicationContext(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    private void fetchQuestionCount() {
        int category_value=Integer.valueOf(category);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<ApiCount> call = api.getQuizQuestions(category_value);
        call.enqueue(new Callback<ApiCount>() {
            @Override
            public void onResponse(Call<ApiCount> call, Response<ApiCount> response) {
                switch (difficulty){
                    case "easy" :
                        if (response.body() != null) {
                            fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalEasyQuestionCount());
                        }
                        break;
                    case "medium" :
                        if (response.body() != null) {
                            fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalMediumQuestionCount());
                        }
                        break;
                    case "hard" :
                        if (response.body() != null) {
                            fetchQuestionAPI(response.body().getCategoryQuestionCount().getTotalHardQuestionCount());
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiCount> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                start.setClickable(true);
            }
        });
    }

    public void fetchQuestionAPI(int categoryCount) {
        int category_value=Integer.valueOf(category);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<QuizQuestions> call = api.getQuizQuestions
                ("url3986", categoryCount >= 10 ? 10 : categoryCount-1, difficulty, "multiple",category_value );
        call.enqueue(new Callback<QuizQuestions>() {
            @Override
            public void onResponse(Call<QuizQuestions> call, Response<QuizQuestions> response) {

                Log.v("url-----", call.request().url().toString());

                QuizQuestions quizQuestions = response.body();

                if (quizQuestions.getResponseCode() == 0) {

                    q.results = quizQuestions.getResults();

                    if (q.results != null) {
                        for (Result r : q.results) {
                            try {
                                q.questions.add(java.net.URLDecoder.decode(r.getQuestions(), "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            Random random = new Random();
                            int ran = random.nextInt(4);
                            setOptions(r, ran);

                            q.Answer.add(ran+1);
                        }
                        Log.v("answers", q.Answer.toString());
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                start.setClickable(true);
                Intent intent=new Intent(MainActivity.this,QuizActivity.class);
                intent.putExtra("question",q);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<QuizQuestions> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                start.setClickable(true);
            }
        });
    }

    void setOptions(Result r, int ran) {
        List<String> wrong;
        switch (ran) {
            case 0:
                try {
                    q.optA.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optB.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    q.optB.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optC.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    q.optC.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optD.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    q.optD.add(java.net.URLDecoder.decode(r.getCorrectAnswer(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                wrong = r.getIncorrectAnswers();
                try {
                    q.optA.add(java.net.URLDecoder.decode(wrong.get(0), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optB.add(java.net.URLDecoder.decode(wrong.get(1), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    q.optC.add(java.net.URLDecoder.decode(wrong.get(2), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
