package com.example.bookcatalog.observer;

/**
 * Abstract Observer
 * 抽象观察者类
 */
public abstract class AbstractObserver {

    /**
     * 更新方法 - 当被观察对象状态改变时调用
     *
     * @param message 消息内容
     */
    public abstract void update(String message);

    /**
     * 获取观察者类型
     *
     * @return 观察者类型
     */
    public abstract String getType();

    /**
     * 内部共通方法
     * 执行观察者的通用逻辑
     *
     * @param action 动作描述
     */
    protected void doAction(String action) {
        System.out.println("Observer [" + getType() + "] executing: " + action);
    }
}
