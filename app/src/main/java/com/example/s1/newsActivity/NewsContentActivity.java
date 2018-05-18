package com.example.s1.newsActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.s1.R;

public class NewsContentActivity extends AppCompatActivity {

    TextView titleText;
    ImageButton titleLeft;
    ImageButton titleRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        titleText=(TextView) findViewById(R.id.title_text);
        titleText.setText("新闻详情");
        titleLeft=(ImageButton)findViewById(R.id.title_left);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight=(ImageButton)findViewById(R.id.title_right);
        titleRight.setVisibility(View.INVISIBLE);



        Intent intent=getIntent();
        String data=intent.getStringExtra("url");
        WebView webView=(WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(data);
    }
}
