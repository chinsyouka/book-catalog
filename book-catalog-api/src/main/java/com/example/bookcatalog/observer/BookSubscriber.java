package com.example.bookcatalog.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * Book Subscriber
 * 书籍订阅者示例
 */
@Slf4j
public class BookSubscriber extends AbstractSubscriber {

    private final String subscriberName;

    public BookSubscriber(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    @Override
    public void add(AbstractObserver observer) {
        log.info("Adding observer [{}] to subscriber [{}]", observer.getType(), subscriberName);
        observers.add(observer);
        doAction("Observer added");
    }

    @Override
    public void remove(AbstractObserver observer) {
        log.info("Removing observer [{}] from subscriber [{}]", observer.getType(), subscriberName);
        observers.remove(observer);
        doAction("Observer removed");
    }

    @Override
    public void notifyAllObservers(String message) {
        log.info("Notifying all observers in subscriber [{}], message: {}", subscriberName, message);
        doAction("Notifying observers");
        
        for (AbstractObserver observer : observers) {
            observer.update(message);
        }
    }
}
