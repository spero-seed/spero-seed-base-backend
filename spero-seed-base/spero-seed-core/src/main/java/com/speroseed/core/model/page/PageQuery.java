package com.speroseed.core.model.page;

import lombok.Data;

import javax.validation.Valid;

/**
 * @description 分页查询请求体
 * @author zfq
 * @date 2025/4/2 12:30
 */
@Data
public class PageQuery<T> {

    /**
     * 查询参数
     */
    @Valid
    private T params;

    /**
     * 分页参数
     */
    private PageDomain page;
}
