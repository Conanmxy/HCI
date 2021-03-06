package com.example.s1.scheduleActivity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.s1.R;
import com.example.s1.Utils.MyOkHttp;
import com.example.s1.adapter.aBookAdapter;

import java.util.ArrayList;

public class BookDetailActivity extends AppCompatActivity {

    //标题

    TextView titleText;
    ImageButton titleLeft;
    ImageButton titleRight;
    TextView bookNameT;


    ArrayList<ArrayList<String>>arrayList;
    TextView showIntro;
    ImageView imageView;
    RecyclerView recyclerView;
    aBookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        showIntro=(TextView)findViewById(R.id.aBookIntro);
        arrayList=new ArrayList<>();
        imageView=(ImageView)findViewById(R.id.book_image);
        final String bookId=getIntent().getStringExtra("bookId");
        String bookName=getIntent().getStringExtra("tbdName");

        recyclerView=(RecyclerView)findViewById(R.id.query_a_book);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
//        CollapsingToolbarLayout ctoolbar=(CollapsingToolbarLayout)findViewById(R.id.bd_collapsing_toolbar);
//        ctoolbar.setTitle(bookName);

        titleText=(TextView) findViewById(R.id.title_text);
        titleText.setText("图书详情");
        titleLeft=(ImageButton)findViewById(R.id.title_left);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight=(ImageButton)findViewById(R.id.title_right);
        titleRight.setVisibility(View.INVISIBLE);
        bookNameT=(TextView)findViewById(R.id.book_name);
        System.out.println(bookName+"...........");
        int endIndex=bookName.lastIndexOf(" ");
        if(endIndex>0)
        {
            bookName=bookName.substring(0,endIndex);
        }
        bookNameT.setText(bookName);





        new Thread(new Runnable() {
            @Override
            public void run() {
                arrayList= MyOkHttp.queryAbook(bookId);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //里面有转义字符
                        String imageUrl=arrayList.get(arrayList.size()-1).get(0);
                        imageUrl=imageUrl.replace("\\","");
                        Log.d("image",imageUrl);
                        Glide.with(BookDetailActivity.this).load(imageUrl)
                                .into(imageView);
                        arrayList.remove(arrayList.size()-1);

                        String info=arrayList.get(arrayList.size()-1).get(0);
                        if(info==null || info.equals(""))
                             showIntro.setText("暂时没有简介...");
                        else showIntro.setText(info);
                        arrayList.remove(arrayList.size()-1);

                        adapter=new aBookAdapter(arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
}
