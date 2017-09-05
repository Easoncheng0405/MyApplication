package com.example.chengjie.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.amap.api.services.nearby.UploadInfo;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.HeinsDatePickerDialog;
import base.OnSelectDateListener;
import base.RxDialogEditSureCancel;
import util.HttpRequest;


public class UpLoadInfo extends AppCompatActivity implements View.OnClickListener {
    private SuperTextView phoneView, nickNameView, emailView, addressView, collegeView, majorView, timeView, trueNameView, subjectsView, salaryView, freeTimeView;
    public static SuperTextView expView, skillsView, otherVIew;
    private View backView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int subjectsRes;
    private int[] subjectsSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_info);
        init();
        String phone = preferences.getString("phone", "");
        String name = preferences.getString("userName", "");
        String email = preferences.getString("email", "@example.com");
        String address = preferences.getString("address", "未填写");
        String college = preferences.getString("college", "未填写");
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
        collegeView.setRightString(college);
        majorView.setRightString(major);
        timeView.setRightString(time);
        trueNameView.setRightString(trueName);
        subjectsView.setRightString(getShowRes(subjects));
        salaryView.setRightString(salary);
        freeTimeView.setRightString(freetime);
        expView.setCenterString(exp);
        skillsView.setCenterString(sklills);
        otherVIew.setCenterString(other);
    }

    private void init() {

        subjectsRes=0;
        subjectsSelect=new int[10];
        for(int i=0;i<10;i++)
            subjectsSelect[i]=0;
        preferences = getSharedPreferences("userData", MODE_PRIVATE);
        editor = preferences.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setTitle("编辑个人资料");
        setSupportActionBar(toolbar);

        phoneView = (SuperTextView) findViewById(R.id.phone);
        nickNameView = (SuperTextView) findViewById(R.id.nick_name);
        emailView = (SuperTextView) findViewById(R.id.email);
        addressView = (SuperTextView) findViewById(R.id.address);
        collegeView = (SuperTextView) findViewById(R.id.college);
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
        collegeView.setRightTextColor(-16777216);
        majorView.setRightTextColor(-16777216);
        timeView.setRightTextColor(-16777216);
        trueNameView.setRightTextColor(-16777216);

        backView.setOnClickListener(this);
        trueNameView.setOnClickListener(this);
        addressView.setOnClickListener(this);
        timeView.setOnClickListener(this);
        collegeView.setOnClickListener(this);
        majorView.setOnClickListener(this);
        emailView.setOnClickListener(this);
        subjectsView.setOnClickListener(this);
        salaryView.setOnClickListener(this);
        freeTimeView.setOnClickListener(this);
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
            case R.id.address:
                final RxDialogEditSureCancel addressDialog = new RxDialogEditSureCancel(UpLoadInfo.this);
                addressDialog.getTileTextView().setText("输入您的住址信息");
                addressDialog.getEditText().setHint("住址");
                addressDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = addressDialog.getEditText().getText().toString();
                        if (!address.equals("")) {
                            editor.putString("address", address);
                            editor.apply();
                            addressView.setRightString(address);
                            addressDialog.cancel();
                        } else
                            Toast.makeText(UpLoadInfo.this, "输入不能为空", Toast.LENGTH_LONG).show();

                    }
                });
                addressDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addressDialog.cancel();
                    }
                });
                addressDialog.show();
                break;
            case R.id.college:
                final RxDialogEditSureCancel collegeDialog = new RxDialogEditSureCancel(UpLoadInfo.this);
                collegeDialog.getTileTextView().setText("输入您所在的大学");
                collegeDialog.getEditText().setHint("大学");
                collegeDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String college = collegeDialog.getEditText().getText().toString();
                        if (!college.equals("")) {
                            editor.putString("college", college);
                            editor.apply();
                            collegeView.setRightString(college);
                            collegeDialog.cancel();
                        } else
                            Toast.makeText(UpLoadInfo.this, "输入不能为空", Toast.LENGTH_LONG).show();

                    }
                });
                collegeDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collegeDialog.cancel();
                    }
                });
                collegeDialog.show();
                break;
            case R.id.major:
                final RxDialogEditSureCancel majorDialog = new RxDialogEditSureCancel(UpLoadInfo.this);
                majorDialog.getTileTextView().setText("输入您所在的专业");
                majorDialog.getEditText().setHint("专业");
                majorDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String major = majorDialog.getEditText().getText().toString();
                        if (!major.equals("")) {
                            editor.putString("major", major);
                            editor.apply();
                            majorView.setRightString(major);
                            majorDialog.cancel();
                        } else
                            Toast.makeText(UpLoadInfo.this, "输入不能为空", Toast.LENGTH_LONG).show();

                    }
                });
                majorDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        majorDialog.cancel();
                    }
                });
                majorDialog.show();
                break;
            case R.id.salary:
                final RxDialogEditSureCancel salaryDialog = new RxDialogEditSureCancel(UpLoadInfo.this);
                salaryDialog.getTileTextView().setText("输入您的预期薪水");
                salaryDialog.getEditText().setHint("例如：不小于50每小时");
                salaryDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String salary = salaryDialog.getEditText().getText().toString();
                        if (!salary.equals("")) {
                            editor.putString("salary", salary);
                            editor.apply();
                            salaryView.setRightString(salary);
                            salaryDialog.cancel();
                        } else
                            Toast.makeText(UpLoadInfo.this, "输入不能为空", Toast.LENGTH_LONG).show();

                    }
                });
                salaryDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        salaryDialog.cancel();
                    }
                });
                salaryDialog.show();
                break;
            case R.id.freetime:
                final RxDialogEditSureCancel freetimeDialog = new RxDialogEditSureCancel(UpLoadInfo.this);
                freetimeDialog.getTileTextView().setText("输入您的空闲时间");
                freetimeDialog.getEditText().setHint("空闲时间");
                freetimeDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String freetime = freetimeDialog.getEditText().getText().toString();
                        if (!freetime.equals("")) {
                            editor.putString("freeTime", freetime);
                            editor.apply();
                            freeTimeView.setRightString(freetime);
                            freetimeDialog.cancel();
                        } else
                            Toast.makeText(UpLoadInfo.this, "输入不能为空", Toast.LENGTH_LONG).show();

                    }
                });
                freetimeDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        freetimeDialog.cancel();
                    }
                });
                freetimeDialog.show();
                break;
            case R.id.exp:
                startActivity(new Intent(UpLoadInfo.this, InputExp.class));
                break;
            case R.id.skills:
                startActivity(new Intent(UpLoadInfo.this, InputSkills.class));
                break;
            case R.id.other:
                startActivity(new Intent(UpLoadInfo.this,InputOther.class));
                break;
            case R.id.subjects:
                subjectsRes=0;
                for(int i=0;i<10;i++)
                    subjectsSelect[i]=0;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择您的科目");
                final String[] items = new String[]{"语文", "数学", "英语","物理","化学","生物","政治","历史","地理","其它"};/*设置多选的内容*/
                final boolean[] checkedItems = new boolean[]{false, false, false,false, false, false,false, false, false,false};/*设置多选默认状态*/
                builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {/*设置多选的点击事件*/
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked)
                                subjectsSelect[which]=1;
                            else
                                subjectsSelect[which]=0;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<10;i++)
                            subjectsRes=(int)(subjectsRes+subjectsSelect[i]*Math.pow(2,9-i));
                        String res=Integer.toBinaryString(subjectsRes);
                        String temp="";
                        for(int i=0;i<10-res.length();i++)
                            temp+="0";
                        res=temp+res;
                        editor.putString("subjects",res);
                        editor.apply();
                        subjectsView.setRightString(getShowRes(res));
                    }

                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setCancelable(false);
                builder.show();
        }
    }

    private boolean isEmail(String email) {
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    private String getShowRes(String res){
        String show="";
        for(int i=0;i<10;i++){
            char a=res.charAt(i);
            if(a=='1')
                show=show+getSubjects(i);
        }
        return show;
    }

    private String getSubjects(int i){
        switch (i){
            case 0:
                return "语 ";
            case 1:
                return "数 ";
            case 2:
                return "英 ";
            case 3:
                return "物 ";
            case 4:
                return "化 ";
            case 5:
                return "生 ";
            case 6:
                return "政 ";
            case 7:
                return "历 ";
            case 8:
                return "地 ";
            case 9:
                return "其它";
        }
        return "";
    }

    @Override
    public void onBackPressed() {
            upLoadInfo();
            finish();
    }

    private void upLoadInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String phone = preferences.getString("phone", "null");
                String name = preferences.getString("userName", "null");
                String email = preferences.getString("email", "null");
                String address = preferences.getString("address", "null");//
                String college = preferences.getString("college", "null");//
                String major = preferences.getString("major", "null");//
                String time = preferences.getString("time", "null");
                String trueName = preferences.getString("trueName", "null");//
                String subjects = preferences.getString("subjects", "null");
                String salary = preferences.getString("salary", "null");//
                String freetime = preferences.getString("freeTime", "null");//
                String exp = preferences.getString("exp", "null");//
                String sklills = preferences.getString("skills", "null");//
                String other = preferences.getString("other", "null");//
                try {
                    address= URLEncoder.encode(address,"UTF-8");
                    college= URLEncoder.encode(college,"UTF-8");
                    major= URLEncoder.encode(major,"UTF-8");
                    sklills= URLEncoder.encode(sklills,"UTF-8");
                    trueName= URLEncoder.encode(trueName,"UTF-8");
                    other= URLEncoder.encode(other,"UTF-8");
                    salary= URLEncoder.encode(salary,"UTF-8");
                    freetime= URLEncoder.encode(freetime,"UTF-8");
                    exp= URLEncoder.encode(exp,"UTF-8");

                } catch (UnsupportedEncodingException e) {

                }
            }
        }).start();
    }
}
