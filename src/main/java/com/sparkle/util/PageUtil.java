package com.sparkle.util;

import java.util.Collections;
import java.util.List;

/**
 * 自定义List分页工具
 *
 * @author Smartisan
 */
public class PageUtil {

    /**
     * @param list     目标List
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     */
    public static <T> List<T> subList(List<T> list, int pageNum, int pageSize) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        int size = list.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = fromIndex + pageSize - 1;

        if (fromIndex > size) {
            return Collections.emptyList();
        }
        toIndex = Math.min(toIndex, size);

        return list.subList(fromIndex, toIndex);
    }
}