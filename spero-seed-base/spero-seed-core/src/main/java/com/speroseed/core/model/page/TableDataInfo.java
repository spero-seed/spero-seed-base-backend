package com.speroseed.core.model.page;

import lombok.Data;

import java.util.List;

/**
 * @description 分页数据
 * @author zfq
 * @date 2025/4/2 12:45
 */
@Data
public class TableDataInfo<T> {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 表格数据对象
     */
    public TableDataInfo() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, int total) {
        this.rows = list;
        this.total = total;
    }
}
