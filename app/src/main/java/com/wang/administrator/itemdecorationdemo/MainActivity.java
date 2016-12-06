package com.wang.administrator.itemdecorationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.wang.administrator.itemdecorationdemo.decoration.MyDecoration;
import com.wang.administrator.itemdecorationdemo.entity.CityBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "abc2", Toast.LENGTH_SHORT).show();
        String[] stringArray = getResources().getStringArray(R.array.provinces);
        List<CityBean> mDatas = new ArrayList<CityBean>();
        for (int i=0;i<stringArray.length;i++){
            CityBean cityBean = new CityBean(stringArray[i]);
            mDatas.add(cityBean);
        }
        Collections.sort(mDatas, new Comparator<CityBean>() {
            @Override
            public int compare(CityBean cityBean, CityBean t1) {
                return cityBean.getTag().compareTo(t1.getTag());
            }
        });
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_city);
        MyDecoration decoration = new MyDecoration(this,mDatas);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CityAdapter(this,mDatas));
//        for (int i=0;i<mDatas.size();i++){
//            Log.e("Tag", "onCreate: "+mDatas.get(i).getTag());
//        }
    }
}
