package com.example.chengjie.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import base.UserInfoJSON;
import util.HttpRequest;

/**
 * Created by chengjie on 17-8-19.
 */

public class LoginActivity extends Activity {
    private EditText etUsername;
    private EditText etPassword;
    private CircularProgressButton btGo;
    private CardView cv;
    private FloatingActionButton fab;
    private int progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String name=URLEncoder.encode(etUsername.getText().toString().trim(),"UTF-8");
                    final String phone=etPassword.getText().toString();
                    if(cheacUserAlreadyExist(name,phone)){

                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fab, fab.getTransitionName());
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            }
        });
    }
    private void init(){
        progress=0;
        etUsername=(EditText)findViewById(R.id.et_username);
        etPassword=(EditText)findViewById(R.id.et_password);
        btGo=(CircularProgressButton)findViewById(R.id.bt_go);
        cv=(CardView)findViewById(R.id.cv);
        fab=(FloatingActionButton)findViewById(R.id.fab);
    }

    private boolean cheacUserAlreadyExist(final String phone,final String name){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://49.140.61.67:8080/JLUServer/UserSignIn";
                String content = "phone="+phone+"&name="+name;
                final String result = HttpRequest.request(url, content);
                Gson gson = new Gson();
                UserInfoJSON infoJSON = gson.fromJson(result, UserInfoJSON.class);
                if (infoJSON.getCode() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"可以注册\n"+result, Toast.LENGTH_LONG).show();
                            btGo.setProgress(100);
                        }
                    });
                } else if(infoJSON.getCode()==101){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "用户名已存在\n"+result, Toast.LENGTH_LONG).show();
                            showError();
                        }
                    });
                }else if(infoJSON.getCode()==102){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "电话号码已注册\n"+result, Toast.LENGTH_LONG).show();
                            showError();
                        }
                    });
                }
            }
        }).start();
        return false;
    }

    private void signUp(final String phone,final String name,final String password){

    }

    private void showError(){
        btGo.setProgress(-1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btGo.setProgress(0);
    }


}
