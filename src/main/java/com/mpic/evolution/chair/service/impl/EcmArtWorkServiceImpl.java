package com.mpic.evolution.chair.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.mpic.evolution.chair.dao.*;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHot;
import com.mpic.evolution.chair.pojo.vo.*;
import com.mpic.evolution.chair.util.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHotDao;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.dao.ProcessMediaByProcedureDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtwork;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.AIVerifyUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.TreeUtil;


/**
 * @author cxd
 */
@Service
public class EcmArtWorkServiceImpl implements EcmArtWorkService {
    @Resource
    EcmArtworkDao ecmArtworkDao;
	@Resource
	EcmUserDao ecmUserDao;
    @Resource
    EcmArtworkNodesDao ecmArtworkNodesDao;
	@Resource
	ProcessMediaByProcedureDao processMediaByProcedureDao;
	@Resource
	EcmArtworkBroadcastHistoryDao ecmArtworkBroadcastHistoryDao;
	@Resource
	EcmArtworkBroadcastHotDao ecmArtworkBroadcastHotDao;
    @Override
    public ResponseDTO getArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {

        return ResponseDTO.ok("success",ecmArtworkDao.selectArtWorks(ecmArtWorkQuery));
    }

    @Override
    public ResponseDTO getArtWork(EcmArtWorkQuery ecmArtWorkQuery) {
        EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtWorkQuery.getPkArtworkId());
        if (ecmArtwork==null){
            return ResponseDTO.fail("查询id为空");
        }

        List<EcmArtworkNodesVo>  list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtWorkQuery.getPkArtworkId());
		List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !"Y".equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());

		if (list.isEmpty()) {
        	return ResponseDTO.fail("查询id无子节点");
		}
        return ResponseDTO.ok("success", TreeUtil.buildTree(collect).get(0));
    }

	@Override
	public ResponseDTO modifyArtWorkStatus(EcmArtworkVo ecmArtworkVo) {
		JSONObject message = this.getMessage();
		try {
			JSONObject condition = this.getCondition();
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtworkVo.getPkArtworkId());
			ecmArtwork.setArtworkStatus(condition.getShort(ecmArtworkVo.getCode()));
			Integer userId = this.getIdByToken(ecmArtworkVo.getToken());
			ecmArtwork.setFkUserid(userId);
			ecmArtwork.setArtworkName(ecmArtworkVo.getArtworkName());
			ecmArtwork.setLastModifyDate(new Date());
			ecmArtworkDao.updateByPrimaryKeySelective(ecmArtwork);
			return ResponseDTO.ok(message.getString(ecmArtworkVo.getCode())+"成功");
		} catch (Exception e) {
			return ResponseDTO.fail(message.getString(ecmArtworkVo.getCode())+"失败");
		}

	}
    @Override
    public ResponseDTO saveArtWorkNode(EcmArtworkNodesVo ecmArtworkNodes) {
		if (ecmArtworkNodes.getFkArtworkId() == null ){
			return ResponseDTO.fail("作品错误");
		}
		EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodes.getFkArtworkId());
		if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodes.getFkUserId())){
			return ResponseDTO.fail("非法访问");
		}


		String videoCode = ecmArtworkNodes.getVideoCode();
    	if(StringUtils.isNotBlank(videoCode)){
			MediaByProcedureVo vo = new MediaByProcedureVo();
			vo.setVideoCode(videoCode);
			List<MediaByProcedureVo> list = processMediaByProcedureDao.getUnHandledVideoByVideoCode(vo);
			if (list == null || list.size() <= 0) {
				vo.setArtworkId(ecmArtworkNodes.getFkArtworkId());
				vo.setVideoId(ecmArtworkNodes.getPkDetailId());
				processMediaByProcedureDao.insertUnHandledVideo(vo);
			}
		}

    	if ("Y".equals(ecmArtworkNodes.getIsleaf())){
			ecmArtworkNodes.setIsleaf("");
			return ResponseDTO.get(1 == ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodes));
		}
    	if (1 == ecmArtworkNodesDao.insert(ecmArtworkNodes)){
			return ResponseDTO.ok("success",ecmArtworkNodes);
		}
       return ResponseDTO.fail("正在保存上一个节点数据");
    }


    @Override
    @Transactional
    public ResponseDTO addArtWork(EcmArtworkNodesVo ecmArtworkNodesVo) {
        // 在 saveArtwork 中 出现 异常后 进行事务回滚操作 并返回错误
        try {
            saveArtwork(ecmArtworkNodesVo);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseDTO.fail("error");
        }

        return ResponseDTO.ok("success");
    }

	@Override
	public ResponseDTO getFindArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {

		List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS= ecmArtworkBroadcastHotDao.selectFindAll(ecmArtWorkQuery);
		if (CollectionUtils.isEmpty(ecmArtworkBroadcastHotVOS)){
			return ResponseDTO.fail("无数据");
		}
		List<EcmArtworkVo> list = ecmArtworkDao.selectFindArtWorks(ecmArtworkBroadcastHotVOS);
		List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);
		list.forEach(ecmArtworkVo -> {
			userVoList.forEach(ecmUserVo -> {
				if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())){
					ecmArtworkVo.setUserName(ecmUserVo.getUsername());
					if (!StringUtils.isEmpty(ecmUserVo.getUserLogoUrl())) {
						ecmArtworkVo.setUserLogoUrl(ecmUserVo.getUserLogoUrl());
					}
				}
			});
			ecmArtworkBroadcastHotVOS.forEach( ecmArtworkBroadcastHotVO -> {
				if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())){
					ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
				}
			});
		});

		EcmArtworkVo ecmArtworkVo = new EcmArtworkVo();
		ecmArtworkVo.setUserName("ad");
		ecmArtworkVo.setLogoPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599039415911&di=41f622b164548552e3e407e5e955b940&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F52%2F52%2F01200000169026136208529565374.jpg");
		ecmArtworkVo.setArtworkName("好产品XXX造");
		ecmArtworkVo.setCode("ad");
		list.add(1,ecmArtworkVo);

    	return ResponseDTO.ok("sucess",list);
	}

	@Override
	public ResponseDTO removeNode(EcmArtworkNodesVo ecmArtworkNodesVo) {
		if (ecmArtworkNodesVo.getFkArtworkId() == null ){
			return ResponseDTO.fail("作品错误");
		}
		EcmArtwork ecmArtwork = ecmArtworkDao.selectByPrimaryKey(ecmArtworkNodesVo.getFkArtworkId());
		if (!ecmArtwork.getFkUserid().equals(ecmArtworkNodesVo.getFkUserId())){
			return ResponseDTO.fail("非法访问");
		}

		if ( ecmArtworkNodesVo.getPkDetailId() != null ){
			return ResponseDTO.get(1 == ecmArtworkNodesDao.removeByPrimaryKey(ecmArtworkNodesVo.getPkDetailId()));
		}
    	return ResponseDTO.fail("网络错误");
	}

	@Override
	public ResponseDTO getFindSortArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {

		List<EcmArtworkVo> list = ecmArtworkDao.selectFindSortArtWorks(ecmArtWorkQuery);
		if (CollectionUtils.isEmpty(list)) {
			return ResponseDTO.fail("无数据");
		}
		List<EcmUserVo> userVoList = ecmUserDao.selectUserByEcmArtworkList(list);
		List<EcmArtworkBroadcastHotVO> ecmArtworkBroadcastHotVOS= ecmArtworkBroadcastHotDao.selectEcmArtworkList(list);
		list.forEach(ecmArtworkVo -> {
			userVoList.forEach(ecmUserVo -> {
				if (ecmUserVo.getPkUserId().equals(ecmArtworkVo.getFkUserid())){
					ecmArtworkVo.setUserName(ecmUserVo.getUsername());
				}

				ecmArtworkBroadcastHotVOS.forEach( ecmArtworkBroadcastHotVO -> {
					if (ecmArtworkBroadcastHotVO.getFkArkworkId().equals(ecmArtworkVo.getPkArtworkId())){
						ecmArtworkVo.setHotCount(ecmArtworkBroadcastHotVO.getBroadcastCount());
					}
				});
				if (ecmArtworkVo.getHotCount() == null){
					ecmArtworkVo.setHotCount(RandomUtil.getCode(3));
				}

			});
		});

		EcmArtworkVo ecmArtworkVo = new EcmArtworkVo();
		ecmArtworkVo.setUserName("ad");
		ecmArtworkVo.setLogoPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599039415911&di=41f622b164548552e3e407e5e955b940&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F52%2F52%2F01200000169026136208529565374.jpg");
		ecmArtworkVo.setArtworkName("好产品XXX造");
		ecmArtworkVo.setCode("ad");
		list.add(1,ecmArtworkVo);

		return ResponseDTO.ok("sucess",list);
	}

	@Override
	public ResponseDTO playArtWork(EcmArtworkVo ecmArtworkVo) {




		List<EcmArtworkNodesVo>  list = ecmArtworkNodesDao.selectByArtWorkId(ecmArtworkVo.getPkArtworkId());
		if (list.isEmpty()) {
			return ResponseDTO.fail("查询id无子节点");
		}
		List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !"Y".equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());

//		ecmArtworkBroadcastHistoryDao.insertSelective()
		//需要添加 播放 历史表 数据
		EcmArtworkBroadcastHistory ecmArtworkBroadcastHistory = new EcmArtworkBroadcastHistory();
		ecmArtworkBroadcastHistory.setFkArtworkId(ecmArtworkVo.getPkArtworkId());

		EcmArtworkBroadcastHot ecmArtworkBroadcastHot = new EcmArtworkBroadcastHot();
		ecmArtworkBroadcastHot.setFkArkworkId(ecmArtworkVo.getPkArtworkId());
		try {
			ecmArtworkBroadcastHotDao.playArtWorkByArtworkId(ecmArtworkVo.getPkArtworkId());
			ecmArtworkBroadcastHistoryDao.insertSelective(ecmArtworkBroadcastHistory);
		}catch (Exception e){

		}
		return ResponseDTO.ok("success", TreeUtil.buildTree(collect).get(0));



	}

	/**
     * @param: [ecmArtworkNodesVo]
     * @return: void
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 EcmArtworkNodesVo 对象的方法
     *       发生异常 会被上级 捕获并回滚
     */
    private void saveArtwork(EcmArtworkNodesVo ecmArtworkNodesVo) {
        //先进行判断是否有主见，没有主键则直接进行插入 并 获取到自增主键

        if ( ecmArtworkNodesVo.getPkDetailId() == null){
//            ecmArtworkNodesVo.set
            ecmArtworkNodesDao.insertSelective(ecmArtworkNodesVo);
        }
        //再 进行 节点 是否还有子节点 判断 有就 先设置 父节点的 id 再 进行循环 并再次 进行  saveArtwork（）方法调用（递归判断）
        if (!CollectionUtils.isEmpty(ecmArtworkNodesVo.getNodesVos()) ){
            ecmArtworkNodesVo.getNodesVos().forEach( node -> {
                node.setParentId(ecmArtworkNodesVo.getPkDetailId());
                saveArtwork(node);
            });
        }
        // 是否为更新节点 判断（ 在节点不是 新建节点时 ，又 进行了节点 更新操作 时 进行更新操作 ）
        if (ecmArtworkNodesVo.getIsleaf() != null){
            // 把前端传回的 标记清空
            ecmArtworkNodesVo.setIsleaf(null);
            ecmArtworkNodesDao.updateByPrimaryKeySelective(ecmArtworkNodesVo);
        }

    }


	@Override
	public ResponseDTO modifyArtWork(EcmArtworkVo ecmArtworkVo) {
		try {
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtworkVo.getPkArtworkId());
			Integer userId = this.getIdByToken(ecmArtworkVo.getToken());
			ecmArtwork.setFkUserid(userId);
			ecmArtwork.setArtworkStatus(ecmArtworkVo.getArtworkStatus());
			String artworkName = ecmArtworkVo.getArtworkName();
			if (StringUtils.isEmpty(artworkName)) {
				ResponseDTO.fail("作品名称不能为空");
			}
			String result = AIVerifyUtil.convertContent(artworkName);
			if (!StringUtils.isEmpty(result)) {
				ResponseDTO.fail("作品名称违规含有违禁词",result,null,510);
			}
			ecmArtwork.setArtworkName(artworkName);
			ecmArtwork.setLogoPathStatus((short)0);
			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
			ecmArtwork.setLastModifyDate(new Date());
 			ecmArtworkDao.updateByPrimaryKey(ecmArtwork);
			return ResponseDTO.ok("作品名称修改成功");
		} catch (Exception e) {
			return ResponseDTO.fail("修改失败");
		}
	}

	@Override
	public ResponseDTO addArtWorks(EcmArtworkVo ecmArtworkVo) {
		try {
			EcmArtwork ecmArtwork = new EcmArtwork();
			ecmArtwork.setPkArtworkId(ecmArtworkVo.getPkArtworkId());
			Integer userId = this.getIdByToken(ecmArtworkVo.getToken());
			ecmArtwork.setFkUserid(userId);
			ecmArtwork.setArtworkName(ecmArtworkVo.getArtworkName());
			ecmArtwork.setArtworkStatus((short)0);
			ecmArtwork.setArtworkDescribe(ecmArtworkVo.getArtworkDescribe());
			ecmArtwork.setFourLetterTips(ecmArtworkVo.getFourLetterTips());
			ecmArtwork.setLastCreateDate(new Date());
			ecmArtwork.setLastModifyDate(new Date());
			ecmArtwork.setLogoPath(ecmArtworkVo.getLogoPath());
			ecmArtworkDao.insert(ecmArtwork);
			EcmArtworkNodes ecmArtworkNodes = new EcmArtworkNodes();
			ecmArtworkNodes.setFkArtworkId(ecmArtwork.getPkArtworkId());
			ecmArtworkNodes.setParentId(0);
			ecmArtworkNodes.setIsleaf("N");
			ecmArtworkNodes.setRevolutionId("x");
			ecmArtworkNodes.setALevel(0);
			ecmArtworkNodesDao.insertSelective(ecmArtworkNodes);
			return ResponseDTO.ok("新建成功");
		} catch (Exception e) {
			return ResponseDTO.fail("新建失败");

		}
	}
	
	private JSONObject getCondition() {
		JSONObject condition = new JSONObject();
		condition.put("delete", 5);
		condition.put("publish", 4);
		condition.put("cancel", 0);
		return condition;
	}

	private JSONObject getMessage() {
		JSONObject message = new JSONObject();
		message.put("delete", "删除");
		message.put("publish", "发布");
		message.put("cancel", "撤销审核");
		return message;
	}
	
	private Integer getIdByToken(String token) {
    	String userIdStr = JWTUtil.getUserId(token);
    	Integer userId = null;
    	if(StringUtils.isNotBlank(userIdStr) && NumberUtils.isParsable(userIdStr)){
    		userId = Integer.parseInt(userIdStr);
    	}
		return userId;
	}

	@Override
	public ResponseDTO getWxUserArtWorks(EcmArtWorkQuery ecmArtWorkQuery) {
		List<EcmArtworkVo> artworks = ecmArtworkDao.selectArtWorksByWxUser(ecmArtWorkQuery);
		artworks.sort((EcmArtworkVo a1,EcmArtworkVo a2)->a2.getLastModifyDate().compareTo(a1.getLastModifyDate()));
		Map<Short, List<EcmArtworkVo>> collect = artworks.stream().collect(Collectors.groupingBy(EcmArtworkVo::getArtworkStatus));
		if (collect.isEmpty()) {
			return ResponseDTO.ok("获取成功", null);
		}
		JSONObject data = new JSONObject();
		collect.forEach((k,v)->{
			if (k == 2) {
				data.put("verfied", collect.get(k));
			}
			if (k == 4) {
				data.put("published", collect.get(k));
			}
		});
		return ResponseDTO.ok("获取成功", data);
	}
	
}

