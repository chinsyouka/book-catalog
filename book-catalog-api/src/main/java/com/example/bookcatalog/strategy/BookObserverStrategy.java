package com.example.bookcatalog.strategy;

import com.example.bookcatalog.enums.ObserverTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Book Observer Strategy
 * 书籍观察者策略类
 */
@Slf4j
@Component
public class BookObserverStrategy extends AbstractStrategy {

    @Override
    public String process(String data) {
        log.info("BookObserverStrategy processing data: {}", data);
        doAction("Processing book observer data");
        
        // 模拟处理逻辑
        String result = "Processed by BookObserverStrategy: " + data;
        log.info("BookObserverStrategy result: {}", result);
        
        return result;
    }

    @Override
    public String getType() {
        return ObserverTypeEnum.type1.name();
    }
}
