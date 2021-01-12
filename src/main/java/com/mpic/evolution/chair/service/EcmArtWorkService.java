package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.query.EcmArtworkEndingsQuery;
import com.mpic.evolution.chair.pojo.query.EcmArtworkNodeBuoyQuery;
import com.mpic.evolution.chair.pojo.vo.*;

/**
 * @author Administrator
 */
public interface EcmArtWorkService {

    ResponseDTO updateNodeInfo();

    void insertNodeInfo(EcmArtworkNodesVo ecmArtworkNodesVo);


    /**
     * @param: [ecmArtWorkQuery] 传入的 查询参数 查询参数可以有 用户id，作品名称（模糊），视频状态，类型（当前模糊）
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: SJ
     * @Date: 2020/8/5
     * 	描述 :  按照条件查询作品
     *        	保存成功: status 200  msg "success" data: 数据
     *       	 保存失败: status 500  msg "error"
     */
    ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * @param:  [ecmArtWorkQuery] 需要 作品id 必传
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date:   2020/8/5
     * 描述 :    查询作品详情根据 作品id
     *          保存成功: status 200  msg "success”  data: 数据
     *          保存失败: status 500  msg "error“
     */
    ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * @param: [ecmArtworkNodes] 单个节点类
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 作品单个节点 ArtWork 接口
     *       保存成功： status 200  msg “success”
     *       保存失败： status 500  msg ”error“
     */
    ResponseDTO saveArtWorkNode(EcmArtworkNodesVo ecmArtworkNodes);

    /**
     * @param: [ecmArtworkNodesVo] json 格式的 作品详情类（树状）
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 作品(所有) ArtWork 接口
     *       保存成功: status 200  msg "success”
     *       保存失败: status 500  msg "error“
     */
    ResponseDTO addArtWork(EcmArtworkNodesVo ecmArtworkNodesVo);

    /**
     * @param: [ecmArtworkNodesVo]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 : 删除节点 ，需要作品id 和节点id
     *       成功: status 200  msg "success”
     *       失败: status 500  msg "error“
     */
    ResponseDTO removeNode(EcmArtworkNodesVo ecmArtworkNodesVo);

    /**
     * @param: [ecmArtworkNodeNumberConditionVO]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 :  保存全局数值选项
     *       成功: status 200  msg "success”
     *       失败: status 500  msg "error“
     */
    ResponseDTO saveArtworkNodeNumberCondition(EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO);

    /**
     * @param: [ecmArtworkNodeNumberConditionVO]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 	 * 描述 : 保存全局数值选项 ~.~
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO saveAllNodeNameFlagChange(EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO);

    /**
     * @param: [ecmArtworkEndingsVO]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 保存 多结局节点
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO saveArtworkEndings(EcmArtworkEndingsQuery ecmArtworkEndingsVO);

    /**
     * @param: [ecmArtworkVo]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 修改作品多结局状态
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO saveArtworkEndingState(EcmArtworkVo ecmArtworkVo);

    /**
     * @param: [ecmArtworkEndingsQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 保存多结局节点 list
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO saveArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    /**
     * @param: [ecmArtworkEndingsQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 批量更新 多结局 节点
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO updateArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    /**
     * @param: [ecmArtworkEndingsQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 删除 多结局 节点
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO deleteArtworkEnding(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    /**
     * @param: [ecmArtworkEndingsQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 获取多结局节点list
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO getArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    /**
     * @param: [ecmArtworkEndingsQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 批量保存多 多结局
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO saveArtworkEndingAll(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    /**
     * @param: [ecmArtworkEndingsQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/12/25
     * 描述 : 批量删除多结局 节点
     *       成功: status 200  msg "success”   date:
     *       失败: status 500  msg "error“
     */
    ResponseDTO deleteArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);


    ResponseDTO saveArtworkNodePopupSettings(EcmArtworkNodePopupSettingsVO ecmArtworkNodePopupSettingsVO);

    ResponseDTO saveArtworkNodeCondition(EcmArtworkNodesVo ecmArtworkVo);

    ResponseDTO saveArtworkEndingCondition(EcmArtworkNodesVo ecmArtworkNodesVo);

    ResponseDTO saveArtworkNodeBuoy(EcmArtworkNodeBuoyQuery ecmArtworkNodeBuoyVO);

    ResponseDTO deleteArtworkNodeBuoy(EcmArtworkNodeBuoyVO ecmArtworkNodeBuoyVO);





//小程序接口


    /**
     * @param: [ecmArtWorkQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/26
     * 描述 :  根据热度进行主页 查询
     *       保存成功: status 200  msg "success” data "数据"
     *       保存失败: status 500  msg "error“
     */
    ResponseDTO getFindArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * @param: [ecmArtWorkQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 :  发现 分类 页面获取 作品信息接口
     *       成功: status 200  msg "success”   date: List<EcmArtworkVo>
     *       失败: status 500  msg "error“
     */
    ResponseDTO getFindSortArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * @param: [ecmArtWorkQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 :  小程序 获取排行榜单的 数据
     *       成功: status 200  msg "success”   date: List<EcmArtworkVo>
     *       失败: status 500  msg "error“
     */
    ResponseDTO getRankingArtWorks(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * @param: [ecmArtWorkQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 : 小程序端 搜索接口
     *       成功: status 200  msg "success”   date: List<EcmArtworkVo>
     *       失败: status 500  msg "error“
     */
    ResponseDTO search(EcmArtWorkQuery ecmArtWorkQuery);

    /**
     * @param: [ecmArtWorkQuery]
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/9/26
     * 描述 : 小程序端 故事线 获取数据接口
     *       成功: status 200  msg "success”   date:List<EcmArtworkVo>
     *       失败: status 500  msg "error“
     */
    ResponseDTO getArtWorkNodes(EcmArtWorkQuery ecmArtWorkQuery);


}
