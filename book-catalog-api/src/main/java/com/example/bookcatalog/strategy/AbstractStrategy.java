package com.example.bookcatalog.strategy;

/**
 * Abstract Strategy
 * 抽象策略类
 */
public abstract class AbstractStrategy {

    /**
     * 处理逻辑 - 抽象方法
     *
     * @param data 处理数据
     * @return 处理结果
     */
    public abstract String process(String data);


    /**
     * 设置对应的观察者类型
     * @return 观察者类型
     */
    public abstract String getType();

    /**
     * 内部共通方法
     * 执行策略的通用逻辑
     *
     * @param action 动作描述
     */
    protected void doAction(String action) {
        System.out.println("Strategy executing: " + action);
    }

}
