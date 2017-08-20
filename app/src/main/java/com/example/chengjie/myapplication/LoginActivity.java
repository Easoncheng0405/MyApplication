package com.example.chengjie.myapplication;

import android.animation.ValueAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.UserInfo;
import base.UserInfoJSON;
import util.HttpRequest;

/**
 * Created by chengjie on 17-8-19.
 */

public class LoginActivity extends Activity {
    private EditText etUsername;
    private EditText etPassword;
    private CircularProgressButton btGo;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btGo.setProgress(0);
                String info = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString();
                if (!isPhoneNum(info))
                    try {
                        info = URLEncoder.encode(info, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                login(info, password);
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

    private void init() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btGo = (CircularProgressButton) findViewById(R.id.bt_go);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void login(final String info, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format.format(date);
                String url = "http://49.140.61.67:8080/Server/Login";
                String opLoc = "吉林省长春市吉林大学前卫南区北苑一公寓";
                String note = "无";
                try {
                    opLoc = URLEncoder.encode(opLoc, "UTF-8");
                    note = URLEncoder.encode(note, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String content = "info=" + info + "&passWord=" + password + "&opInfo.opTime=" + time + "&opInfo.opLoc=" + opLoc + "&opInfo.opDev=" + android.os.Build.MODEL
                        + "&opInfo.opType=Login" + "&opInfo.note=" + note;
                String res = HttpRequest.request(url, content);
                System.out.println(res);
                Gson gson = new Gson();
                UserInfoJSON infoJSON = gson.fromJson(res, UserInfoJSON.class);
                final int code = infoJSON.getCode();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setButtonProgress(btGo,code);
                    }
                });
            }
        }).start();

    }

    private boolean isPhoneNum(String phoneNum) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();

    }

    private void setButtonProgress(final CircularProgressButton button, int code) {
        if (code == 0) {
            ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
            widthAnimation.setDuration(1500);
            widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    button.setProgress(value);
                }
            });
            widthAnimation.start();
            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        } else {
            ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
            widthAnimation.setDuration(1500);
            widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    button.setProgress(value);
                    if (value == 99) {
                        button.setProgress(-1);
                    }
                }
            });
            widthAnimation.start();
            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();

        }

    }
}