package com.example.chengjie.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import util.HttpRequest;

public class UpLoadInfo extends AppCompatActivity {
    private EditText name,sex,phone,age,colloge,grade,major,subjects,salary,skills,exp,judge,other;
    private Button button;
    private String _name,_phone,_colloge,_grade,_major,_skills,_exp,_other;
    private float _judge;
    private int _sex,_age,_subjects,_salary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_info);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initInfo();
                final String url="http://49.140.61.67:8080/Server/upLoadTeaInfo";
                final String content="name="+_name+"&sex="+_sex+"&phone="+_phone+"&age="+_age+"&grade="+_grade+"&major="+_major+"&subjects="+_subjects+"&salary="+_salary+"&skills="+_skills+"&exp="+_exp+"&judge="+_judge+"&other="+_other+"&colloge="+_colloge;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String res=HttpRequest.request(url,content);
                        System.out.println(_subjects);
                        System.out.println(res);
                    }
                }).start();
            }
        });


    }
    private void init(){
        name=(EditText)findViewById(R.id.name);
        sex=(EditText)findViewById(R.id.sex);
        phone=(EditText)findViewById(R.id.phone);
        age=(EditText)findViewById(R.id.age);
        colloge=(EditText)findViewById(R.id.colloge);
        grade=(EditText)findViewById(R.id.grade);
        major=(EditText)findViewById(R.id.major);
        subjects=(EditText)findViewById(R.id.subjects);
        salary=(EditText)findViewById(R.id.salary);
        skills=(EditText)findViewById(R.id.skills);
        exp=(EditText)findViewById(R.id.exp);
        judge=(EditText)findViewById(R.id.judge);
        other=(EditText)findViewById(R.id.other);
        button=(Button)findViewById(R.id.ok);
    }

    private void initInfo(){
        _sex=Integer.valueOf(sex.getText().toString());
        _age=Integer.valueOf(age.getText().toString());
        _subjects=Integer.valueOf(subjects.getText().toString());
        _salary=Integer.valueOf(salary.getText().toString());
        _judge=Float.valueOf(judge.getText().toString());


        _name=name.getText().toString();
        _phone=phone.getText().toString();
        _colloge=colloge.getText().toString();
        _grade=grade.getText().toString();
        _major=major.getText().toString();
        _skills=skills.getText().toString();
        _exp=exp.getText().toString();
        _other=other.getText().toString();
        try {
            _name= URLEncoder.encode(_name,"UTF-8");
            _phone=URLEncoder.encode(_phone,"UTF-8");
            _colloge=URLEncoder.encode(_colloge,"UTF-8");;
            _grade=URLEncoder.encode(_grade,"UTF-8");
            _major=URLEncoder.encode(_major,"UTF-8");
            _skills=URLEncoder.encode(_skills,"UTF-8");
            _exp=URLEncoder.encode(_exp,"UTF-8");
            _other=URLEncoder.encode(_other,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
