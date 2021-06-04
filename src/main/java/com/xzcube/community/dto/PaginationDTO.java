package com.xzcube.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzcube
 * @date 2021/5/27 15:12
 * 封装一个页面中展示的内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO<T> {
    private List<T> data; // 每页展示的话题
    private boolean showPrevious; // 是否展示前一页按钮
    private boolean showFirstPage;  // 是否展示跳转首页按钮
    private boolean showNextPage; // 是否展示后一页按钮
    private boolean showLastPage; // 是否展示跳转尾页按钮
    //private List<CommentShowDTO> showComment; // 每页展示的评论

    private Integer page; // 当前页码
    private List<Integer> pages = new ArrayList<>(); // 当前分页条中展示的页码
    private Integer totalPage; // 总页数

    /**
     * 通过话题总数，当前页码，每页展示的话题数量，给上面的各种属性赋值
     * @param totalCount 话题总数
     * @param page 当前页码
     * @param size 每页展示数量
     */
    public void setPagination(Integer totalCount, Integer page, Integer size){
        int totalPage = totalCount / size; // 总的页码数量
        if(totalCount % size != 0){
            totalPage++;
        }
        this.totalPage = totalPage;
        page = page > totalPage ? totalPage : page;
        page = page <= 0 ? 1 : page;
        this.page = page;
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if(page - i > 0){
                pages.add(0, page - i); // 头部插入
            }
            if(page + i <= totalPage){
                pages.add(page + i);
            }
        }

        // 当前页码不为 1 的时候展示上一页按钮
        showPrevious = page != 1;
        // 当前不在最后一页的时候展示下一页按钮
        showNextPage = page != totalPage;
        // 当前展示的页码中不包括第一页时，展示跳转首页按钮
        showFirstPage = !pages.contains(1);
        // 当前展示的页码中不包括最后一页的时候展示跳转尾页按钮
        showLastPage = !pages.contains(totalPage);
    }
}
