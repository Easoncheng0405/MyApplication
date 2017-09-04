package com.example.chengjie.myapplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.allen.library.SuperTextView;


public class UpLoadInfo extends AppCompatActivity {
    private SuperTextView phoneView, nickNameView, emailView, addressView, collogeView, majorView, timeView, trueNameView, subjectsView, salaryView, freeTimeView;
    private SuperTextView expView, skillsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_info);
        init();
        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
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
    }

    private void init() {

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


        expView = (SuperTextView) findViewById(R.id.exp);
        skillsView = (SuperTextView) findViewById(R.id.skills);


        phoneView.setRightTextColor(-16777216);
        nickNameView.setRightTextColor(-16777216);
        emailView.setRightTextColor(-16777216);
        addressView.setRightTextColor(-16777216);
        collogeView.setRightTextColor(-16777216);
        majorView.setRightTextColor(-16777216);
        timeView.setRightTextColor(-16777216);
        trueNameView.setRightTextColor(-16777216);
    }
}
