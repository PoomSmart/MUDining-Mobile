package com.untitled.untitledapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseWorker.work(getApplicationContext());

        tv = findViewById(R.id.tvCenter);
        ClickListener cl = new ClickListener();
        tv.setOnClickListener(cl);
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), SetPreferenceActivity.class);
            startActivity(i);
        }
    }
}
