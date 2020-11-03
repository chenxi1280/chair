package com.mpic.evolution.chair.util;

import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodeNumberCondition;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodeNumberConditionVO;
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
            // 私有属性必须设置访问权限
            declaredField.setAccessible(true);
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

    /**
     * @param: [ecmArtworkNodeNumberCondition 被获取的对象,, ecmArtworkNodeNumberConditionClass 被获取的类的class对象, ecmArtworkNodeNumberConditionVOClass 转换类的class对象,
     *                                                          changeCondition 被转换的属性名, changeConditionValue 转换的属性名, i 索引]
     * @return: void
     * @author: cxd
     * @Date: 2020/10/27
     * 描述 :  给返回前端的VO 属性动态 转换 String -> integer
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    public static void setNodeNumberConditionValueFieldValue(EcmArtworkNodeNumberCondition ecmArtworkNodeNumberCondition, Class<EcmArtworkNodeNumberCondition> ecmArtworkNodeNumberConditionClass, Class<EcmArtworkNodeNumberConditionVO> ecmArtworkNodeNumberConditionVOClass, String changeCondition, String changeConditionValue, int i) {
        try {
            Field declaredField = ecmArtworkNodeNumberConditionClass.getDeclaredField(changeCondition + i);
            declaredField.setAccessible(true);
            String resultValue = (String) declaredField.get(ecmArtworkNodeNumberCondition);
            Field vDeclaredField = ecmArtworkNodeNumberConditionVOClass.getDeclaredField(changeConditionValue + i);
            vDeclaredField.setAccessible(true);
            vDeclaredField.set(ecmArtworkNodeNumberCondition,Integer.valueOf(resultValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
