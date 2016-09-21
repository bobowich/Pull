package com.zy.gifdownloader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zy.xmlparser.PullBookParser;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Switch mSwitch;
    Button mButton;
    PullBookParser mPullBookParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPullBookParser = new PullBookParser();
        mSwitch = (Switch) findViewById(R.id.switch1);
        mButton = (Button) findViewById(R.id.parse);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = MainActivity.this.getAssets().open("books.xml");
                            mPullBookParser.parseBook(inputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(MainActivity.this, MainService.class));
                } else {
                    stopService(new Intent(MainActivity.this, MainService.class));
                }
            }
        });
    }
}
