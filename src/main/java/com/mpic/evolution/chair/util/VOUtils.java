package com.mpic.evolution.chair.util;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeNumberCondition;
import com.mpic.evolution.chair.pojo.vo.NodeNumberConditionVO;

import java.lang.reflect.Field;

/**
 * @author by cxd
 * @Classname VOUtils
 * @Description TODO
 * @Date 2020/10/23 11:09
 */
public class VOUtils {
    /**
     * @param: [ecmArtworkNodeNumberCondition 被获取的对象, fieldName 获取的属性名, index 索引,
     *                  ecmArtworkNodeNumberConditionClass 被获取的类的class对象, nodeNumberCondition 赋值对象]
     * @return: void
     * @author: cxd
     * @Date: 2020/10/20
     * 描述 :  通过属性名赋值对应 对象属性
     *          主要返回前端使用
     */
    public static void setNodeNumberConditionFieldValue(EcmArtworkNodeNumberCondition ecmArtworkNodeNumberCondition,
                                                  String fieldName , int index, Class<EcmArtworkNodeNumberCondition> ecmArtworkNodeNumberConditionClass, NodeNumberConditionVO nodeNumberCondition )  {

        try {
            Field declaredField = ecmArtworkNodeNumberConditionClass.getDeclaredField(fieldName + index);
            declaredField.setAccessible(true); // 私有属性必须设置访问权限
            String resultValue = (String) declaredField.get(ecmArtworkNodeNumberCondition);
            String appear = resultValue.substring(0, 1);
            String appearValue = resultValue.substring(1);

            if ("appearCondition".equals(fieldName)) {
                nodeNumberCondition.setAppear(appear);
                nodeNumberCondition.setAppearValue(appearValue);
            }
            if ("changeCondition".equals(fieldName)) {
                nodeNumberCondition.setChange(appear);
                nodeNumberCondition.setChangeValue(appearValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
