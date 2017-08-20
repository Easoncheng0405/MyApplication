package com.example.chengjie.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
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

import base.UserInfoJSON;
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
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
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
                String name= etName.getText().toString().trim();
                final String phone=etPhone.getText().toString();
                final String passWord=etPassWord.getText().toString();
                final String confirm=etConfirm.getText().toString();
                int res=checkInput(name,phone,passWord,confirm);
                if(res!=0)
                    setButtonProgress(button,res);
                else {
                    try {
                        name=URLEncoder.encode(name,"UTF-8");
                        register(phone, name, passWord);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void init(){
        fab=(FloatingActionButton)findViewById(R.id.fab);
        cvAdd=(CardView)findViewById(R.id.cv_add);
        button=(CircularProgressButton)findViewById(R.id.bt_go);
        etName=(EditText)findViewById(R.id.et_username);
        etPhone=(EditText)findViewById(R.id.et_phone);
        etPassWord=(EditText)findViewById(R.id.et_password);
        etConfirm=(EditText)findViewById(R.id.et_repeatpassword);
    }

    private void register(final String phone,final String name,final String passWord){
        int res=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://49.140.61.67:8080/Server/CheckUserAlreadyExist";
                String content = "phone="+phone+"&name="+name;
                String result = HttpRequest.request(url, content);
                Gson gson = new Gson();
                UserInfoJSON infoJSON = gson.fromJson(result, UserInfoJSON.class);
                final int code=infoJSON.getCode();
                if (code == 0) {
                    url="http://49.140.61.67:8080/Server/Register";
                    Date date=new Date();
                    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time=format.format(date);
                    content="info.name="+name+"&info.phone="+phone+"&info.password="+passWord+"&info.type="+0+"&info.signTime="+time;
                    result=HttpRequest.request(url,content);
                    infoJSON=gson.fromJson(result,UserInfoJSON.class);
                    final int i=infoJSON.getCode();
                    if(i==0)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setButtonProgress(button,0);
                            }
                        });
                } else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setButtonProgress(button,code);
                        }
                    });
                }
            }
        }).start();
    }

    private void setButtonProgress(final CircularProgressButton button,int code){
        if(code==0){
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
            Toast.makeText(RegisterActivity.this,"注册成功，即将跳转",Toast.LENGTH_SHORT).show();
        }else{
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
            if(code==101)
                Toast.makeText(RegisterActivity.this,"注册失败，用户名已存在",Toast.LENGTH_LONG).show();
            else if(code ==102)
                Toast.makeText(RegisterActivity.this,"注册失败，电话号已注册",Toast.LENGTH_LONG).show();
            else if(code ==1)
                Toast.makeText(RegisterActivity.this,"输入不能为空",Toast.LENGTH_LONG).show();
            else if(code ==2)
                Toast.makeText(RegisterActivity.this,"您输入的密码不一致",Toast.LENGTH_LONG).show();
            else if(code ==3)
                Toast.makeText(RegisterActivity.this,"          您输入的用户名不合法\n(中英文字符组成，最少两个字符)",Toast.LENGTH_LONG).show();
            else if(code ==4)
                Toast.makeText(RegisterActivity.this,"您输入的电话号号码不合法",Toast.LENGTH_LONG).show();

        }

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
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
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
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
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

    private int checkInput(String name,String phone,String password,String confirm){
        if(name.equals("")||phone.equals("")||password.equals("")||confirm.equals(""))
            return 1;
        if(!password.equals(confirm))
            return 2;
        if(!isLegalName(name))
            return 3;
        if(!isPhoneNum(phone))
            return 4;
        return 0;
    }
    private boolean isLegalName(String name){
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
}


