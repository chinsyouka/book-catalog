package com.example.bookcatalog.controller;

import com.example.bookcatalog.dto.ApiResponse;
import com.example.bookcatalog.entity.BookEntity;
import com.example.bookcatalog.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Book Controller
 * RESTful API for Book CRUD operations
 */
@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 查询所有书籍
     * GET /api/books
     *
     * @return 书籍列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookEntity>>> findAll() {
        log.info("REST API: Get all books");
        List<BookEntity> books = bookService.findAll();
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    /**
     * 根据ID查询单个书籍
     * GET /api/books/{id}
     *
     * @param id 书籍ID
     * @return 书籍信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookEntity>> findOne(@PathVariable Long id) {
        log.info("REST API: Get book by ID: {}", id);
        BookEntity book = bookService.findOne(id);
        
        if (book == null) {
            log.warn("Book not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Book not found with ID: " + id));
        }
        
        return ResponseEntity.ok(ApiResponse.success(book));
    }

    /**
     * 创建书籍
     * POST /api/books
     *
     * @param book 书籍信息
     * @return 创建的书籍
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookEntity>> create(@RequestBody BookEntity book) {
        log.info("REST API: Create new book: {}", book.getName());
        
        // 验证请求参数
        if (book.getName() == null || book.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Book name cannot be empty"));
        }
        
        BookEntity created = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    /**
     * 更新书籍
     * PUT /api/books/{id}
     *
     * @param id 书籍ID
     * @param book 书籍信息
     * @return 更新后的书籍
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookEntity>> update(@PathVariable Long id, 
                                                           @RequestBody BookEntity book) {
        log.info("REST API: Update book with ID: {}", id);
        
        // 确保路径参数和请求体中的ID一致
        if (!id.equals(book.getId())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Path ID and body ID must match"));
        }
        
        BookEntity updated = bookService.update(book);
        
        if (updated == null) {
            log.warn("Book not found for update with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Book not found with ID: " + id));
        }
        
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    /**
     * 部分更新书籍
     * PATCH /api/books/{id}
     *
     * @param id 书籍ID
     * @param book 书籍信息（部分字段）
     * @return 更新后的书籍
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<BookEntity>> partialUpdate(@PathVariable Long id,
                                                                  @RequestBody BookEntity book) {
        log.info("REST API: Partial update book with ID: {}", id);
        
        // 获取现有书籍
        BookEntity existing = bookService.findOne(id);
        if (existing == null) {
            log.warn("Book not found for partial update with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Book not found with ID: " + id));
        }
        
        // 合并字段（只更新提供的字段）
        if (book.getName() != null) {
            existing.setName(book.getName());
        }
        if (book.getPrice() != null) {
            existing.setPrice(book.getPrice());
        }
        if (book.getAuthor() != null) {
            existing.setAuthor(book.getAuthor());
        }
        if (book.getCatalogId() != null) {
            existing.setCatalogId(book.getCatalogId());
        }
        
        BookEntity updated = bookService.update(existing);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    /**
     * 删除书籍
     * DELETE /api/books/{id}
     *
     * @param id 书籍ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("REST API: Delete book with ID: {}", id);
        
        // 先检查书籍是否存在
        BookEntity existing = bookService.findOne(id);
        if (existing == null) {
            log.warn("Book not found for deletion with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "Book not found with ID: " + id));
        }
        
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 批量查询书籍（按分类ID）
     * GET /api/books/catalog/{catalogId}
     *
     * @param catalogId 分类ID
     * @return 书籍列表
     */
    @GetMapping("/catalog/{catalogId}")
    public ResponseEntity<ApiResponse<List<BookEntity>>> findByCatalogId(@PathVariable Long catalogId) {
        log.info("REST API: Get books by catalog ID: {}", catalogId);
        List<BookEntity> books = bookService.findAll();
        
        // 过滤指定分类的书籍
        List<BookEntity> filteredBooks = books.stream()
                .filter(book -> catalogId.equals(book.getCatalogId()))
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success(filteredBooks));
    }
}
