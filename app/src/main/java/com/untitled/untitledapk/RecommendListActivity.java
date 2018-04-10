package com.untitled.untitledapk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecommendListActivity extends AppCompatActivity {

    ListView lv;
    String[] name = {"Amy", "John","DD","Frank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);

        lv = (ListView) findViewById(R.id.restaurant);
        lv.setAdapter(new ArrayAdapter<String>(RecommendListActivity.this, android.R.layout.simple_list_item_1,name));
    }
}
