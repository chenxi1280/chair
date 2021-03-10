package com.mpic.evolution.chair.controller;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.mpic.evolution.chair.common.constant.PublishConstants;
import com.mpic.evolution.chair.common.constant.TiktokConstant;
import com.mpic.evolution.chair.common.exception.TokenException;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.query.EcmArtworkEndingsQuery;
import com.mpic.evolution.chair.pojo.query.EcmArtworkNodeBuoyQuery;
import com.mpic.evolution.chair.pojo.vo.*;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.HttpMpicUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.RedisUtil;
import com.qcloud.vod.common.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import static com.mpic.evolution.chair.common.constant.CommonField.NODE_ENDING_MAX;
import static com.mpic.evolution.chair.common.constant.CommonField.STRING_ZORE;

/**
 * @author cxd
 */
@Controller
@RequestMapping("/Ecmartwork")
public class EcmArtWorkController extends BaseController{

    @Resource
    EcmArtWorkService ecmArtWorkService;

    @Resource
	RedisUtil redisUtil;




	@RequestMapping("/updateNodeInfo")
	@ResponseBody
	public ResponseDTO updateNodeInfo(){
		return ecmArtWorkService.updateNodeInfo();
	}

	@RequestMapping("/insertNodeInfo")
	@ResponseBody
	public void insertNodeInfo(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){
		ecmArtWorkService.insertNodeInfo(ecmArtworkNodesVo);
	}

    /**
     * @param: [ecmArtWorkQuery] 传入的 查询参数 token
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: SJ
     * @Date: 2020/8/5
     * 	描述 : 按照条件查询作品
     *  	保存成功: status 200  msg "success” data: 数据
     *     	保存失败: status 500  msg "error“
     *
     */
    @RequestMapping("/getArtWorks")
    @ResponseBody
    public ResponseDTO getArtWorks(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		ecmArtWorkQuery.setFkUserid(getUserIdByHandToken());
//		ecmArtWorkQuery.setPage();
//		ecmArtWorkQuery.setLimit(20);
        return ecmArtWorkService.getArtWorks(ecmArtWorkQuery);
    }


    /**
     * @param: [ecmArtWorkQuery] 需要 作品id 必传
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 查询作品详情根据 作品id
     *       保存成功: status 200  msg "success”  data: 数据
     *       保存失败: status 500  msg "error“
     */
    @RequestMapping("/getArtWork")
    @ResponseBody
    public ResponseDTO getArtWork(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		ecmArtWorkQuery.setFkUserid(getUserIdByHandToken());
		return ecmArtWorkService.getArtWork(ecmArtWorkQuery);
    }

    /**
     * @param: [ecmArtworkNodes] 单个节点类
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 作品单个节点 ArtWork 接口
     *       保存成功： status 200  msg “success”
     *       保存失败： status 500  msg ”error“
     */
    @ResponseBody
    @RequestMapping("/saveArtWorkNode")
    public ResponseDTO saveArtWorkNod(@RequestBody EcmArtworkNodesVo ecmArtworkNodes){

		if (ecmArtworkNodes.getParentId() == null){
            return ResponseDTO.fail("父节点id为空");
        }
		if (ecmArtworkNodes.getFkArtworkId() == null) {
			return ResponseDTO.fail("作品错误");
		}
		int maxCount = 50;
		if (StringUtils.isNotBlank(ecmArtworkNodes.getRevolutionId()) && ecmArtworkNodes.getRevolutionId().length() > maxCount) {
			return ResponseDTO.fail("视频最多编排50层");
		}
		ecmArtworkNodes.setFkUserId(getUserIdByHandToken());

        return ecmArtWorkService.saveArtWorkNode(ecmArtworkNodes);
    }


    /**
     * @param: [ecmArtworkNodesVo]  json 格式的 作品详情类（树状）
     * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
     * @author: cxd
     * @Date: 2020/8/5
     * 描述 : 保存 作品(所有) ArtWork 接口
     *       保存成功: status 200  msg "success”
     *       保存失败: status 500  msg "error“
     */
    @RequestMapping("/addArtWork")
    @ResponseBody
    public ResponseDTO addArtWork(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){
		ecmArtworkNodesVo.setFkUserId(getUserIdByHandToken());
        return ecmArtWorkService.addArtWork(ecmArtworkNodesVo);
    }

	/**
	 * @param: [ecmArtworkNodesVo] 节点id 请求头token 作品id
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/7
	 * 描述 : 删除节点
	 *       成功: status 200  msg "success”
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/removeNode")
	@ResponseBody
	public ResponseDTO removeNode(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){
		ecmArtworkNodesVo.setFkUserId(getUserIdByHandToken());

		return ecmArtWorkService.removeNode(ecmArtworkNodesVo);
	}

	/**
	 * @param: [ecmArtworkNodeNumberConditionVO]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/2
	 * 描述 : 保存数值选项 ~.~
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkNodeNumberCondition")
	@ResponseBody
	public ResponseDTO saveArtworkNodeNumberCondition(@RequestBody EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO){
		ecmArtworkNodeNumberConditionVO.setFkUserId(getUserIdByHandToken());

		return ecmArtWorkService.saveArtworkNodeNumberCondition(ecmArtworkNodeNumberConditionVO);
	}
	/**
	 * @param: [ecmArtworkNodeNumberConditionVO]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/2
	 * 描述 : 保存全局数值选项 ~.~
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveAllNodeNameFlagChange")
	@ResponseBody
	public ResponseDTO saveAllNodeNameFlagChange(@RequestBody EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO){
		ecmArtworkNodeNumberConditionVO.setFkUserId(getUserIdByHandToken());

		return ecmArtWorkService.saveAllNodeNameFlagChange(ecmArtworkNodeNumberConditionVO);
	}

	/**
	 * @param: [ecmArtworkEndingsQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/4
	 * 描述 :saveArtworkEndings 保存多结局信息
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkEndings")
	@ResponseBody
	public ResponseDTO saveArtworkEndings(@RequestBody EcmArtworkEndingsQuery ecmArtworkEndingsQuery){
		Integer userId = getUserIdByHandToken();
		ecmArtworkEndingsQuery.setFkUserId(getUserIdByHandToken());
		String saveArtworkEndings = "saveArtworkEndings";
		Long o = (Long) redisUtil.get(userId + saveArtworkEndings);
		if ( o != null) {
			int three = 3;
			if ( o <= (System.currentTimeMillis() - three)){
				return ResponseDTO.fail("保存频繁",null,480,480);
			}
		}

		if (CollectionUtils.isEmpty(ecmArtworkEndingsQuery.getEcmArtworkEndingsVOS()) && ecmArtworkEndingsQuery.getEcmArtworkEndingsVOS().size() > NODE_ENDING_MAX ){
			return ResponseDTO.fail("结局数过多",null,490,490);
		}
		redisUtil.set(userId + saveArtworkEndings ,System.currentTimeMillis() );
		return ecmArtWorkService.saveArtworkEndings(ecmArtworkEndingsQuery);
	}

	/**
	 * @param: [ecmArtworkVo]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/4
	 * 描述 : 保存多结局 作品状态
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkEndingState")
	@ResponseBody
	public ResponseDTO saveArtworkEndingState(@RequestBody EcmArtworkVo ecmArtworkVo){
		ecmArtworkVo.setFkUserid(getUserIdByHandToken());

		return ecmArtWorkService.saveArtworkEndingState(ecmArtworkVo);
	}
	/**
	 * @param: [ecmArtworkEndingsQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : 保存多节点集合
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkEndingList")
	@ResponseBody
	public ResponseDTO saveArtworkEndingList(@RequestBody EcmArtworkEndingsQuery ecmArtworkEndingsQuery){
		ecmArtworkEndingsQuery.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.saveArtworkEndingList(ecmArtworkEndingsQuery);
	}

	/**
	 * @param: [ecmArtworkEndingsQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 :  多结局 多节点 更新
	 *
	 */
	@RequestMapping("updateArtworkEndingList")
	@ResponseBody
	public ResponseDTO updateArtworkEndingList(@RequestBody EcmArtworkEndingsQuery ecmArtworkEndingsQuery){
		ecmArtworkEndingsQuery.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.updateArtworkEndingList(ecmArtworkEndingsQuery);
	}

	/**
	 * @param: [ecmArtworkEndingsQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : 多结局 节点 删除
	 */
	@RequestMapping("/deleteArtworkEnding")
	@ResponseBody
	public ResponseDTO deleteArtworkEnding(@RequestBody EcmArtworkEndingsQuery ecmArtworkEndingsQuery){
		ecmArtworkEndingsQuery.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.deleteArtworkEnding(ecmArtworkEndingsQuery);
	}

	/**
	 * @param: [ecmArtworkEndingsQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : 获取多结局 list
	 *       成功: status 200  msg "success”   date:ecmArtworkEndingsVOList
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/getArtworkEndingList")
	@ResponseBody
	public ResponseDTO getArtworkEndingList(@RequestBody EcmArtworkEndingsQuery ecmArtworkEndingsQuery){
		ecmArtworkEndingsQuery.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.getArtworkEndingList(ecmArtworkEndingsQuery);
	}

	/**
	 * @param: [ecmArtworkEndingsQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : saveArtworkEndingAll 全删 全存 多结局
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkEndingAll")
	@ResponseBody
	public ResponseDTO saveArtworkEndingAll(@RequestBody EcmArtworkEndingsQuery ecmArtworkEndingsQuery){

		if (ecmArtworkEndingsQuery.getNodeNum() == null  ) {
			return ResponseDTO.fail("无多结局");
		}

		if (ecmArtworkEndingsQuery.getNodeNum().length > NODE_ENDING_MAX){
			return ResponseDTO.fail("结局数过多",null,490,490);
		}

		ecmArtworkEndingsQuery.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.saveArtworkEndingAll(ecmArtworkEndingsQuery);
	}

	/**
	 * @param: [ecmArtworkEndingsQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : 多删除
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/deleteArtworkEndingList")
	@ResponseBody
	public ResponseDTO deleteArtworkEndingList(@RequestBody EcmArtworkEndingsQuery ecmArtworkEndingsQuery){

		ecmArtworkEndingsQuery.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.deleteArtworkEndingList(ecmArtworkEndingsQuery);
	}

	/**
	 * @param: [ecmArtworkNodePopupSettingsVO]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2021/2/20
	 * 描述 : 保存节点 弹窗设置
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkNodePopupSettings")
	@ResponseBody
	public ResponseDTO saveArtworkNodePopupSettings(@RequestBody EcmArtworkNodePopupSettingsVO ecmArtworkNodePopupSettingsVO){
		ecmArtworkNodePopupSettingsVO.setFkUserId(getUserIdByHandToken());

		return ecmArtWorkService.saveArtworkNodePopupSettings(ecmArtworkNodePopupSettingsVO);
	}

	/**
	 * @param: [ecmArtworkNodesVo]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2021/2/20
	 * 描述 : 保存节点条件设置
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkNodeCondition")
	@ResponseBody
	public ResponseDTO saveArtworkNodeCondition(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){

		ecmArtworkNodesVo.setFkUserId(getUserIdByHandToken());

		return ecmArtWorkService.saveArtworkNodeCondition(ecmArtworkNodesVo);
	}

	/**
	 * @param: [ecmArtworkNodesVo]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2021/2/20
	 * 描述 : 保存节点多结局条件
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/saveArtworkEndingCondition")
	@ResponseBody
	public ResponseDTO saveArtworkEndingCondition(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){
		ecmArtworkNodesVo.setFkUserId(getUserIdByHandToken());

		return ecmArtWorkService.saveArtworkEndingCondition(ecmArtworkNodesVo);
	}

	@RequestMapping("/saveArtworkNodeBuoy")
	@ResponseBody
	public ResponseDTO saveArtworkNodeBuoy(@RequestBody EcmArtworkNodeBuoyQuery ecmArtworkNodeBuoyQuery){
		ecmArtworkNodeBuoyQuery.setFkUserId(getUserIdByHandToken());

		return ecmArtWorkService.saveArtworkNodeBuoy(ecmArtworkNodeBuoyQuery);
	}
	/**
	 * @param: [ecmArtworkNodeBuoyVO]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2021/2/20
	 * 描述 :
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/deleteArtworkNodeBuoy")
	@ResponseBody
	public ResponseDTO deleteArtworkNodeBuoy(@RequestBody EcmArtworkNodeBuoyVO ecmArtworkNodeBuoyVO){

		ecmArtworkNodeBuoyVO.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.deleteArtworkNodeBuoy(ecmArtworkNodeBuoyVO);
	}

	@RequestMapping("/getArtworkNodeBuoy")
	@ResponseBody
	public ResponseDTO getArtworkNodeBuoy(@RequestBody EcmArtworkNodeBuoyQuery ecmArtworkNodeBuoyQuery){

		ecmArtworkNodeBuoyQuery.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.getArtworkNodeBuoy(ecmArtworkNodeBuoyQuery);
	}

	@RequestMapping("/updateArtworkNodeBuoy")
	@ResponseBody
	public ResponseDTO updateArtworkBuoy(@RequestBody EcmArtworkNodesVo ecmArtworkNodesVo){

		ecmArtworkNodesVo.setFkUserId(getUserIdByHandToken());
		return ecmArtWorkService.updateArtworkNodeBuoy(ecmArtworkNodesVo);
	}

    /**
     * 	获取发布微信二维码
     * 	这里面的 scene 参数是前台要传过来的videoId
     *
     * @author: SJ
     * @Date: 2020/8/14
     * @param ecmArtWorkQuery token
     */
    @RequestMapping("/getWxcode")
    @ResponseBody
    public ResponseDTO getWxcode (@RequestBody EcmArtWorkQuery ecmArtWorkQuery) {
    	String token = ecmArtWorkQuery.getToken();
    	String userId = JWTUtil.getUserId(token);
    	JSONObject data = new JSONObject();
    	if (StringUtil.isEmpty(userId)){
			throw new TokenException(603,"非法访问");
		}
    	//如果是null返回false
    	boolean hasKey = redisUtil.hasKey("WxQRCode");
    	String accessToken = "";
    	try {
	    	if (hasKey) {
	    		accessToken = String.valueOf(redisUtil.get("WxQRCode"));
			}else {
				accessToken = getAccessToken();
			}
	    	String qrCodeStr = this.getQRCode(accessToken,ecmArtWorkQuery,data);
			if (HttpMpicUtil.isJsonObject(qrCodeStr)) {
				//返回的结果是：{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest rid: 5f364b21-395edb8d-336ae042"}
				JSONObject result = JSONObject.parseObject(qrCodeStr);
				// result.get("errcode").equals("40001")
				String error= "40001";
				String errorCode = "errcode";
				if( error.equals(result.get(errorCode))) {
					accessToken = getAccessToken();
					qrCodeStr = this.getQRCode(accessToken,ecmArtWorkQuery,data);
					String str = "data:image/jpg;base64," + qrCodeStr;
					data.put("qrCodeStr", str);
					return ResponseDTO.ok("获取发布二维码成功",data);
				}else {
					return ResponseDTO.fail("获取发布二维码失败", result.get("errmsg"),null,(Integer)result.get("errcode"));
				}
			}else {
				String str = "data:image/jpg;base64," + qrCodeStr;
				data.put("qrCodeStr", str);
				return ResponseDTO.ok("获取发布二维码成功",data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("获取二维码图片失败");
		}
    }

	/**
	 * @param: [ecmArtWorkQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : 获取抖音二维码
	 */
    @RequestMapping("/getDycode")
	@ResponseBody
	public ResponseDTO getDyCode (@RequestBody EcmArtWorkQuery ecmArtWorkQuery) {

		Integer userIdByHandToken = getUserIdByHandToken();

		//如果是null返回false
		JSONObject data = new JSONObject();
		boolean hasKey = redisUtil.hasKey("DyQRCode");
		String accessToken = "";
		try {
			if (hasKey) {
				accessToken = String.valueOf(redisUtil.get("DyQRCode"));
			}else {
				accessToken = getDyAccessToken();
			}
			String qrCodeStr = this.getDyQRCode(accessToken,ecmArtWorkQuery,data);
			if (HttpMpicUtil.isJsonObject(qrCodeStr)) {
				//返回的结果是：{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest rid: 5f364b21-395edb8d-336ae042"}
				JSONObject result = JSONObject.parseObject(qrCodeStr);
				//result.get("errcode").equals("40002")
				String error= "40002";
				String errorCode = "errcode";
				if( error.equals(result.get(errorCode)) ) {
					accessToken = getDyAccessToken();
					qrCodeStr = this.getDyQRCode(accessToken,ecmArtWorkQuery,data);
					String str = "data:image/jpg;base64," + qrCodeStr;
					data.put("qrCodeStr", str);
					return ResponseDTO.ok("获取发布二维码成功",data);
				}else {
					return ResponseDTO.fail("获取发布二维码失败", result.get("errmsg"),null,(Integer)result.get("errcode"));
				}
			}else {
				String str = "data:image/jpg;base64," + qrCodeStr;
				data.put("qrCodeStr", str);
				return ResponseDTO.ok("获取发布二维码成功",data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("获取二维码图片失败");
		}
	}

	@RequestMapping("/getWXBuoyPreviewCode")
	@ResponseBody
	public ResponseDTO getWXBuoyPreviewCode (@RequestBody EcmArtWorkQuery ecmArtWorkQuery) {

		Integer userIdByHandToken = getUserIdByHandToken();


		JSONObject data = new JSONObject();

		//如果是null返回false
		boolean hasKey = redisUtil.hasKey("WxQRCode");
		String accessToken = "";
		try {
			if (hasKey) {
				accessToken = String.valueOf(redisUtil.get("WxQRCode"));
			}else {
				accessToken = getAccessToken();
			}
			String qrCodeStr = this.getBuoyPreviewQRCode(accessToken,ecmArtWorkQuery,data);
			if (HttpMpicUtil.isJsonObject(qrCodeStr)) {
				//返回的结果是：{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest rid: 5f364b21-395edb8d-336ae042"}
				JSONObject result = JSONObject.parseObject(qrCodeStr);
				// result.get("errcode").equals("40001")
				String error= "40001";
				String errorCode = "errcode";
				if( error.equals(result.get(errorCode))) {
					accessToken = getAccessToken();
					qrCodeStr = this.getBuoyPreviewQRCode(accessToken,ecmArtWorkQuery,data);
					String str = "data:image/jpg;base64," + qrCodeStr;
					data.put("qrCodeStr", str);
					return ResponseDTO.ok("获取发布二维码成功",data);
				}else {
					return ResponseDTO.fail("获取发布二维码失败", result.get("errmsg"),null,(Integer)result.get("errcode"));
				}
			}else {
				String str = "data:image/jpg;base64," + qrCodeStr;
				data.put("qrCodeStr", str);
				return ResponseDTO.ok("获取发布二维码成功",data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("获取二维码图片失败");
		}
	}

	private String getBuoyPreviewQRCode(String accessToken,EcmArtWorkQuery ecmArtWorkQuery,JSONObject data) {
		String url = String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?"
				+ "access_token=%s", accessToken);
		JSONObject param = new JSONObject();
		param.put("page","pages/play/buoyPreviewPlay");
//		String artWorkId = ecmArtWorkQuery.getArtWorkId();
		//scene的value 是 videoId
//		String codeType = ecmArtWorkQuery.getCodeType();
		String string = "artWorkId="+ecmArtWorkQuery.getArtWorkId()+"=pkDetailId="+ecmArtWorkQuery.getPkDetailId();
		param.put("scene",string);
		String base64Str = HttpMpicUtil.sendPostForBase64(url, param);
		return base64Str;
	}

	/**
	 * @param: [accessToken, ecmArtWorkQuery, data]
	 * @return: java.lang.String
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : 微信获取二维码
	 */
    private String getQRCode(String accessToken,EcmArtWorkQuery ecmArtWorkQuery,JSONObject data) {
    	String url = String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?"
        		+ "access_token=%s", accessToken);
        JSONObject param = new JSONObject();
        param.put("page","pages/play/play");
        String artWorkId = ecmArtWorkQuery.getArtWorkId();
        //scene的value 是 videoId
        String codeType = ecmArtWorkQuery.getCodeType();
        String string = "";

        if ( STRING_ZORE.equals(codeType)) {
        	string = "artWorkId="+artWorkId+"=status=1";
		}else {
			string = "artWorkId="+artWorkId+"=status=4";
	        data.put("path","pages/play/play?"+"pkArtworkId="+artWorkId);
		}
        param.put("scene",string);
		String base64Str = HttpMpicUtil.sendPostForBase64(url, param);
		return base64Str;
    }

	/**
	 * 	通过请求获取accessToken
	 *	用户登陆时的token 解密出来的用户id
	 * 	此处要根据过期时间，存redis，有人请求，先从redis里面拿token，没有再请求
	 *
	 */
	private String getAccessToken() {
		String requestUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?"
				+ "grant_type=client_credential&appid=%s&secret=%s", PublishConstants.APP_ID, PublishConstants.SECRET);
		//将返回的access_token 存入redis 过期时间3000秒
		String jsonStr = HttpKit.get(requestUrl);
		JSONObject result = JSONObject.parseObject(jsonStr);
		String accessToken = result.getString("access_token");
		String expiresIn = result.getString("expires_in");
		redisUtil.set("WxQRCode", accessToken, Long.parseLong(expiresIn));
		return accessToken;
	}



	/**
	 * @param: [accessToken, ecmArtWorkQuery, data]
	 * @return: java.lang.String
	 * @author: cxd
	 * @Date: 2020/12/25
	 * 描述 : 抖音获取二维码
	 */
	private String getDyQRCode(String accessToken,EcmArtWorkQuery ecmArtWorkQuery,JSONObject data) {
		String url = String.format("https://developer.toutiao.com/api/apps/qrcode");
		JSONObject param = new JSONObject();
		param.put("access_token",accessToken);
		param.put("path","pages/play/play");
		String artWorkId = ecmArtWorkQuery.getArtWorkId();
		//scene的value 是 videoId
		String codeType = ecmArtWorkQuery.getCodeType();
		String string = "";
		//codeType.equals("0")
		if (STRING_ZORE.equals(codeType)) {
			string = "artWorkId="+artWorkId+"=status=1";
		}else {
			string = "artWorkId="+artWorkId+"=status=4";
			data.put("path","pages/play/play?"+"pkArtworkId="+artWorkId);
		}
		param.put("scene",string);
		param.put("appname","douyin");

		String post = HttpMpicUtil.post(param, url );

		return post;
	}
	private String getDyAccessToken() {
		String requestUrl = String.format("https://developer.toutiao.com/api/apps/token?"
				+ "grant_type=client_credential&appid=%s&secret=%s", TiktokConstant.APP_ID, TiktokConstant.SECRET);
		//将返回的access_token 存入redis 过期时间3000秒
		String jsonStr = HttpKit.get(requestUrl);
		JSONObject result = JSONObject.parseObject(jsonStr);
		String accessToken = result.getString("access_token");
		String expiresIn = result.getString("expires_in");
		redisUtil.set("DyQRCode", accessToken, Long.valueOf(expiresIn));
		return accessToken;
	}

	// 下面小程序端接口



    /**
	 * @param: [ecmArtWorkQuery] 自带分页
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/8/26
	 * 描述 :  小程序端
	 * 			获取发现页面的热门数据 按照热门 HOT表中热度进行排序
	 *       成功: status 200  msg "success” data List<EcmartWorkVO>
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/getFindArtWorks")
	@ResponseBody
	public ResponseDTO getFindArtWorks(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		return ecmArtWorkService.getFindArtWorks(ecmArtWorkQuery);
	}

	/**
	 * @param: [ecmArtWorkQuery] 自带分页
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/7
	 * 描述 : 小程序端
	 * 		获取发现页面的分类数据 按照热门 HOT表中热度进行排序
	 *       成功: status 200  msg "success” data List<EcmartWorkVO>
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/getFindSortArtWorks")
	@ResponseBody
	public ResponseDTO getFindSortArtWorks(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		return ecmArtWorkService.getFindSortArtWorks(ecmArtWorkQuery);
	}

	/**
	 * @param: [ecmArtWorkQuery] 搜索页面热度榜
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/7
	 * 描述 : 小程序端
	 *       成功: status 200  msg "success”   date: List<EcmartWorkVO>
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/getRankingArtWorks")
	@ResponseBody
	public ResponseDTO getRankingArtWorks(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		return ecmArtWorkService.getRankingArtWorks(ecmArtWorkQuery);
	}

	/**
	 * @param: [ecmArtWorkQuery] searchText 搜索文本
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/8
	 * 描述 : 小程序端
	 * 		搜索结果
	 *       成功: status 200  msg "success”   date: List<EcmArtworkVo>
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/search")
	@ResponseBody
	public ResponseDTO search(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		return ecmArtWorkService.search(ecmArtWorkQuery);
	}

	/**
	 * @param: [ecmArtWorkQuery]
	 * @return: com.mpic.evolution.chair.pojo.dto.ResponseDTO
	 * @author: cxd
	 * @Date: 2020/9/26
	 * 描述 : 故事线获取接口，通过作品id查询 整个 作品节点，并找到兄弟节点
	 *       成功: status 200  msg "success”   date:
	 *       失败: status 500  msg "error“
	 */
	@RequestMapping("/getArtWorkNodes")
	@ResponseBody
	public ResponseDTO getArtWorkNodes(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
		return ecmArtWorkService.getArtWorkNodes(ecmArtWorkQuery);
	}


}
