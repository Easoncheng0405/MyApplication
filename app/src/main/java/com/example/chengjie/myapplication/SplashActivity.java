package com.example.chengjie.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import base.MyDataBase;
import base.TeaInfoJSON;
import util.ErrorCode;
import util.HttpRequest;

public class SplashActivity extends Activity {

    private static final int sleepTime = 1000;

    @Override
    protected void onCreate(Bundle arg0) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        super.onCreate(arg0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final long start = System.currentTimeMillis();
        new Thread(new Runnable() {
            public void run() {
                SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
                String name = preferences.getString("userName", "");
                String phone = preferences.getString("phone", "");
                SharedPreferences sharedPreferences = getSharedPreferences("db", MODE_PRIVATE);
                final int version=(sharedPreferences.getInt("version",0)+1);
                System.out.println(version+"******");
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("version",version);
                editor.apply();
                if (name.equals("") || phone.equals(""))
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                else {
                    String url = "http://49.140.61.67:8080/Server/getUserName";
                    final String res = HttpRequest.request(url, "");
                    if (res.equals("SocketTimeoutException")) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SplashActivity.this, "网络连接超时，检查您的网络设置", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else if (res.equals("FileNotFoundException") || res.equals("ConnectException")) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SplashActivity.this, "服务器发生异常，我们将尽快修复" + res, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Gson gson = new Gson();
                        TeaInfoJSON teaInfoJSON = gson.fromJson(res, TeaInfoJSON.class);
                        SQLiteDatabase sqLiteDatabase=new MyDataBase(SplashActivity.this,"TeaInfo.db",null,version).getWritableDatabase();
                        ContentValues contentValues=new ContentValues();
                        int length=teaInfoJSON.getName().size();
                        for(int m=0;m<length;m++){
                            contentValues.put("name",teaInfoJSON.getName().get(m));
                            contentValues.put("phone",teaInfoJSON.getPhone().get(m));
                            contentValues.put("ld",teaInfoJSON.getLongDescription().get(m));
                            contentValues.put("sd",teaInfoJSON.getShortDescription().get(m));
                            sqLiteDatabase.insert("tea",null,contentValues);
                            contentValues.clear();
                        }
                        sqLiteDatabase.close();
                        final int i = teaInfoJSON.getCode();
                        if (i == 0) {
                            Intent intent = new Intent(SplashActivity.this, TeaInfoActivity.class);
                            long costTime = System.currentTimeMillis() - start;
                            if (sleepTime - costTime > 0) {
                                try {
                                    Thread.sleep(sleepTime - costTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //进入主页面
                            if (intent != null)
                                startActivity(intent);
                            else
                                System.exit(0);
                            finish();
                        } else {
                            showErrorInfo(i);
                            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        }
                    }
                }
            }
        }).start();
    }

    private void showErrorInfo(final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (code) {
                    case 201:
                        ErrorCode.showErrorInfo(SplashActivity.this, null, ErrorCode.REGISTER_SERVER_EXCEPTION, "服务器发生异常，我们将尽快修复 code=" + 201);
                        break;
                    case 202:
                        ErrorCode.showErrorInfo(SplashActivity.this, null, ErrorCode.REGISTER_SERVER_EXCEPTION, "服务器发生异常，我们将尽快修复 code=" + 202);
                        break;
                }
            }
        });
    }
}
