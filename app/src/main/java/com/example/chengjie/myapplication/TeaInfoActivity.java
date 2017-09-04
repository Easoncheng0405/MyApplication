package com.example.chengjie.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.squareup.picasso.Picasso;
import com.yalantis.euclid.library.EuclidActivity;
import com.yalantis.euclid.library.EuclidListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.MyDataBase;
import ui.activity.MainActivity;
import util.FileImageUpload;
import util.SelectDialog;

/**
 * Created by chengjie on 17-8-20.
 */

public class TeaInfoActivity extends EuclidActivity {

    private Drawer drawer;
    public static ProfileDrawerItem profile;
    public static AccountHeader headerResult;
    private Context context;
    public static final String TMP_PATH = "temp.jpg";
    private String name,phone;
    private final int CAMERA_WITH_DATA = 1;
    /**
     * 本地图片选取标志
     */
    private static final int FLAG_CHOOSE_IMG = 2;
    /**
     * 截取结束标志
     */
    private static final int FLAG_MODIFY_FINISH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = TeaInfoActivity.this;
        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        name = preferences.getString("userName", "");
        phone = preferences.getString("phone", "");
        Picasso.with(this).load("http://49.140.61.67:8080/images/" + phone + ".jpg").into(back);

        profile = new ProfileDrawerItem()
                .withName(name)
                .withEmail(phone)
                .withIcon(R.drawable.fluidicon);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home).withIcon(R.drawable.ic_account_circle_black_48dp);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings).withIcon(R.drawable.ic_settings_black_48dp);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.logout).withIcon(R.drawable.ic_power_settings_new_black_48dp);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.community).withIcon(R.drawable.ic_people_black_48dp);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.feedback).withIcon(R.drawable.ic_feedback_black_48dp);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        showSelectPictureMenu();
                        drawer.closeDrawer();
                        return true;
                    }
                })
                .build();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(item1, item2, item3, item4, item5)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 1)
                            startActivity(new Intent(TeaInfoActivity.this, UpLoadInfo.class));
                        if (position == 3)
                            clear();
                        close();
                        return true;
                    }
                })
                .build();

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeaInfoActivity.this, MainActivity.class);
                intent.putExtra("teaName", teaName);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen())
                    drawer.closeDrawer();
                else
                    drawer.openDrawer();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeaInfoActivity.this, "搜索", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected BaseAdapter getAdapter() {
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("db", MODE_PRIVATE);
        int version = preferences.getInt("version", 0);
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> phone = new ArrayList<>();
        ArrayList<String> l = new ArrayList<>();
        ArrayList<String> s = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = new MyDataBase(TeaInfoActivity.this, "TeaInfo.db", null, version).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Tea", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                name.add(cursor.getString(cursor.getColumnIndex("name")));
                phone.add(cursor.getString(cursor.getColumnIndex("phone")));
                l.add(cursor.getString(cursor.getColumnIndex("ld")));
                s.add(cursor.getString(cursor.getColumnIndex("sd")));
            } while (cursor.moveToNext());
        }

        for (int i = 0; i < name.size(); i++) {
            profileMap = new HashMap<>();
            profileMap.put(EuclidListAdapter.KEY_AVATAR, "http://49.140.61.67:8080/Server/pic/" + name.get(i) + ".jpg");
            profileMap.put(EuclidListAdapter.KEY_NAME, name.get(i));
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, s.get(i));
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, l.get(i));
            profilesList.add(profileMap);
        }

        return new EuclidListAdapter(this, R.layout.list_item, profilesList);
    }

    private void close() {
        drawer.closeDrawer();
    }

    private void clear() {
        EMClient.getInstance().logout(true);
        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();
        editor1.clear();
        editor1.apply();
        startActivity(new Intent(TeaInfoActivity.this, LoginActivity.class));
        finish();
    }

    public void showSelectPictureMenu() {
        new SelectDialog(context)
                .builder()
                .setCancelable(true)
                .setTitle("请选择操作")
                .setCanceledOnTouchOutside(true)
                .addSelectItem("相机", SelectDialog.SelectItemColor.Green,
                        new SelectDialog.OnSelectItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (ContextCompat.checkSelfPermission(TeaInfoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(TeaInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                                    ActivityCompat.requestPermissions(TeaInfoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                else
                                    startCamera();
                            }
                        })
                .addSelectItem("图库", SelectDialog.SelectItemColor.Green,
                        new SelectDialog.OnSelectItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (ContextCompat.checkSelfPermission(TeaInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                                    ActivityCompat.requestPermissions(TeaInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                else

                                    startAlbum();
                            }
                        }).show();
    }

    private void startAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, FLAG_CHOOSE_IMG);
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), TMP_PATH)));
        startActivityForResult(intent, CAMERA_WITH_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    startCropImageActivity(Environment.getExternalStorageDirectory() + "/" + TMP_PATH);
                    finish();
                    break;
                case 2:
                    if (data != null) {
                        Uri uri = data.getData();
                        if (!TextUtils.isEmpty(uri.getAuthority())) {
                            Cursor cursor = getContentResolver().query(uri,
                                    new String[]{MediaStore.Images.Media.DATA},
                                    null, null, null);
                            if (null == cursor) {
                                Toast.makeText(getApplicationContext(), "图片没找到", Toast.LENGTH_LONG).show();
                                return;
                            }
                            cursor.moveToFirst();
                            String path = cursor.getString(cursor
                                    .getColumnIndex(MediaStore.Images.Media.DATA));
                            cursor.close();
                            Intent intent = new Intent(this, CutPictureAty.class);
                            intent.putExtra("path", path);
                            startActivityForResult(intent, 3);
                            finish();
                        } else {
                            Intent intent = new Intent(this, CutPictureAty.class);
                            intent.putExtra("path", uri.getPath());
                            startActivityForResult(intent, 3);
                            finish();
                        }
                    }
                    break;

            }
        }


    }

    private void startCropImageActivity(String path) {
        Intent intent = new Intent(this, CutPictureAty.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, FLAG_MODIFY_FINISH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "您拒了拍照权限，请前往设置中开启", Toast.LENGTH_LONG).show();
                else
                    startCamera();
                break;
            case 2:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "您拒了读取相册权限，请前往设置中开启", Toast.LENGTH_LONG).show();
                else
                    startAlbum();
        }
    }
}
