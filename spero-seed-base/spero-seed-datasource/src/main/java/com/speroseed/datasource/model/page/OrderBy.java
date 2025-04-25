package com.speroseed.datasource.model.page;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @description
 * @author zfq
 * @date 2025/4/2 12:43
 */
@Data
public class OrderBy {

    /**
     * 字段名（驼峰）
     */
    private String column;

    /**
     * 是否正序，默认true
     */
    private Boolean isAsc;

    @Override
    public String toString() {
        if (isAsc) {
            return StrUtil.toUnderlineCase(column) + " ASC";
        }
        return StrUtil.toUnderlineCase(column) + " DESC";
    }
}
