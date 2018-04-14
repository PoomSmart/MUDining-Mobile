package com.untitled.untitledapk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class RecommendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        final Button button = findViewById(R.id.rec);
        button.setOnClickListener(v -> startActivity(new Intent(RecommendActivity.this, RecommendListActivity.class)));
    }
}
