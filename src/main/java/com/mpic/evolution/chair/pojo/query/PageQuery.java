package com.mpic.evolution.chair.pojo.query;

import lombok.Data;

/**
 * @author cxd
 */
@Data
public class PageQuery {
    /**
     * 当前页码
     */

    private Integer page = 1;

    /**
     * 查询的条数
     */

    private Integer limit = 10;


    /**
     * 查询的起始条数
     */

    private Integer start = 0;

    /**
     * 通过传入的page 和 limit 计算 start
     * @return start 起始位置
     */
    public Integer getStart() {
        if (page != null && limit != null) {
            return (page - 1) * limit;
        }
        return start;
    }

}
