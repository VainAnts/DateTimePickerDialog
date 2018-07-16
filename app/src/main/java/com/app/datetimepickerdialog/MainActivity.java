package com.app.datetimepickerdialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimePickDialogUtil(MainActivity.this).dateTimePickDialog(new DateTimePickDialogUtil.OnSelectFinishedListener() {
                    @Override
                    public void selectFinished(String startTime) {
                        textView.setText(startTime);
                    }
                });
            }
        });
    }
}