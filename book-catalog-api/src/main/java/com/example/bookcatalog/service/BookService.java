package com.example.bookcatalog.service;

import com.example.bookcatalog.entity.BookEntity;

import java.util.List;

/**
 * Book Service Interface
 */
public interface BookService {

    /**
     * 查询所有书籍
     *
     * @return 书籍列表
     */
    List<BookEntity> findAll();

    /**
     * 根据ID查询单个书籍
     *
     * @param id 书籍ID
     * @return 书籍信息
     */
    BookEntity findOne(Long id);

    /**
     * 创建书籍
     *
     * @param book 书籍信息
     * @return 创建的书籍
     */
    BookEntity create(BookEntity book);

    /**
     * 更新书籍
     *
     * @param book 书籍信息
     * @return 更新后的书籍
     */
    BookEntity update(BookEntity book);

    /**
     * 删除书籍
     *
     * @param id 书籍ID
     */
    void delete(Long id);
}
