package com.example.s1.newsActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.R;
import com.example.s1.rxjava.RxBus2;

import java.util.ArrayList;

public class NewsFirstRunActivity extends AppCompatActivity
implements View.OnClickListener{

    private ArrayList<Integer> non_interests;
    ImageView cbPolitics;
    ImageView cbWar;
    ImageView cbFinance;
    ImageView cbFun;
    ImageView cbTechnology;
    ImageView cbSport;
    TextView finished;

    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.politics:
                cbPolitics.setImageResource(R.mipmap.politics);
                break;
            case R.id.war:
                cbWar.setImageResource(R.mipmap.war);
                break;
            case R.id.finance:
                cbFinance.setImageResource(R.mipmap.finance);
                break;
            case R.id.fun:
                cbFun.setImageResource(R.mipmap.fun);
                break;
            case R.id.technology:
                cbTechnology.setImageResource(R.mipmap.technology);
                break;
            case R.id.sport:
                cbSport.setImageResource(R.mipmap.sport);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_first_run);
        initialView();
        initialEvent();

    }

    public void initialView()
    {

        cbPolitics=(ImageView) findViewById(R.id.politics);
        cbWar=(ImageView) findViewById(R.id.war);
        cbFinance=(ImageView) findViewById(R.id.finance);
        cbFun=(ImageView) findViewById(R.id.fun);
        cbTechnology=(ImageView) findViewById(R.id.technology);
        cbSport=(ImageView) findViewById(R.id.sport);
        finished=(TextView)findViewById(R.id.finish);

        cbPolitics.setImageResource(R.mipmap.politics_un);
        cbWar.setImageResource(R.mipmap.war_un);
        cbFinance.setImageResource(R.mipmap.finance_un);
        cbFun.setImageResource(R.mipmap.fun_un);
        cbTechnology.setImageResource(R.mipmap.technology_un);
        cbSport.setImageResource(R.mipmap.sport_un);

    }
    public void initialEvent()
    {
        cbPolitics.setOnClickListener(this);
        cbWar.setOnClickListener(this);
        cbFinance.setOnClickListener(this);
        cbFun.setOnClickListener(this);
        cbTechnology.setOnClickListener(this);
        cbSport.setOnClickListener(this);
        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                non_interests=new ArrayList<Integer>();
                non_interests.add(0);
                non_interests.add(1);
                non_interests.add(2);
                RxBus2.getDefault().post(non_interests);
                //Toast.makeText(NewsFirstRunActivity.this,"eee",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
