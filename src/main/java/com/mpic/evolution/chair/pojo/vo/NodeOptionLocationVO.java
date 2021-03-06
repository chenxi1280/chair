package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

/**
 * @author by cxd
 * @Classname NodeOptionLocationVO
 * @Description TODO
 * @Date 2020/10/7 12:58
 */
@Data
public class NodeOptionLocationVO {

    // 节点 id
    private Integer pkDetailId;
    // 圆所在的位置X轴比
    private Double circleX;
    // 圆所在的位置Y轴比
    private Double circleY;
    // 文字所在的位置X轴比
    private Double textRectX;
    // 文字所在的位置Y轴比
    private Double textRectY;
    // 是否为隐藏选项
    private String  isHide;
    //文本框的高度
    private Double hideHeightScale;
    //文本框的宽度
    private Double hideWidthScale;
    // 透明度
    private Double rectOpacity;
    // 是否使用 皮肤flag 弃用
//    private String locationSkinFlag;
    // 左皮肤框位置X轴比
//    private Double skinRectLeftX;
//    // 左皮肤框位置Y轴比
//    private Double skinRectLeftY;
//    // 右皮肤框位置X轴比
//    private Double skinRectrightX;
//    // 右皮肤框位置Y轴比
//    private Double skinRectrightY;
//    // 文本框位置X轴比
//    private Double textRectWidth;
//    // 文本框的位置Y轴比
//    private Double textRectHeight;
//    // 透明框位置 宽比
//    private Double transparentRectWidth;
//    // 透明框位置 高比
//    private Double transparentRectHeight;
}
