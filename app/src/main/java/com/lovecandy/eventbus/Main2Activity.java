package com.lovecandy.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Main2Activity extends AppCompatActivity {
    String TAG = "lichao";
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button = findViewById(R.id.button);
        EventBus.getDefault().register(this);

    }

    public void doClick(View view) {
        new Thread(){
            @Override
            public void run() {
                EventBus.getDefault().post("2222222222");
            }
        }.start();
    }
    @Subscribe(threadMode = ThreadMode.MAIN,priority = 100,sticky = true)
    public void test(String str){
        Log.d(TAG,"main2  test is " + str);
        button.setText(str);
    }
}
