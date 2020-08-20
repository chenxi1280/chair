package com.mpic.evolution.chair.dao;

import com.mpic.evolution.chair.pojo.vo.MediaByProcedureVo;

import java.util.List;

/**
 * 类名称： ProcessMediaByProcedureDao
 *
 * @author: xuezx
 * @date 2020/8/19 13:24
 * 类描述：
 */
public interface ProcessMediaByProcedureDao {

    List<MediaByProcedureVo> getUnHandledVideo();

}
