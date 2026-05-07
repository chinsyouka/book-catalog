package com.example.bookcatalog.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Strategy Factory
 * 策略工厂类
 */
public class StrategyFactory {

    /**
     * 策略映射表
     * key: 观察者的 getType() 值
     * value: 策略类实例
     */
    private final Map<String, AbstractStrategy> strategyMap = new HashMap<>();

    /**
     * 构造函数，通过 Spring 自动注入所有策略实例
     *
     * @param strategies 所有 AbstractStrategy 的子类实例列表
     */
    public StrategyFactory(List<AbstractStrategy> strategies) {
        // 注意：这里只是保存引用，实际初始化在 BookCatalogConfiguration 中完成
        // 这个构造函数确保 Spring 能够注入所有策略实例
    }

    /**
     * 初始化策略映射表
     * 由 BookCatalogConfiguration 调用
     *
     * @param strategies 所有策略实例列表
     */
    public void initStrategies(List<AbstractStrategy> strategies) {
        for (AbstractStrategy strategy : strategies) {
            // 根据策略类名推断对应的 Observer 类型
            String observerType = strategy.getType();
            strategyMap.put(observerType, strategy);
        }
        System.out.println("StrategyFactory initialized with " + strategyMap.size() + " strategies");
    }

    /**
     * 根据观察者类型获取对应的策略
     *
     * @param observerType 观察者类型（即 AbstractObserver.getType() 的返回值）
     * @return 对应的策略实例
     */
    public AbstractStrategy getStrategy(String observerType) {
        AbstractStrategy strategy = strategyMap.get(observerType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for observer type: " + observerType);
        }
        return strategy;
    }

    /**
     * 获取所有已注册的策略
     *
     * @return 策略映射表
     */
    public Map<String, AbstractStrategy> getAllStrategies() {
        return new HashMap<>(strategyMap);
    }
}
