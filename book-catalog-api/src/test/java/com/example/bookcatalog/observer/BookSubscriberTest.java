package com.example.bookcatalog.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BookSubscriber Test
 */
class BookSubscriberTest {

    private BookSubscriber bookSubscriber;
    private BookObserver observer1;
    private BookObserver observer2;

    @BeforeEach
    void setUp() {
        bookSubscriber = new BookSubscriber("TestSubscriber");
        observer1 = new BookObserver();
        observer2 = new BookObserver();
    }

    /**
     * 测试场景：验证添加单个观察者到订阅者列表
     */
    @Test
    void testAdd_ShouldAddObserverToList() {
        // When
        bookSubscriber.add(observer1);

        // Then
        assertEquals(1, bookSubscriber.observers.size());
        assertTrue(bookSubscriber.observers.contains(observer1));
    }

    /**
     * 测试场景：验证添加多个观察者到订阅者列表
     */
    @Test
    void testAdd_MultipleObservers_ShouldAddAll() {
        // When
        bookSubscriber.add(observer1);
        bookSubscriber.add(observer2);

        // Then
        assertEquals(2, bookSubscriber.observers.size());
        assertTrue(bookSubscriber.observers.contains(observer1));
        assertTrue(bookSubscriber.observers.contains(observer2));
    }

    /**
     * 测试场景：验证从订阅者列表中移除指定的观察者
     */
    @Test
    void testRemove_ShouldRemoveObserverFromList() {
        // Given
        bookSubscriber.add(observer1);
        bookSubscriber.add(observer2);

        // When
        bookSubscriber.remove(observer1);

        // Then
        assertEquals(1, bookSubscriber.observers.size());
        assertFalse(bookSubscriber.observers.contains(observer1));
        assertTrue(bookSubscriber.observers.contains(observer2));
    }

    /**
     * 测试场景：验证移除不存在的观察者时列表不受影响
     */
    @Test
    void testRemove_NonExistentObserver_ShouldNotChangeList() {
        // Given
        bookSubscriber.add(observer1);

        // When
        bookSubscriber.remove(observer2);

        // Then
        assertEquals(1, bookSubscriber.observers.size());
        assertTrue(bookSubscriber.observers.contains(observer1));
    }

    /**
     * 测试场景：验证从空列表中移除观察者时不会抛出异常
     */
    @Test
    void testRemove_FromEmptyList_ShouldNotThrowException() {
        // When & Then
        assertDoesNotThrow(() -> bookSubscriber.remove(observer1));
        assertEquals(0, bookSubscriber.observers.size());
    }

    /**
     * 测试场景：验证通知单个观察者时能正确调用其 update 方法
     */
    @Test
    void testNotifyAllObservers_WithOneObserver_ShouldCallUpdate() {
        // Given
        TestObserver testObserver = new TestObserver();
        bookSubscriber.add(testObserver);
        String message = "Test message";

        // When
        bookSubscriber.notifyAllObservers(message);

        // Then
        assertEquals(1, testObserver.getUpdateCount());
        assertEquals(message, testObserver.getLastMessage());
    }

    /**
     * 测试场景：验证通知多个观察者时能正确调用所有观察者的 update 方法
     */
    @Test
    void testNotifyAllObservers_WithMultipleObservers_ShouldCallUpdateOnAll() {
        // Given
        TestObserver testObserver1 = new TestObserver();
        TestObserver testObserver2 = new TestObserver();
        bookSubscriber.add(testObserver1);
        bookSubscriber.add(testObserver2);
        String message = "Test message";

        // When
        bookSubscriber.notifyAllObservers(message);

        // Then
        assertEquals(1, testObserver1.getUpdateCount());
        assertEquals(1, testObserver2.getUpdateCount());
        assertEquals(message, testObserver1.getLastMessage());
        assertEquals(message, testObserver2.getLastMessage());
    }

    /**
     * 测试场景：验证当没有观察者时通知操作不会抛出异常
     */
    @Test
    void testNotifyAllObservers_WithNoObservers_ShouldNotThrowException() {
        // Given
        String message = "Test message";

        // When & Then
        assertDoesNotThrow(() -> bookSubscriber.notifyAllObservers(message));
    }

    /**
     * 测试场景：验证移除观察者后不再通知该观察者
     */
    @Test
    void testNotifyAllObservers_AfterRemove_ShouldNotNotifyRemovedObserver() {
        // Given
        TestObserver testObserver = new TestObserver();
        bookSubscriber.add(testObserver);
        bookSubscriber.remove(testObserver);
        String message = "Test message";

        // When
        bookSubscriber.notifyAllObservers(message);

        // Then
        assertEquals(0, testObserver.getUpdateCount());
    }

    /**
     * 测试场景：验证获取观察者列表返回正确的结果
     */
    @Test
    void testGetObservers_ShouldReturnCorrectList() {
        // Given
        bookSubscriber.add(observer1);
        bookSubscriber.add(observer2);

        // When
        List<AbstractObserver> observers = bookSubscriber.observers;

        // Then
        assertEquals(2, observers.size());
    }

    /**
     * Test helper class to track update calls
     */
    private static class TestObserver extends AbstractObserver {
        private int updateCount = 0;
        private String lastMessage;

        @Override
        public void update(String message) {
            updateCount++;
            lastMessage = message;
        }

        @Override
        public String getType() {
            return "TEST_OBSERVER";
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public String getLastMessage() {
            return lastMessage;
        }
    }
}
