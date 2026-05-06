package com.example.bookcatalog.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * Book Observer
 * 书籍观察者示例
 */
@Slf4j
public class BookObserver extends AbstractObserver {

    private final String observerName;

    public BookObserver(String observerName) {
        this.observerName = observerName;
    }

    @Override
    public void update(String message) {
        log.info("BookObserver [{}] received update: {}", observerName, message);
        doAction("Processing book update notification");
    }

    @Override
    public String getType() {
        return "BOOK_OBSERVER";
    }
}
