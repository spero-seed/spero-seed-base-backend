package com.speroseed.datasource.model.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description 分页数据
 * @author zfq
 * @date 2025/4/2 12:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDataInfo<T> {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;
}
