package com.example.s1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {



    TextView titleText;
    ImageButton titleLeft;
    ImageButton titleRight;

    TextView t1;
    TextView t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        String info=getIntent().getStringExtra("menu_info");
        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);

        titleText=(TextView) findViewById(R.id.title_text);

        titleLeft=(ImageButton)findViewById(R.id.title_left);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight=(ImageButton)findViewById(R.id.title_right);
        titleRight.setVisibility(View.INVISIBLE);



        if(info.equals("use_help"))
        {
            t1.setText(R.string.use_help1);
            t2.setText(R.string.use_help2);
            titleText.setText("使用说明");
        }
        else if(info.equals("about_us"))
        {
            t1.setText(R.string.about_us1);
            t2.setText(R.string.about_us2);
            titleText.setText("关于我们");
        }
    }
}
