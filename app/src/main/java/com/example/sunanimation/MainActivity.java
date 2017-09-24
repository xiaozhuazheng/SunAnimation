package com.example.sunanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mET;
    private Button mBegin;
    private SunView mSunView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mET = (EditText) findViewById(R.id.time_et);
        mBegin = (Button) findViewById(R.id.set_bt);
        mSunView = (SunView) findViewById(R.id.sun);
        mBegin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_bt:
                String time = String.valueOf(mET.getText());
                mSunView.setTime(time);
                break;
        }
    }
}
