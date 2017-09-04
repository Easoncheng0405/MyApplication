package com.example.chengjie.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.allen.library.SuperTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.HeinsDatePickerDialog;
import base.OnSelectDateListener;
import base.RxDialogEditSureCancel;


public class UpLoadInfo extends AppCompatActivity implements View.OnClickListener {
    private SuperTextView phoneView, nickNameView, emailView, addressView, collogeView, majorView, timeView, trueNameView, subjectsView, salaryView, freeTimeView;
    public static SuperTextView expView, skillsView, otherVIew;
    private View backView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_info);
        init();
        String phone = preferences.getString("phone", "");
        String name = preferences.getString("userName", "");
        String email = preferences.getString("email", "@example.com");
        String address = preferences.getString("address", "未填写");
        String colloge = preferences.getString("colloge", "未填写");
        String major = preferences.getString("major", "未填写");
        String time = preferences.getString("time", "未选择");
        String trueName = preferences.getString("trueName", "未填写");
        String subjects = preferences.getString("subjects", "未选择");
        String salary = preferences.getString("salary", "未选择");
        String freetime = preferences.getString("freeTime", "未选择");
        String exp = preferences.getString("exp", "介绍一下您的家教经验吧(50字以内)");
        String sklills = preferences.getString("skills", "介绍一下您的特长吧(50字以内)");
        String other = preferences.getString("other", "您还有其它要补充的吗(50字以内)");
        phoneView.setRightString(phone);
        nickNameView.setRightString(name);
        emailView.setRightString(email);
        addressView.setRightString(address);
        collogeView.setRightString(colloge);
        majorView.setRightString(major);
        timeView.setRightString(time);
        trueNameView.setRightString(trueName);
        subjectsView.setRightString(subjects);
        salaryView.setRightString(salary);
        freeTimeView.setRightString(freetime);
        expView.setCenterString(exp);
        skillsView.setCenterString(sklills);
        otherVIew.setCenterString(other);
    }

    private void init() {
        preferences = getSharedPreferences("userData", MODE_PRIVATE);
        editor = preferences.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setTitle("编辑个人资料");
        setSupportActionBar(toolbar);

        phoneView = (SuperTextView) findViewById(R.id.phone);
        nickNameView = (SuperTextView) findViewById(R.id.nick_name);
        emailView = (SuperTextView) findViewById(R.id.email);
        addressView = (SuperTextView) findViewById(R.id.address);
        collogeView = (SuperTextView) findViewById(R.id.colloge);
        majorView = (SuperTextView) findViewById(R.id.major);
        timeView = (SuperTextView) findViewById(R.id.time);
        trueNameView = (SuperTextView) findViewById(R.id.true_name);
        freeTimeView = (SuperTextView) findViewById(R.id.freetime);
        subjectsView = (SuperTextView) findViewById(R.id.subjects);
        salaryView = (SuperTextView) findViewById(R.id.salary);
        otherVIew = (SuperTextView) findViewById(R.id.other);

        expView = (SuperTextView) findViewById(R.id.exp);
        skillsView = (SuperTextView) findViewById(R.id.skills);

        backView = findViewById(R.id.back);

        phoneView.setRightTextColor(-16777216);
        nickNameView.setRightTextColor(-16777216);
        emailView.setRightTextColor(-16777216);
        addressView.setRightTextColor(-16777216);
        collogeView.setRightTextColor(-16777216);
        majorView.setRightTextColor(-16777216);
        timeView.setRightTextColor(-16777216);
        trueNameView.setRightTextColor(-16777216);

        backView.setOnClickListener(this);
        trueNameView.setOnClickListener(this);
        timeView.setOnClickListener(this);
        emailView.setOnClickListener(this);
        expView.setOnClickListener(this);
        skillsView.setOnClickListener(this);
        otherVIew.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.true_name:
                final RxDialogEditSureCancel dialog = new RxDialogEditSureCancel(UpLoadInfo.this);
                dialog.getTileTextView().setText("输入您的真实姓名");
                dialog.getEditText().setHint("真实姓名");
                dialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String trueName = dialog.getEditText().getText().toString();
                        if (!trueName.equals("")) {
                            editor.putString("trueName", trueName);
                            editor.apply();
                            trueNameView.setRightString(trueName);
                            dialog.cancel();
                        } else
                            Toast.makeText(UpLoadInfo.this, "输入不能为空", Toast.LENGTH_LONG).show();

                    }
                });
                dialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;
            case R.id.time:
                final HeinsDatePickerDialog datePicker = new HeinsDatePickerDialog();
                datePicker.setListener(new OnSelectDateListener() {
                    @Override
                    public void onSelectDate(Date date) throws Exception {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String myTime = format.format(date);
                        timeView.setRightString(myTime);
                        editor.putString("time", myTime);
                        editor.apply();
                    }
                });
                datePicker.show(getSupportFragmentManager(), getClass().getSimpleName());
                break;
            case R.id.email:
                final RxDialogEditSureCancel emaildialog = new RxDialogEditSureCancel(UpLoadInfo.this);
                emaildialog.getTileTextView().setText("输入您的邮箱");
                emaildialog.getEditText().setHint("someone@exmaple.com");
                emaildialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emaildialog.getEditText().getText().toString().trim();
                        if (!email.equals("")) {
                            if (!isEmail(email))
                                Toast.makeText(UpLoadInfo.this, "您输入的邮箱地址不合法", Toast.LENGTH_LONG).show();
                            else {
                                editor.putString("email", email);
                                editor.apply();
                                emailView.setRightString(email);
                                emaildialog.cancel();
                            }
                        } else
                            Toast.makeText(UpLoadInfo.this, "输入不能为空", Toast.LENGTH_LONG).show();


                    }
                });
                emaildialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        emaildialog.cancel();
                    }
                });
                emaildialog.show();
                break;
            case R.id.exp:
                startActivity(new Intent(UpLoadInfo.this, InputExp.class));
                break;
            case R.id.skills:
                startActivity(new Intent(UpLoadInfo.this, InputSkills.class));
                break;
        }
    }

    private boolean isEmail(String email) {
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }


}
