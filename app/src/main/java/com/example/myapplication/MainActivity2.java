package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.w3c.dom.Document;

public class MainActivity2 extends MainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        Intent intent=getIntent();
        float dollar=intent.getFloatExtra("dollar_rate_key",0.0f);
        float won=intent.getFloatExtra("won_rate_key",0.0f);
        float pound=intent.getFloatExtra("pound_rate_key",0.0f);
    }
}