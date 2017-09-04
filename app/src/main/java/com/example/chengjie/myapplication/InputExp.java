package com.example.chengjie.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class InputExp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_exp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setTitle("编辑个人资料");
        setSupportActionBar(toolbar);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final EditText input = (EditText) findViewById(R.id.input);
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = input.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                editor.putString("exp", res);
                editor.apply();
                UpLoadInfo.expView.setCenterString(res);
                finish();
            }
        });
    }
}
