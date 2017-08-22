package com.example.chengjie.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.yalantis.euclid.library.EuclidActivity;
import com.yalantis.euclid.library.EuclidListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengjie on 17-8-20.
 */

public class MainActivity extends EuclidActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Oh hi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected BaseAdapter getAdapter() {
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();
        ArrayList<String> res=getIntent().getStringArrayListExtra("res");
        String[] list=new String[res.size()];
        String[] names = getResources().getStringArray(R.array.array_names);

        for (int i = 0; i < list.length; i++) {
            profileMap = new HashMap<>();
            list[i]="http://49.140.61.67:8080/Server/pic/"+res.get(i);
            profileMap.put(EuclidListAdapter.KEY_AVATAR, list[i]);
            profileMap.put(EuclidListAdapter.KEY_NAME, names[i]);
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, getString(R.string.lorem_ipsum_short));
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, getString(R.string.lorem_ipsum_long));
            profilesList.add(profileMap);
        }

        return new EuclidListAdapter(this, R.layout.list_item, profilesList);
    }
}
