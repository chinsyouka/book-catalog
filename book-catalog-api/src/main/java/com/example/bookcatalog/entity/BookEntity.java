package com.example.bookcatalog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Book Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 书名
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 作者
     */
    private String author;

    /**
     * 分类ID
     */
    private Long catalogId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
