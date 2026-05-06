package com.example.bookcatalog.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Subscriber
 * 抽象订阅者类
 */
public abstract class AbstractSubscriber {

    protected List<AbstractObserver> observers = new ArrayList<>();

    /**
     * 添加观察者
     *
     * @param observer 观察者实例
     */
    public abstract void add(AbstractObserver observer);

    /**
     * 移除观察者
     *
     * @param observer 观察者实例
     */
    public abstract void remove(AbstractObserver observer);

    /**
     * 通知所有观察者
     *
     * @param message 消息内容
     */
    public abstract void notifyAllObservers(String message);

    /**
     * 内部共通方法
     * 执行订阅者的通用逻辑
     *
     * @param action 动作描述
     */
    protected void doAction(String action) {
        System.out.println("Subscriber executing: " + action + ", Total observers: " + observers.size());
    }
}
