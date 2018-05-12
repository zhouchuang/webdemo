package user.zc.utils;

/**
 * 分页实体类
 *
 * @author:Administrator
 * @create 2018-01-21 11:10
 */

import lombok.Data;

import java.util.List;

@Data
public  class PageParam extends QueryParam {
    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageSize;
    private int currentPage;
    private int prePage;
    private int nextPage;
    private int totalPage;
    private int totalCount;
    private List list;

    public PageParam() {
        super();
        this.currentPage = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     *
     * @param currentPage
     * @param pageSize
     */
    public PageParam(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}