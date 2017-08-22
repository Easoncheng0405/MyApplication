package com.example.chengjie.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
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

import base.TeaInfoJSON;
import base.UserInfoJSON;
import util.ErrorCode;
import util.HttpRequest;


public class RegisterActivity extends Activity {


    private FloatingActionButton fab;
    private CardView cvAdd;
    private CircularProgressButton button;
    private EditText etName;
    private EditText etPhone;
    private EditText etPassWord;
    private EditText etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setProgress(0);
                String name = etName.getText().toString().trim();
                final String phone = etPhone.getText().toString();
                final String passWord = etPassWord.getText().toString();
                final String confirm = etConfirm.getText().toString();
                if (checkInput(name, phone, passWord, confirm)) {
                    if (HttpRequest.isNetworkAvailable(RegisterActivity.this)) {
                        try {
                            name = URLEncoder.encode(name, "UTF-8");
                            register(phone, name, passWord);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "网络无连接，检查您的网络设置", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void init() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cvAdd = (CardView) findViewById(R.id.cv_add);
        button = (CircularProgressButton) findViewById(R.id.bt_go);
        etName = (EditText) findViewById(R.id.et_username);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassWord = (EditText) findViewById(R.id.et_password);
        etConfirm = (EditText) findViewById(R.id.et_repeatpassword);
    }

    private void register(final String phone, final String name, final String passWord) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://49.140.61.67:8080/Server/CheckUserAlreadyExist";
                String content = "phone=" + phone + "&name=" + name;
                final String result = HttpRequest.request(url, content);
                final int code;
                if (result.equals("SocketTimeoutException")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_TIMEOUT, "连接超时，检查您的网络设置");
                        }
                    });
                } else if (result.equals("ConnectException")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_SERVER_EXCEPTION, "无法连接到服务器，我们将尽快修复！");
                        }
                    });
                } else {
                    Gson gson = new Gson();
                    UserInfoJSON infoJSON = gson.fromJson(result, UserInfoJSON.class);
                    code = infoJSON.getCode();
                    if (code == 0) {
                        url = "http://49.140.61.67:8080/Server/Register";
                        Date date = new Date();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format.format(date);
                        content = "info.name=" + name + "&info.phone=" + phone + "&info.password=" + passWord + "&info.type=" + 0 + "&info.signTime=" + time;
                        String res = HttpRequest.request(url, content);
                        final int i;
                        if (res.equals("SocketTimeoutException"))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_TIMEOUT, "连接超时，检查您的网络设置");
                                }
                            });
                        else if (res.equals("ConnectException"))
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_SERVER_EXCEPTION, "无法连接到服务器，我们将尽快修复！");
                                }
                            });
                        else {
                            infoJSON = gson.fromJson(res, UserInfoJSON.class);
                            i = infoJSON.getCode();
                            if (i == 0) {
                                url = "http://49.140.61.67:8080/Server/getUserName";
                                res = HttpRequest.request(url, "");
                                TeaInfoJSON teaInfoJSON = gson.fromJson(res, TeaInfoJSON.class);
                                final int n=teaInfoJSON.getCode();
                                if(n==0){
                                    ErrorCode.extra=teaInfoJSON.getResArr();
                                    ErrorCode.activity=RegisterActivity.this;
                                    SharedPreferences.Editor editor=getSharedPreferences("userData",MODE_PRIVATE).edit();
                                    editor.putString("userName",infoJSON.getUserInfo().getName());
                                    editor.putString("phone",infoJSON.getUserInfo().getPhone());
                                    editor.apply();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ErrorCode.showErrorInfo(RegisterActivity.this,button,ErrorCode.LOGIN_SUCCESS,null);
                                        }
                                    });

                                }else
                                    showErrorInfo(n);
                            } else
                                showErrorInfo(i);
                        }

                    } else if (code == 101) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_USERNAME_EXIST, "用户名已存在");
                            }
                        });
                    } else if (code == 102) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_USERNAME_EXIST, "电话号已存在");
                            }
                        });
                    }
                }

            }
        }).start();
    }


    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    private boolean checkInput(String name, String phone, String password, String confirm) {
        if (name.equals("") || phone.equals("") || password.equals("") || confirm.equals("")) {
            ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_INPUT_NONE, "输入不能为空");
            return false;
        }
        if (!password.equals(confirm)) {
            ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_INPUT_NONE, "您输入的密码不一致");
            return false;
        }
        if (!isLegalName(name)) {
            ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_INPUT_NONE, "              您输入的用户名不合法\n(由中英字符开头且只能包含中英字符与数字)");
            return false;
        }
        if (!isPhoneNum(phone)) {
            ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_INPUT_NONE, "您输入的电话号码不存在");
            return false;
        }
        return true;
    }

    private boolean isLegalName(String name) {
        Pattern p = Pattern
                .compile("^[\\u4e00-\\u9fa5a-zA-Z][\\u4e00-\\u9fa5a-zA-Z]+$");
        Matcher m = p.matcher(name);
        System.out.println(m.matches() + "---");
        return m.matches();

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
                        ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_SERVER_EXCEPTION, "服务器发生异常，我们将尽快修复 code=" + 201);
                        break;
                    case 202:
                        ErrorCode.showErrorInfo(RegisterActivity.this, button, ErrorCode.REGISTER_SERVER_EXCEPTION, "服务器发生异常，我们将尽快修复 code=" + 202);
                        break;
                }
            }
        });
    }
}


