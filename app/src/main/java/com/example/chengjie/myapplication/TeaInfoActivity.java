package com.example.chengjie.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.yalantis.euclid.library.EuclidActivity;
import com.yalantis.euclid.library.EuclidListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengjie on 17-8-20.
 */

public class TeaInfoActivity extends EuclidActivity  {

    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        String name = preferences.getString("userName", "");
        String phone = preferences.getString("phone", "");
        ProfileDrawerItem profile = new ProfileDrawerItem()
                .withName(name)
                .withEmail(phone)
                .withIcon(R.drawable.fluidicon);

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home).withIcon(R.drawable.ic_account_circle_black_48dp);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings).withIcon(R.drawable.ic_build_black_48dp);
        PrimaryDrawerItem item4=new PrimaryDrawerItem().withIdentifier(3).withName(R.string.logout).withIcon(R.drawable.ic_power_settings_new_black_48dp);
        PrimaryDrawerItem item3=new PrimaryDrawerItem().withIdentifier(4).withName(R.string.community).withIcon(R.drawable.ic_people_black_48dp);
        PrimaryDrawerItem item5=new PrimaryDrawerItem().withIdentifier(5).withName(R.string.feedback).withIcon(R.drawable.ic_feedback_black_48dp);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .build();
        drawer=new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(item1, item2,item3,item4,item5)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(position==4)
                            clear();
                        close();
                        return true;
                    }
                })
                .build();

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TeaInfoActivity.this, "Oh hi!", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen())
                    drawer.closeDrawer();
                else
                    drawer.openDrawer();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeaInfoActivity.this,"搜索",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected BaseAdapter getAdapter() {
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();
        ArrayList<String> res=getIntent().getStringArrayListExtra("res");
        ArrayList<String> l=getIntent().getStringArrayListExtra("long");
        ArrayList<String> s=getIntent().getStringArrayListExtra("short");
        ArrayList<String> name=getIntent().getStringArrayListExtra("name");
        for (int i = 0; i < res.size(); i++) {
            profileMap = new HashMap<>();
            profileMap.put(EuclidListAdapter.KEY_AVATAR, "http://49.140.61.67:8080/Server/pic/"+res.get(i));
            profileMap.put(EuclidListAdapter.KEY_NAME, name.get(i));
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, s.get(i));
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, l.get(i));
            profilesList.add(profileMap);
        }

        return new EuclidListAdapter(this, R.layout.list_item, profilesList);
    }

    private void close(){
        drawer.closeDrawer();
    }

    private void clear(){
        SharedPreferences.Editor editor=getSharedPreferences("userData",MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(TeaInfoActivity.this,LoginActivity.class));
        finish();
    }
}
