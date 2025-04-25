package com.speroseed.datasource.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.speroseed.datasource.model.page.OrderBy;
import com.speroseed.datasource.model.page.PageDomain;
import com.speroseed.datasource.model.page.TableDataInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description
 * @author zfq
 * @date 2025/4/2 23:05
 */
public class PageUtil extends PageHelper {

    /**
     * 设置请求分页数据
     */
    public static void startPage(PageDomain pageDomain) {
        if (ObjectUtils.isEmpty(pageDomain)) {
            return;
        }

        Integer pageNum = Convert.toInt(pageDomain.getPageNum(), 1);
        Integer pageSize = Convert.toInt(pageDomain.getPageSize(), 10);
        String orderBy = SqlUtil.escapeOrderBySql(getOrderByColumn(pageDomain.getOrderBy()));
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageSize = pageSize == 0 ? 10 : pageSize;
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }

    /**
     * list转TableDataInfo
     * @param list
     * @return
     */
    public static <T> TableDataInfo<T> toTableDataInfo(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            return new TableDataInfo<>(page.getTotal(), page.getResult());
        } else {
            throw new RuntimeException("当前list不支持转换为TableDataInfo对象");
        }
    }

    private static String getOrderByColumn(Set<OrderBy> orderBy) {
        if (CollectionUtils.isEmpty(orderBy)) {
            return "";
        }

        Set<String> orderByColumns = new HashSet<>();
        for (OrderBy item : orderBy) {
            if (StrUtil.isNotEmpty(item.getColumn())) {
                orderByColumns.add(item.toString());
            }
        }

        return StringUtils.collectionToDelimitedString(orderByColumns, ", ");
    }
}
