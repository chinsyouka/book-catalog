package com.example.bookcatalog.observer;

import com.example.bookcatalog.enums.ObserverTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Book Observer
 * 书籍观察者示例
 */
@Slf4j
@Component
public class BookObserver extends AbstractObserver {

    @Override
    public void update(String message) {
        log.info("BookObserver [{}] received update: {}", getType(), message);
        doAction("Processing book update notification");
    }

    @Override
    public String getType() {
        return ObserverTypeEnum.type1.name();
    }
}
