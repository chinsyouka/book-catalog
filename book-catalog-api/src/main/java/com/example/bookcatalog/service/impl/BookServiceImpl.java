package com.example.bookcatalog.service.impl;

import com.example.bookcatalog.entity.BookEntity;
import com.example.bookcatalog.observer.BookSubscriber;
import com.example.bookcatalog.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Book Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookSubscriber bookSubscriber;

    // 使用内存存储模拟数据库
    private final Map<Long, BookEntity> bookStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<BookEntity> findAll() {
        log.info("查询所有书籍");
        return new ArrayList<>(bookStore.values());
    }

    @Override
    public BookEntity findOne(Long id) {
        log.info("查询单个书籍, ID: {}", id);
        return bookStore.get(id);
    }

    @Override
    public BookEntity create(BookEntity book) {
        log.info("创建书籍: {}", book.getName());
        
        // 生成ID并设置时间
        Long id = idGenerator.getAndIncrement();
        book.setId(id);
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        
        // 存储到内存
        bookStore.put(id, book);
        
        log.info("书籍创建成功, ID: {}", id);

        bookSubscriber.notifyAllObservers("Book created: " + book.getId());

        return book;
    }

    @Override
    public BookEntity update(BookEntity book) {
        log.info("更新书籍, ID: {}", book.getId());
        
        if (book.getId() == null || !bookStore.containsKey(book.getId())) {
            log.warn("书籍不存在, ID: {}", book.getId());
            return null;
        }
        
        // 保留创建时间，更新修改时间
        BookEntity existingBook = bookStore.get(book.getId());
        book.setCreateTime(existingBook.getCreateTime());
        book.setUpdateTime(LocalDateTime.now());
        
        // 更新存储
        bookStore.put(book.getId(), book);
        
        log.info("书籍更新成功, ID: {}", book.getId());
        bookSubscriber.notifyAllObservers("Book updated: " + book.getId());
        return book;
    }

    @Override
    public void delete(Long id) {
        log.info("删除书籍, ID: {}", id);
        
        BookEntity removed = bookStore.remove(id);
        if (removed != null) {
            log.info("书籍删除成功, ID: {}, 书名: {}", id, removed.getName());
            bookSubscriber.notifyAllObservers("Book deleted: " + id);
        } else {
            log.warn("书籍不存在, ID: {}", id);
        }
    }
}
