package com.speroseed.datasource.model.page;

import lombok.Data;

import java.util.Set;

/**
 * @description
 * @author zfq
 * @date 2025/4/2 12:38
 */
@Data
public class PageDomain {

    /**
     * 当前记录起始索引
     */
    private Integer pageNum = 1;

    /**
     * 每页显示记录数
     */
    private Integer pageSize = 10;

    /**
     * 排序
     */
    private Set<OrderBy> orderBy;
}
