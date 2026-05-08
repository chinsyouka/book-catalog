package com.example.bookcatalog.controller;

import com.example.bookcatalog.dto.ApiResponse;
import com.example.bookcatalog.entity.BookEntity;
import com.example.bookcatalog.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BookController Test
 */
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    private BookEntity testBook;

    @BeforeEach
    void setUp() {
        testBook = BookEntity.builder()
                .id(1L)
                .name("Java Programming")
                .price(new BigDecimal("59.99"))
                .author("John Doe")
                .catalogId(1L)
                .build();
    }

    /**
     * 测试场景：查询所有书籍应返回书籍列表
     */
    @Test
    void testFindAll_ShouldReturnBookList() throws Exception {
        // Given：准备测试数据
        List<BookEntity> books = Arrays.asList(testBook);
        given(bookService.findAll()).willReturn(books);

        // When & Then：执行 GET 请求并验证响应
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Java Programming"));

        // Verify：验证服务方法被调用
        verify(bookService).findAll();
    }

    /**
     * 测试场景：根据ID查询存在的书籍应返回书籍信息
     */
    @Test
    void testFindOne_WhenExists_ShouldReturnBook() throws Exception {
        // Given：模拟服务返回书籍
        given(bookService.findOne(1L)).willReturn(testBook);

        // When & Then：执行 GET 请求并验证响应
        mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Java Programming"));

        // Verify：验证服务方法被调用
        verify(bookService).findOne(1L);
    }

    /**
     * 测试场景：根据ID查询不存在的书籍应返回404
     */
    @Test
    void testFindOne_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given：模拟服务返回null
        given(bookService.findOne(999L)).willReturn(null);

        // When & Then：执行 GET 请求并验证404响应
        mockMvc.perform(get("/api/books/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Book not found with ID: 999"));

        // Verify：验证服务方法被调用
        verify(bookService).findOne(999L);
    }

    /**
     * 测试场景：创建书籍应返回201和创建的书籍信息
     */
    @Test
    void testCreate_ShouldReturnCreatedBook() throws Exception {
        // Given：准备新书籍对象（不含ID）
        BookEntity newBook = BookEntity.builder()
                .name("New Book")
                .price(new BigDecimal("39.99"))
                .author("Jane Smith")
                .catalogId(2L)
                .build();

        BookEntity createdBook = BookEntity.builder()
                .id(2L)
                .name("New Book")
                .price(new BigDecimal("39.99"))
                .author("Jane Smith")
                .catalogId(2L)
                .build();

        given(bookService.create(any(BookEntity.class))).willReturn(createdBook);

        // When & Then：执行 POST 请求并验证201响应
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value("New Book"));

        // Verify：验证服务方法被调用
        verify(bookService).create(any(BookEntity.class));
    }

    /**
     * 测试场景：创建书名为空的书籍应返回400错误
     */
    @Test
    void testCreate_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        // Given：准备书名为空的书籍对象
        BookEntity invalidBook = BookEntity.builder()
                .name("")
                .price(new BigDecimal("39.99"))
                .build();

        // When & Then：执行 POST 请求并验证400响应
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Book name cannot be empty"));
    }

    /**
     * 测试场景：更新存在的书籍应返回更新后的书籍信息
     */
    @Test
    void testUpdate_WhenExists_ShouldReturnUpdatedBook() throws Exception {
        // Given：准备更新的书籍对象
        BookEntity updateBook = BookEntity.builder()
                .id(1L)
                .name("Updated Java Programming")
                .price(new BigDecimal("69.99"))
                .author("John Doe")
                .catalogId(1L)
                .build();

        given(bookService.update(any(BookEntity.class))).willReturn(updateBook);

        // When & Then：执行 PUT 请求并验证响应
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Updated Java Programming"));

        // Verify：验证服务方法被调用
        verify(bookService).update(any(BookEntity.class));
    }

    /**
     * 测试场景：更新书籍时路径ID和请求体ID不一致应返回400
     */
    @Test
    void testUpdate_WithMismatchedId_ShouldReturnBadRequest() throws Exception {
        // Given：准备ID不匹配的书籍对象
        BookEntity updateBook = BookEntity.builder()
                .id(2L)  // 与路径参数 /api/books/1 不匹配
                .name("Updated Book")
                .build();

        // When & Then：执行 PUT 请求并验证400响应
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Path ID and body ID must match"));
    }

    /**
     * 测试场景：更新不存在的书籍应返回404
     */
    @Test
    void testUpdate_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given：准备更新的书籍对象，但服务返回null
        BookEntity updateBook = BookEntity.builder()
                .id(999L)
                .name("Non-existent Book")
                .build();

        given(bookService.update(any(BookEntity.class))).willReturn(null);

        // When & Then：执行 PUT 请求并验证404响应
        mockMvc.perform(put("/api/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBook)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    /**
     * 测试场景：部分更新存在的书籍应返回更新后的书籍信息
     */
    @Test
    void testPartialUpdate_WhenExists_ShouldReturnUpdatedBook() throws Exception {
        // Given：模拟现有书籍和部分更新数据
        BookEntity existingBook = BookEntity.builder()
                .id(1L)
                .name("Java Programming")
                .price(new BigDecimal("59.99"))
                .author("John Doe")
                .catalogId(1L)
                .build();

        BookEntity partialUpdate = BookEntity.builder()
                .price(new BigDecimal("79.99"))  // 只更新价格
                .build();

        BookEntity updatedBook = BookEntity.builder()
                .id(1L)
                .name("Java Programming")
                .price(new BigDecimal("79.99"))
                .author("John Doe")
                .catalogId(1L)
                .build();

        given(bookService.findOne(1L)).willReturn(existingBook);
        given(bookService.update(any(BookEntity.class))).willReturn(updatedBook);

        // When & Then：执行 PATCH 请求并验证响应
        mockMvc.perform(patch("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.price").value(79.99));

        // Verify：验证服务方法被调用
        verify(bookService).findOne(1L);
        verify(bookService).update(any(BookEntity.class));
    }

    /**
     * 测试场景：部分更新不存在的书籍应返回404
     */
    @Test
    void testPartialUpdate_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given：模拟书籍不存在
        given(bookService.findOne(999L)).willReturn(null);

        BookEntity partialUpdate = BookEntity.builder()
                .price(new BigDecimal("79.99"))
                .build();

        // When & Then：执行 PATCH 请求并验证404响应
        mockMvc.perform(patch("/api/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    /**
     * 测试场景：删除存在的书籍应返回成功
     */
    @Test
    void testDelete_WhenExists_ShouldReturnSuccess() throws Exception {
        // Given：模拟书籍存在
        given(bookService.findOne(1L)).willReturn(testBook);
        doNothing().when(bookService).delete(1L);

        // When & Then：执行 DELETE 请求并验证响应
        mockMvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // Verify：验证服务方法被调用
        verify(bookService).findOne(1L);
        verify(bookService).delete(1L);
    }

    /**
     * 测试场景：删除不存在的书籍应返回404
     */
    @Test
    void testDelete_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Given：模拟书籍不存在
        given(bookService.findOne(999L)).willReturn(null);

        // When & Then：执行 DELETE 请求并验证404响应
        mockMvc.perform(delete("/api/books/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));

        // Verify：验证只调用了 findOne，没有调用 delete
        verify(bookService).findOne(999L);
    }

    /**
     * 测试场景：按分类ID查询应返回过滤后的书籍列表
     */
    @Test
    void testFindByCatalogId_ShouldReturnFilteredBooks() throws Exception {
        // Given：准备多个书籍，其中两个属于分类1
        BookEntity book1 = BookEntity.builder()
                .id(1L)
                .name("Java Programming")
                .catalogId(1L)
                .build();

        BookEntity book2 = BookEntity.builder()
                .id(2L)
                .name("Python Programming")
                .catalogId(1L)
                .build();

        BookEntity book3 = BookEntity.builder()
                .id(3L)
                .name("Database Design")
                .catalogId(2L)
                .build();

        List<BookEntity> allBooks = Arrays.asList(book1, book2, book3);
        given(bookService.findAll()).willReturn(allBooks);

        // When & Then：执行 GET 请求并验证只返回分类1的书籍
        mockMvc.perform(get("/api/books/catalog/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].catalogId").value(1))
                .andExpect(jsonPath("$.data[1].catalogId").value(1));

        // Verify：验证服务方法被调用
        verify(bookService).findAll();
    }

    /**
     * 测试场景：按不存在的分类ID查询应返回空列表
     */
    @Test
    void testFindByCatalogId_WithNonExistentCatalog_ShouldReturnEmptyList() throws Exception {
        // Given：准备书籍列表，但没有分类999的书籍
        BookEntity book1 = BookEntity.builder()
                .id(1L)
                .name("Java Programming")
                .catalogId(1L)
                .build();

        List<BookEntity> allBooks = Arrays.asList(book1);
        given(bookService.findAll()).willReturn(allBooks);

        // When & Then：执行 GET 请求并验证返回空列表
        mockMvc.perform(get("/api/books/catalog/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }
}
