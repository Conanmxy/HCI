package com.example.s1.scheduleActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s1.R;
import com.example.s1.Utils.MyOkHttp;
import com.example.s1.adapter.BooksAdapter;

import java.util.ArrayList;
import java.util.List;

public class QueryBookActivity extends AppCompatActivity {

    EditText bookNameE;
    Button searchBook;
    List<String> booksList=new ArrayList<>();;
    RecyclerView recyclerView;
    ImageButton titleLeft;
    ImageButton titleRight;
    TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_book);
        bookNameE=(EditText)findViewById(R.id.book_name);
        searchBook=(Button)findViewById(R.id.book_search);
        bookNameE.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        titleLeft=(ImageButton)findViewById(R.id.title_left);
        titleRight=(ImageButton)findViewById(R.id.title_right);
        titleText=(TextView)findViewById(R.id.title_text);
        titleText.setText("图书检索");
        titleRight.setVisibility(View.INVISIBLE);

        //返回
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取缓存的用户信息
        SharedPreferences userPrefer=getSharedPreferences("TJuser",MODE_PRIVATE);

        final String userName=userPrefer.getString("username","");
        final String  password=userPrefer.getString("password","");

        recyclerView=(RecyclerView)findViewById(R.id.books_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookNameE.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawable=bookNameE.getCompoundDrawables()[2];
                if(drawable==null)//右边没有图片
                {
                    return false;
                }
                //如果不是按下事件
                if(event.getAction()!=MotionEvent.ACTION_UP)
                {
                    return false;
                }
                if(event.getX()>bookNameE.getWidth()
                        -bookNameE.getPaddingRight()
                        -drawable.getIntrinsicWidth())
                {
                    bookNameE.setText("");
                }


                return false;
            }

        });



        bookNameE.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String bookName=bookNameE.getText().toString();
                            booksList= MyOkHttp.loginLib(userName,password,bookName);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BooksAdapter booksAdapter=new BooksAdapter(booksList);
                                    if(booksList.size()==0)
                                    {
                                        Toast.makeText(QueryBookActivity.this,"没有查询结果！",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        recyclerView.setAdapter(booksAdapter);
                                    }
                                }
                            });

                        }
                    }).start();
                }

                return true;
            }
        });



        searchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String bookName=bookNameE.getText().toString();
                System.out.print("bookname1.."+bookName);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       booksList= MyOkHttp.loginLib(userName,password,bookName);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BooksAdapter booksAdapter=new BooksAdapter(booksList);
                                if(booksList.size()==0)
                                {
                                    Toast.makeText(QueryBookActivity.this,"没有查询结果！",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    recyclerView.setAdapter(booksAdapter);
                                }
                            }
                        });

                    }
                }).start();

            }
        });
    }
}
