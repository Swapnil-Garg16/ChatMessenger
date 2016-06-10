package com.houssup.houssupmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Chatting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        String name= getIntent().getStringExtra("namevalue");
        TextView textView  = (TextView) findViewById(R.id.textView3);
        textView.setText(name);

    }
}
