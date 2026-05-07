package com.example.bookcatalog.config;

import java.util.List;

import com.example.bookcatalog.observer.AbstractObserver;
import com.example.bookcatalog.observer.BookSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.bookcatalog.strategy.AbstractStrategy;
import com.example.bookcatalog.strategy.StrategyFactory;

@Configuration
public class BookCatalogConfiguration {

    /**
     * 配置策略工厂，初始化策略映射表
     *
     * @param strategies Spring 自动注入的所有策略实例
     * @return 初始化完成的策略工厂
     */
    @Bean
    public StrategyFactory strategyFactory(List<AbstractStrategy> strategies) {
        StrategyFactory factory = new StrategyFactory(strategies);
        // 初始化策略映射表
        factory.initStrategies(strategies);
        return factory;
    }

    @Bean
    public BookSubscriber bookSubscriber(List<AbstractObserver> observers) {
        BookSubscriber subscriber = new BookSubscriber("BookSubscriber");
        for (AbstractObserver observer : observers) {
            subscriber.add(observer);
        }
        return subscriber;
    }
}
