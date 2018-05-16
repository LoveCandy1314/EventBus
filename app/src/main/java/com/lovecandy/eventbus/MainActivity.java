package com.lovecandy.eventbus;

import android.content.Intent;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseActivity {



    public void doClick(View view) {
        Intent intent =new Intent(this,Main2Activity.class);
        startActivity(intent);
    }

    @Override
    public void test(String str) {
//        super.test(str);
        Log.d(TAG,"main test is " + str);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }
}
