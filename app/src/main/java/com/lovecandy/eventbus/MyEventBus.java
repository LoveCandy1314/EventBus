package com.lovecandy.eventbus;

import android.os.Looper;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/5/15 17:11
 * 邮箱：lichao@voole.com
 */

public class MyEventBus {
    static volatile MyEventBus defaultInstance;
    //默认的eventBusBuilder
    private static final EventBusBuilder DEFAULT_BUILDER = new EventBusBuilder();
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap<>();
    //key为方法参数对象，value为Subscription list集合
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    //key为类对象，value为 类中方法的参数对象集合
    private final Map<Object, List<Class<?>>> typesBySubscriber;
    List<SubscriberMethod> subscriberMethods;
    public static MyEventBus getDefault() {
        MyEventBus instance = defaultInstance;
        if (instance == null) {
            synchronized (MyEventBus.class) {
                instance = MyEventBus.defaultInstance;
                if (instance == null) {
                    instance = MyEventBus.defaultInstance = new MyEventBus( DEFAULT_BUILDER);
                }
            }
        }
        return instance;
    }
    private MyEventBus(EventBusBuilder builder){
        subscriptionsByEventType = new HashMap<>();
        typesBySubscriber = new HashMap<>();
        subscriberMethods = new ArrayList<>();
    }
    public void register(Object subscriber) {
        //获取到注册对象，一般为MainActivity
        Class<?> subscriberClass = subscriber.getClass();
        //这里获取到解析此对象中有Subscribe注解信息列表封装成SubscriberMethod对象
        List<SubscriberMethod> subscriberMethods = getSubscriberMethodList(subscriberClass);
        synchronized (this) {
            for (SubscriberMethod subscriberMethod : subscriberMethods) {
                subscribe(subscriber, subscriberMethod);
            }
        }
    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        //将数据以键值对方式存入subscriptionsByEventType中
        Class<?>eventType =subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions ==null){
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType,subscriptions);
        }
        subscriptions.add(new Subscription(subscriber,subscriberMethod));
    }
    public void post(Object o){
        Class<?> eventType = o.getClass();
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions!=null&&subscriptions.size()>0){
            for (Subscription subscription : subscriptions) {
                postToSubscription(subscription,o,false);
            }
        }

    }
    private void postToSubscription(final Subscription subscription, final Object event, boolean isMainThread) {
        //根据mode选择执行方式
        switch (subscription.subscriberMethod.threadMode) {
            //在当前线程执行方法
            case POSTING:
                invokeSubscriber(subscription, event);
                break;
            //如果是主线程在当前线程执行，不是主线程，通过handler方式执行
            case MAIN:
                if (isMainThread) {
                    invokeSubscriber(subscription, event);
                } else {
                    android.os.Handler handler= new android.os.Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeSubscriber(subscription, event);
                        }
                    });
                }
                break;
            //始终在子线程中执行
            case BACKGROUND:
                invokeSubscriber(subscription, event);
                break;
            //始终在另外一个新线程中执行
            case ASYNC:
                break;
            default:
                throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
        }
    }

    void invokeSubscriber(Subscription subscription, Object event) {
        try {
            //通过反射，执行具体的方法，并把参数传进去
            subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }
    private List<SubscriberMethod> getSubscriberMethodList(Class<?> subscriberClass) {
        subscriberMethods.clear();
        //获取到类中的所有方法
        Method[] methods;
        methods = subscriberClass.getDeclaredMethods();
        for (Method method : methods) {
            int modifiers= method.getModifiers();
            //当类型为public才往下执行
            if (modifiers== Modifier.PUBLIC){
                Class<?>[] parameterTypes =method.getParameterTypes();
                if (parameterTypes.length==1){
                    //获取ubscribe设置的值
                    Subscribe subscribe = method.getAnnotation(Subscribe.class);
                    if (subscribe!=null){
                       Class<?> eventType = parameterTypes[0];
                       //将对象加入到集合中
                       subscriberMethods.add(new SubscriberMethod(method,eventType,subscribe.threadMode(),subscribe.priority(),subscribe.sticky()));
                    }
                }
            }
        }
        return subscriberMethods;
    }
}
