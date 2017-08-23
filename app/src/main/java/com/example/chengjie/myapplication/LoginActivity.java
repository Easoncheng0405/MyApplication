package com.example.chengjie.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.TeaInfoJSON;
import base.UserInfoJSON;
import util.HttpRequest;
import util.ErrorCode;

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
        setContentView(R.layout.activity_login);
        init();

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HttpRequest.isNetworkAvailable(LoginActivity.this)) {
                    btGo.setProgress(0);
                    String info = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString();
                    if (info.equals("") || password.equals(""))
                        ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_NETWORK_EXCEPTION, "输入不能为空");
                    else {
                        if (!isPhoneNum(info))
                            try {
                                info = URLEncoder.encode(info, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        login(info, password);
                    }
                } else
                    ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_NETWORK_EXCEPTION, "网络无连接，检查您的网络设置");

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
                        + "&opInfo.type=Login" + "&opInfo.note=" + note;
                String res = HttpRequest.request(url, content);
                System.out.println(res);
                if (res == null || res.equals("SocketTimeoutException")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_INPUT_NONE, "连接服务器超时，检查您的网络设置");
                        }
                    });

                } else if (res.equals("ConnectException") || res.equals("FileNotFoundException")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_NETWORK_EXCEPTION, "无法连接到服务器，我们将尽快修复！");
                        }
                    });
                } else {
                    Gson gson = new Gson();
                    UserInfoJSON infoJSON = gson.fromJson(res, UserInfoJSON.class);
                    final int code = infoJSON.getCode();
                    if (code != 0)
                        showErrorInfo(code);
                    else {
                        url = "http://49.140.61.67:8080/Server/getUserName";
                        res = HttpRequest.request(url, "");
                        TeaInfoJSON teaInfoJSON = gson.fromJson(res, TeaInfoJSON.class);
                        final int i = teaInfoJSON.getCode();
                        if (i == 0) {
                            ErrorCode.teaPicName = teaInfoJSON.getResArr();
                            ErrorCode.longDescription=teaInfoJSON.getLongDescription();
                            ErrorCode.shortDescription=teaInfoJSON.getShortDescription();
                            ErrorCode.name=teaInfoJSON.getName();
                            ErrorCode.activity = LoginActivity.this;
                            SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                            editor.putString("userName", infoJSON.getUserInfo().getName());
                            editor.putString("phone", infoJSON.getUserInfo().getPhone());
                            editor.apply();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_SUCCESS, null);
                                }
                            });

                        } else
                            showErrorInfo(i);
                    }

                }
            }
        }).start();

    }

    private boolean isPhoneNum(String phoneNum) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();

    }

    private void showErrorInfo(final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (code) {
                    case 201:
                        ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_FAILED, "用户名或密码错误");
                        break;
                    case 202:
                        ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_FAILED, "服务器发生异常，我们将尽快修复 code=" + 202);
                        break;
                    case 203:
                        ErrorCode.showErrorInfo(LoginActivity.this, btGo, ErrorCode.LOGIN_FAILED, "服务器发生异常，我们将尽快修复 code=" + 203);
                        break;

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
