package com.lovecandy.eventbus;


/**
 * @author Created by lichao
 * @desc
 * @time 2018/5/15 17:26
 * 邮箱：lichao@voole.com
 */

public class Subscription {
    final Object subscriber;
    final SubscriberMethod subscriberMethod;
    volatile boolean active;

    Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        //主类Mactivity
        this.subscriber = subscriber;
        //类中对应的对象
        this.subscriberMethod = subscriberMethod;
        active = true;
    }
}
