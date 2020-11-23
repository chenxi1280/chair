package com.mpic.evolution.chair.controller;


import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.mpic.evolution.chair.common.constant.PublishConstants;
import com.mpic.evolution.chair.common.returnvo.ErrorEnum;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodeNumberConditionVO;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.HttpMpicUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.RedisUtil;
import com.qcloud.vod.common.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
		Integer userIdByHandToken = getUserIdByHandToken();
		if (userIdByHandToken == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		ecmArtWorkQuery.setFkUserid(userIdByHandToken);
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

		Integer userIdByHandToken = getUserIdByHandToken();
		if (userIdByHandToken == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		ecmArtWorkQuery.setFkUserid(userIdByHandToken);

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
		Integer userId = getUserIdByHandToken();

		if (userId== null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
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
		ecmArtworkNodes.setFkUserId(userId);

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
		String token = getRequest().getHeader("Authorization");
		if (StringUtil.isEmpty(token)){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
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
		Integer userId = getUserIdByHandToken();
		if (userId == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		ecmArtworkNodesVo.setFkUserId(userId);

		return ecmArtWorkService.removeNode(ecmArtworkNodesVo);
	}

	@RequestMapping("/saveArtworkNodeNumberCondition")
	@ResponseBody
	public ResponseDTO saveArtworkNodeNumberCondition(@RequestBody EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO){
		Integer userId = getUserIdByHandToken();
		if (userId == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		ecmArtworkNodeNumberConditionVO.setFkUserId(userId);

		return ecmArtWorkService.saveArtworkNodeNumberCondition(ecmArtworkNodeNumberConditionVO);
	}

	@RequestMapping("/saveAllNodeNameFlagChange")
	@ResponseBody
	public ResponseDTO saveAllNodeNameFlagChange(@RequestBody EcmArtworkNodeNumberConditionVO ecmArtworkNodeNumberConditionVO){
		Integer userId = getUserIdByHandToken();
		if (userId == null){
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
		ecmArtworkNodeNumberConditionVO.setFkUserId(userId);

		return ecmArtWorkService.saveAllNodeNameFlagChange(ecmArtworkNodeNumberConditionVO);
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
			return ResponseDTO.fail(ErrorEnum.ERR_603.getText());
		}
    	//如果是null返回false
    	boolean hasKey = redisUtil.hasKey("QRCode");
    	String accessToken = "";
    	try {
	    	if (hasKey) {
	    		accessToken = String.valueOf(redisUtil.get("QRCode"));
			}else {
				accessToken = getAccessToken();
			}
	    	String qrCodeStr = this.getQRCode(accessToken,ecmArtWorkQuery,data);
			if (HttpMpicUtil.isJsonObject(qrCodeStr)) {
				//返回的结果是：{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest rid: 5f364b21-395edb8d-336ae042"}
				JSONObject result = JSONObject.parseObject(qrCodeStr);
				if(result.get("errcode").equals("40001")) {
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

    private String getQRCode(String accessToken,EcmArtWorkQuery ecmArtWorkQuery,JSONObject data) {
    	String url = String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?"
        		+ "access_token=%s", accessToken);
        JSONObject param = new JSONObject();
        param.put("page","pages/play/play");
        String artWorkId = ecmArtWorkQuery.getArtWorkId();
        //scene的value 是 videoId
        String codeType = ecmArtWorkQuery.getCodeType();
        String string = "";
        if (codeType.equals("0")) {
        	string = "artWorkId="+artWorkId+"=status=1";
		}else {
			string = "artWorkId="+artWorkId+"=status=4";
	        data.put("path","pages/play/play?"+"pkArtworkId="+artWorkId);
		}
        param.put("scene",string);
		String Base64Str = HttpMpicUtil.sendPostForBase64(url, param);
		return Base64Str;
    }

	/**
     * 	通过请求获取accessToken
     *	用户登陆时的token 解密出来的用户id
     * 	此处要根据过期时间，存redis，有人请求，先从redis里面拿token，没有再请求
     *
     */
    private String getAccessToken() {
    	String requestUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?"
    			+ "grant_type=client_credential&appid=%s&secret=%s", PublishConstants.appid, PublishConstants.secret);
        //将返回的access_token 存入redis 过期时间3000秒
    	String jsonStr = HttpKit.get(requestUrl);
    	JSONObject result = JSONObject.parseObject(jsonStr);
    	String accessToken = result.getString("access_token");
    	redisUtil.set("QRCode", accessToken, 3000L);
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
