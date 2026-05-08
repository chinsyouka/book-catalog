package com.example.bookcatalog.observer;

import com.example.bookcatalog.enums.ObserverTypeEnum;
import com.example.bookcatalog.strategy.AbstractStrategy;
import com.example.bookcatalog.strategy.StrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * BookObserver Test
 */
@ExtendWith(MockitoExtension.class)
class BookObserverTest {

    @Mock
    private StrategyFactory strategyFactory;

    @Mock
    private AbstractStrategy strategy;

    private BookObserver bookObserver;

    @BeforeEach
    void setUp() throws Exception {
        bookObserver = new BookObserver();
        
        // 通过反射注入 mock 的 StrategyFactory
        Field field = AbstractObserver.class.getDeclaredField("strategyFactory");
        field.setAccessible(true);
        field.set(bookObserver, strategyFactory);
        
        // 配置 mock 行为
        when(strategyFactory.getStrategy(anyString())).thenReturn(strategy);
        when(strategy.process(anyString())).thenReturn("processed");
    }

    /**
     * 测试场景：验证 BookObserver 的 getType 方法返回正确的观察者类型
     */
    @Test
    void testGetType_ShouldReturnCorrectType() {
        // When
        String type = bookObserver.getType();

        // Then
        assertEquals(ObserverTypeEnum.type1.name(), type);
    }

    /**
     * 测试场景：验证 update 方法能正确记录日志并调用策略处理消息
     */
    @Test
    void testUpdate_ShouldLogAndProcessMessage() {
        // Given
        String message = "Test message";

        // When
        bookObserver.update(message);

        // Then
        verify(strategyFactory, times(1)).getStrategy(ObserverTypeEnum.type1.name());
        verify(strategy, times(1)).process(anyString());
    }

    /**
     * 测试场景：验证多次调用 update 方法时每次都能正确处理
     */
    @Test
    void testUpdate_WithDifferentMessages_ShouldProcessEach() {
        // Given
        String message1 = "Message 1";
        String message2 = "Message 2";

        // When
        bookObserver.update(message1);
        bookObserver.update(message2);

        // Then
        verify(strategyFactory, times(2)).getStrategy(ObserverTypeEnum.type1.name());
        verify(strategy, times(2)).process(anyString());
    }

    /**
     * 测试场景：验证 doAction 内部方法能正确调用策略的 process 方法
     */
    @Test
    void testDoAction_ShouldCallStrategyProcess() {
        // Given
        String action = "Test action";

        // When & Then
        // doAction is protected, called internally by update
        bookObserver.update("test");
        
        verify(strategy, atLeastOnce()).process(anyString());
    }
}
