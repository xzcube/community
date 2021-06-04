package com.xzcube.community.utils;

/**
 * @author xzcube
 * @date 2021/6/4 10:47
 */
public class PageUtils {
    /**
     * 计算页面的偏移量
     * @param page 当前页码
     * @param totalCount 总记录数
     * @param size 每页展示的数量
     * @return
     */
    public static Integer setOffset(Integer page, Integer totalCount, Integer size){
        // 页面展示的偏移量
        int offset = size * (page - 1);

        int i = 2;
        while(offset >= totalCount){
            offset = size * (page - i);
            i++;
        }
        offset = Math.max(offset, 0);
        return offset;
    }
}
