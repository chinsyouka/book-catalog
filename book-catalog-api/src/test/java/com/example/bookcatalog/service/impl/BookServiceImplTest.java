package com.example.bookcatalog.service.impl;

import com.example.bookcatalog.entity.BookEntity;
import com.example.bookcatalog.observer.BookSubscriber;
import com.example.bookcatalog.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * BookServiceImpl Test
 */
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookSubscriber bookSubscriber;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        // 创建 BookServiceImpl 实例，注入 mock 的 BookSubscriber
        bookService = new BookServiceImpl(bookSubscriber);
    }

    /**
     * 测试场景：当书籍列表为空时，查询所有书籍应返回空列表
     */
    @Test
    void testFindAll_WhenEmpty_ShouldReturnEmptyList() {
        // When
        List<BookEntity> result = bookService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * 测试场景：当存在多本书籍时，查询所有书籍应返回全部书籍
     */
    @Test
    void testFindAll_WhenHasBooks_ShouldReturnAllBooks() {
        // Given
        BookEntity book1 = createTestBook("Book 1");
        BookEntity book2 = createTestBook("Book 2");
        bookService.create(book1);
        bookService.create(book2);

        // When
        List<BookEntity> result = bookService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /**
     * 测试场景：当书籍存在时，根据ID查询应返回对应的书籍信息
     */
    @Test
    void testFindOne_WhenExists_ShouldReturnBook() {
        // Given
        BookEntity book = createTestBook("Test Book");
        BookEntity created = bookService.create(book);

        // When
        BookEntity found = bookService.findOne(created.getId());

        // Then
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("Test Book", found.getName());
    }

    /**
     * 测试场景：当书籍不存在时，根据ID查询应返回null
     */
    @Test
    void testFindOne_WhenNotExists_ShouldReturnNull() {
        // When
        BookEntity found = bookService.findOne(999L);

        // Then
        assertNull(found);
    }

    /**
     * 测试场景：创建书籍时应自动生成ID、设置时间戳并通知观察者
     */
    @Test
    void testCreate_ShouldSetIdAndTimeAndNotifyObservers() {
        // Given
        BookEntity book = createTestBook("New Book");

        // When
        BookEntity created = bookService.create(book);

        // Then
        assertNotNull(created);
        assertNotNull(created.getId());
        assertNotNull(created.getCreateTime());
        assertNotNull(created.getUpdateTime());
        assertEquals("New Book", created.getName());
        
        // Verify observer notification
        verify(bookSubscriber, times(1)).notifyAllObservers(anyString());
    }

    /**
     * 测试场景：更新存在的书籍时应成功更新并通知观察者
     */
    @Test
    void testUpdate_WhenExists_ShouldUpdateBookAndNotifyObservers() {
        // Given
        BookEntity book = createTestBook("Original Name");
        BookEntity created = bookService.create(book);
        
        // When
        created.setName("Updated Name");
        created.setPrice(new BigDecimal("99.99"));
        BookEntity updated = bookService.update(created);

        // Then
        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals(new BigDecimal("99.99"), updated.getPrice());
        assertNotNull(updated.getUpdateTime());
        
        // Verify observer notification
        verify(bookSubscriber, times(1)).notifyAllObservers(anyString());
    }

    /**
     * 测试场景：更新不存在的书籍时应返回null且不通知观察者
     */
    @Test
    void testUpdate_WhenNotExists_ShouldReturnNull() {
        // Given
        BookEntity book = createTestBook("Non-existent Book");
        book.setId(999L);

        // When
        BookEntity updated = bookService.update(book);

        // Then
        assertNull(updated);
        
        // Verify no observer notification
        verify(bookSubscriber, never()).notifyAllObservers(anyString());
    }

    /**
     * 测试场景：更新ID为null的书籍时应返回null
     */
    @Test
    void testUpdate_WhenIdIsNull_ShouldReturnNull() {
        // Given
        BookEntity book = createTestBook("Book with null ID");
        book.setId(null);

        // When
        BookEntity updated = bookService.update(book);

        // Then
        assertNull(updated);
    }

    /**
     * 测试场景：删除存在的书籍时应成功删除并通知观察者
     */
    @Test
    void testDelete_WhenExists_ShouldDeleteBookAndNotifyObservers() {
        // Given
        BookEntity book = createTestBook("Book to Delete");
        BookEntity created = bookService.create(book);

        // When
        bookService.delete(created.getId());

        // Then
        BookEntity found = bookService.findOne(created.getId());
        assertNull(found);
        
        // Verify observer notification
        verify(bookSubscriber, times(1)).notifyAllObservers(anyString());
    }

    /**
     * 测试场景：删除不存在的书籍时不应抛异常且不通知观察者
     */
    @Test
    void testDelete_WhenNotExists_ShouldDoNothing() {
        // When
        bookService.delete(999L);

        // Then
        // No exception should be thrown
        
        // Verify no observer notification
        verify(bookSubscriber, never()).notifyAllObservers(anyString());
    }

    /**
     * 测试场景：创建多本书籍时应生成唯一的ID
     */
    @Test
    void testCreateMultipleBooks_ShouldGenerateUniqueIds() {
        // Given & When
        BookEntity book1 = bookService.create(createTestBook("Book 1"));
        BookEntity book2 = bookService.create(createTestBook("Book 2"));
        BookEntity book3 = bookService.create(createTestBook("Book 3"));

        // Then
        assertNotEquals(book1.getId(), book2.getId());
        assertNotEquals(book2.getId(), book3.getId());
        assertNotEquals(book1.getId(), book3.getId());
    }

    /**
     * 测试场景：更新书籍时应保留原始创建时间，只更新修改时间
     */
    @Test
    void testUpdate_ShouldPreserveCreateTime() {
        // Given
        BookEntity book = createTestBook("Test Book");
        BookEntity created = bookService.create(book);
        
        // When
        try {
            Thread.sleep(10); // Ensure time difference
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        created.setName("Updated Name");
        BookEntity updated = bookService.update(created);

        // Then
        assertEquals(created.getCreateTime(), updated.getCreateTime());
        assertNotEquals(created.getUpdateTime(), updated.getUpdateTime());
    }

    /**
     * Helper method to create a test book
     */
    private BookEntity createTestBook(String name) {
        return BookEntity.builder()
                .name(name)
                .price(new BigDecimal("29.99"))
                .author("Test Author")
                .catalogId(1L)
                .build();
    }
}
