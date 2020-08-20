package com.mpic.evolution.chair.controller;


import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import com.mpic.evolution.chair.common.constant.publishConstants;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkNodes;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.service.EcmArtWorkService;
import com.mpic.evolution.chair.util.HttpMpicUtil;
import com.mpic.evolution.chair.util.JWTUtil;
import com.mpic.evolution.chair.util.RedisUtil;

/**
 * @author cxd
 */
@Controller
@RequestMapping("/Ecmartwork")
public class EcmArtWorkController {

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
     *       保存成功: status 200  msg "success” data: 数据
     *       保存失败: status 500  msg "error“
     *      
     */
    @RequestMapping("/getArtWorks")
    @ResponseBody
    public ResponseDTO getArtWorks(@RequestBody EcmArtWorkQuery ecmArtWorkQuery){
    	String token = ecmArtWorkQuery.getToken();
    	String userIdStr = JWTUtil.getUserId(token);
    	if(StringUtils.isBlank(userIdStr) || !NumberUtils.isParsable(userIdStr)){
    		return ResponseDTO.fail("获取作品失败");
    	}
    	Integer userId = Integer.parseInt(userIdStr);
    	ecmArtWorkQuery.setFkUserid(userId);
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
    public ResponseDTO saveArtWorkNod(@RequestBody EcmArtworkNodes ecmArtworkNodes){
        if (ecmArtworkNodes.getParentId() == null){
            return ResponseDTO.fail("父节点id为空");
        }
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
        return ecmArtWorkService.addArtWork(ecmArtworkNodesVo);
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
    	try {
	    	String userId = JWTUtil.getUserId(ecmArtWorkQuery.getToken());
	    	boolean hasKey = redisUtil.hasKey(userId);
	    	String accessToken = "";
	    	//如果是null返回false
	    	if (hasKey) {
	    		accessToken = String.valueOf(redisUtil.get(userId));
			}else {
				accessToken = getAccessToken(userId);
			}
	        String url = String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?"
	        		+ "access_token=%s", accessToken);
	        JSONObject param = new JSONObject();
	        param.put("page","pages/play/play");
	        String videoId = ecmArtWorkQuery.getVideoId();
	        //scene的value 是 videoId
	        param.put("scene",videoId);
			String Base64Str = HttpMpicUtil.sendPostForBase64(url, param);
			if (HttpMpicUtil.isJsonObject(Base64Str)) {
				//返回的结果是：{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest rid: 5f364b21-395edb8d-336ae042"}
				JSONObject result = JSONObject.parseObject(Base64Str);
				return ResponseDTO.fail("获取发布二维码失败", result.get("errmsg"),null,(Integer)result.get("errcode"));
			}else {
				String str = "data:image/jpg;base64," + Base64Str;
				return ResponseDTO.ok("获取发布二维码成功",str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.fail("获取二维码图片失败");
		}
    } 

	/**
     * 	通过请求获取accessToken
     *	用户登陆时的token 解密出来的用户id
     * 	此处要根据过期时间，存redis，有人请求，先从redis里面拿token，没有再请求
     *
     */
    private String getAccessToken(String userId) {
    	String requestUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?"
    			+ "grant_type=client_credential&appid=%s&secret=%s", publishConstants.appid,publishConstants.secret);
        //将返回的access_token 存入redis 过期时间3000秒
    	String jsonStr = HttpKit.get(requestUrl);
    	JSONObject result = JSONObject.parseObject(jsonStr);
    	String accessToken = result.getString("access_token");
    	redisUtil.set(userId, accessToken, 3000L);
        return accessToken;
    }   

}
