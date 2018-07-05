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
    boolean[] f=new boolean[6];


    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.politics:
                f[0]=!f[0];
                if(f[0])
                    cbPolitics.setImageResource(R.mipmap.politics);
                else cbPolitics.setImageResource(R.mipmap.politics_un);
                break;
            case R.id.war:
                f[1]=!f[1];
                if(f[1])
                cbWar.setImageResource(R.mipmap.war);
                else cbWar.setImageResource(R.mipmap.war_un);
                break;
            case R.id.finance:
                f[2]=!f[2];
                if(f[2])
                cbFinance.setImageResource(R.mipmap.finance);
                else cbFinance.setImageResource(R.mipmap.finance_un);
                break;
            case R.id.fun:
                f[3]=!f[3];
                if(f[3])
                cbFun.setImageResource(R.mipmap.fun);
                else cbFun.setImageResource(R.mipmap.fun_un);
                break;
            case R.id.technology:
                f[4]=!f[4];
                if(f[4])
                cbTechnology.setImageResource(R.mipmap.technology);
                else cbTechnology.setImageResource(R.mipmap.technology_un);
                break;
            case R.id.sport:
                f[5]=!f[5];
                if(f[5])
                cbSport.setImageResource(R.mipmap.sport);
                else cbSport.setImageResource(R.mipmap.sport_un);
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
