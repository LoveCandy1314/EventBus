package com.lovecandy.eventbus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/5/16 09:46
 * 邮箱：lichao@voole.com
 */

public abstract class BaseActivity extends Activity {
    String TAG = "lichao";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        EventBus.getDefault().register(this);

    }

    protected abstract void setLayout();

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 100,sticky = true)
    public void test(String str){
        Log.d(TAG,"base test is " + str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
