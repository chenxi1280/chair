package com.mpic.evolution.chair.service;

import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.query.EcmArtworkEndingsQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodeNumberConditionVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;

/**
 * @author Administrator
 */
public interface EcmArtWorkService {

    ResponseDTO updateNodeInfo(EcmArtWorkQuery ecmArtWorkQuery);

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

    ResponseDTO saveArtworkNodeNumberCondition(EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO);

    ResponseDTO saveAllNodeNameFlagChange(EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO);

    ResponseDTO saveArtworkEndings(EcmArtworkEndingsQuery ecmArtworkEndingsVO);

    ResponseDTO saveArtworkEndingState(EcmArtworkVo ecmArtworkVo);

    ResponseDTO saveArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    ResponseDTO updateArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    ResponseDTO deleteArtworkEnding(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    ResponseDTO getArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    ResponseDTO saveArtworkEndingAll(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);

    ResponseDTO deleteArtworkEndingList(EcmArtworkEndingsQuery ecmArtworkEndingsQuery);




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
