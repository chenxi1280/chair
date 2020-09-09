package com.mpic.evolution.chair.pojo.vo;

import lombok.Data;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-9-9 12:58:56 
*/
@Data
public class WxPlayRecordVo {
	
	 /**
	  *	作品树id
	  */
    private Integer pkArtworkId;
	
	 /**
	  *	作品树其中某个节点的id
	  */
    private Integer detailId;
    
    /**
     * 	观看用户id
     */
    private Integer userId;
}
