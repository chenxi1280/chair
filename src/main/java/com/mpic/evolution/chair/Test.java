package com.mpic.evolution.chair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by cxd
 * @Classname Test
 * @Description TODO
 * @Date 2020/9/26 14:43
 */
public class Test {
    public static void main(String[] args) {
        int[] nodeNum = {4,3,2};

        getEndingAll(nodeNum);

//        int[] newArr = {};
//        for (int i = 0; i < count; i++) {
//            int[] ints = {};
//            ints[0] = 0;
//            System.arraycopy(arr,0,ints,1,arr.length);
////            arr[i].unshift(0);
////            newArr[i] = iqia{}
////            newArr[i].selectTitle = '点击修改'
////            newArr[i].selectTree = arr[i]
//        }


    }

    private static void getEndingAll(int[] nodeNum) {
        // 计算总结局数
        int count = 1;
        if (nodeNum.length < 1) {
            return;
        }

        for (int i = 0; i < nodeNum.length; i++) {
            count = nodeNum[i] * count;
        }
        // 根据总结局数 判断是否启用全排列  如果总结局数大于256就阻止使用


        // 暂存的数组
        int[][] arr = new int[count][nodeNum.length];
        // 当前需要改变的数组元素的索引
        int editCount = nodeNum.length - 1;
        // 判断是否是当前改变的editCount
        boolean isEdit = false;

        // 第一层循环生成所有的结局数组
        for (int i = 0; i < count; i++) {
//            arr[i] = new int[];
            // 第二层循环生成数组里面的 节点长度
            for (int j = 0; j < nodeNum.length; j++) {
                // 第三层不能使用循环
                // 1、如果是第一次循环，直接等于即可
                if (i == 0) {
                    arr[i][j] = nodeNum[j];
                } else {
                    // 第一种情况 当前的索引就是修改的值的索引  且索引的值大于1
                    // 1、当前数组的索引的值 等于上一个数组的对应索引减1
                    // 第二种情况 当前的索引就是修改的值的索引  索引的值小于等于1  并且editCount的值大于0
                    // 1、当前数组的索引的值  等于原始数组对应索引的最大值，当前索引的前一个  等于前数组对应索引减1
                    // 第三种情况  当前数组改变参数的索引  已经为最后一个  直接返回
                    if (j == editCount && arr[i - 1][j] > 1) {
                        arr[i][j] = arr[i - 1][j] - 1;
                    } else if (j == editCount && arr[i - 1][j] <= 1 && editCount > 0 && !isEdit) {
                        setNum(i, j,arr,editCount, nodeNum);
                    } else if (isEdit) {
                        arr[i][j] = nodeNum[j];
                    } else if (!isEdit) {
                        arr[i][j] = arr[i - 1][j];
                    }
                }
                isEdit = false;
            }
            // arr[i].unshift(0)
        }
        List<String> list = new ArrayList<>();
        for (int[] ints : arr) {
            String s = "";
            for (int anInt : ints) {
                s = s + String.valueOf(anInt);
//                System.out.print();
            }
            list.add(s);
            System.out.println(s);
        }
    }

    private static void  setNum(int i, int j, int[][] arr, int editCount, int[] nodeNum) {
        if (arr[i - 1][editCount] - 1 <= 0) {
            editCount -= 1;
            if (editCount >= 0) {
                arr[i][editCount] = nodeNum[editCount];
                setNum(i, j, arr, editCount, nodeNum);
            }
        } else {
            arr[i][editCount] = arr[i - 1][editCount] - 1;
            arr[i][j] = nodeNum[j];
            editCount = nodeNum.length - 1;
//            isEdit = true;
        }
    }

}
