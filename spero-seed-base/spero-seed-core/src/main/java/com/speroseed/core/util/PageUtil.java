package com.speroseed.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import java.util.HashSet;
import java.util.Set;

import com.speroseed.core.model.page.OrderBy;
import com.speroseed.core.model.page.PageDomain;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
