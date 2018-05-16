package com.lovecandy.eventbus;

import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/5/15 17:50
 * 邮箱：lichao@voole.com
 */

public class SubscriberMethod {
    public final Method method;
    public final ThreadMode threadMode;
    public final Class<?> eventType;
    public final int priority;
    public final boolean sticky;
    /** Used for efficient comparison */
    String methodString;

    public SubscriberMethod(Method method, Class<?> eventType, ThreadMode threadMode, int priority, boolean sticky) {
        //方法对象
        this.method = method;
        //方法上的threadMode
        this.threadMode = threadMode;
        //方法中参数类(String)
        this.eventType = eventType;
        //方法上priority，优先级
        this.priority = priority;
        //方法上sticky，粘性
        this.sticky = sticky;
    }
}
