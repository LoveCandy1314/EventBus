package com.lovecandy.eventbus;

import org.greenrobot.eventbus.Logger;
import org.greenrobot.eventbus.MainThreadSupport;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/5/15 17:12
 * 邮箱：lichao@voole.com
 */

public class EventBusBuilder {
    private final static ExecutorService DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    boolean logSubscriberExceptions = true;
    boolean logNoSubscriberMessages = true;
    boolean sendSubscriberExceptionEvent = true;
    boolean sendNoSubscriberEvent = true;
    boolean throwSubscriberException;
    boolean eventInheritance = true;
    boolean ignoreGeneratedIndex;
    boolean strictMethodVerification;
    ExecutorService executorService = DEFAULT_EXECUTOR_SERVICE;
    List<Class<?>> skipMethodVerificationForClasses;
    List<SubscriberInfoIndex> subscriberInfoIndexes;
    Logger logger;
    MainThreadSupport mainThreadSupport;

    EventBusBuilder() {
    }
}
