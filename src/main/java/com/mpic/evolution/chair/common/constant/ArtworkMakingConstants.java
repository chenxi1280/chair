package com.mpic.evolution.chair.common.constant;

/**
 *
 * 类名:  ArtworkMakingConstants
 * @author Xuezx
 * @date 2019/9/20 0020 16:38
 * 描述: 视频制作页面常用常量
 */
public interface ArtworkMakingConstants {

    /**
     * 草稿
     */
    int ARTWORK_STATUS_DRAFT = 0;
    /**
     * 待审核
     */
    int ARTWORK_STATUS_PRE_REVIEWE = 1;
    /**审核通过
     *
     */
    int ARTWORK_STATUS_REVIEWED = 2;
    /**审核未通过
     *
     */
    int ARTWORK_STATUS_REVIEWE_REFUSED  = 3;
    /**被删除
     *
     */
    int ARTWORK_STATUS_DELETED = 5;

    /**作品封面的基础保存路径
     *
     */
    String PIC_BASIC_PATH = "/appl/artwork_data/";


    /**
     *
     * 是否喜欢
     */
    String LIKED = "1";
    String UN_LIKED = "0";

    int CSS_VO_MAX_LENGTH = 6;


    String IS_ENDING_TYPE = "1";
}
