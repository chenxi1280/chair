package com.mpic.evolution.chair.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpic.evolution.chair.common.constant.JudgeConstant;
import com.mpic.evolution.chair.dao.EcmArtworkDao;
import com.mpic.evolution.chair.dao.EcmArtworkNodesDao;
import com.mpic.evolution.chair.dao.EcmInnerMessageDao;
import com.mpic.evolution.chair.dao.EcmInviteCodeDao;
import com.mpic.evolution.chair.dao.EcmUserExtraflowDao;
import com.mpic.evolution.chair.dao.EcmUserVipDao;
import com.mpic.evolution.chair.dao.WxUserDao;
import com.mpic.evolution.chair.pojo.entity.EcmInnerMessage;
import com.mpic.evolution.chair.pojo.entity.EcmInviteCode;
import com.mpic.evolution.chair.pojo.entity.EcmUserExtraflow;
import com.mpic.evolution.chair.pojo.entity.EcmUserVip;
import com.mpic.evolution.chair.pojo.entity.WxUser;
import com.mpic.evolution.chair.pojo.query.EcmArtWorkQuery;
import com.mpic.evolution.chair.pojo.query.EcmUserExtraflowQuery;
import com.mpic.evolution.chair.pojo.query.EcmUserVipQuery;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkNodesVo;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.util.TreeUtil;

import net.minidev.json.JSONObject;

/** 
* @author 作者 SJ: 
* @date 创建时间：2020-8-20 10:17:08 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SqlTest {
	
	@Autowired
	EcmInnerMessageDao ecmInnerMessageDao;
	
	@Autowired
	EcmArtworkDao ecmArtworkDao;
	
	@Resource
	EcmArtworkNodesDao ecmArtworkNodesDao;
	
	@Resource
    EcmInviteCodeDao ecmInviteCodeDao;
	
	@Resource
    WxUserDao wxUserDao;
	
	@Resource
    EcmUserVipDao ecmUserVipDao;
	
	@Resource
    EcmUserExtraflowDao ecmUserExtraflowDao;
	
	/**
	 * java8 流排序和分组 遍历
	 */
	@Test
	public void selectSqlTest() {
		EcmInnerMessage ecmInnerMessage = new EcmInnerMessage();
		ecmInnerMessage.setFkUserId(1352);
		List<EcmInnerMessage> messages = ecmInnerMessageDao.selectByList(ecmInnerMessage);
		messages = messages.stream().filter((EcmInnerMessage m)->m.getMessageStatus()<2).collect(Collectors.toList());
		messages.sort((EcmInnerMessage m1, EcmInnerMessage m2)->m2.getSendDate().compareTo(m1.getSendDate()));
		Map<Short, Long> sum = messages.stream().collect(Collectors.groupingBy(EcmInnerMessage::getMessageStatus, Collectors.counting()));
		sum.forEach((k,v)->System.out.println(k+":"+v));
	}
	
	/**
	 * java8 流排序和分组 遍历
	 */
	@Test
	public void batchUpdateSqlTest() {
		EcmArtWorkQuery ecmArtWorkQuery = new EcmArtWorkQuery();
		ecmArtWorkQuery.setFkUserid(1352);
		List<EcmArtworkVo> artworks = ecmArtworkDao.selectArtWorksByWxUser(ecmArtWorkQuery);
		artworks.sort((EcmArtworkVo a1,EcmArtworkVo a2)->a2.getLastModifyDate().compareTo(a1.getLastModifyDate()));
//		artworks.forEach(item->System.out.println(item.getLastModifyDate()));
		Map<Short, List<EcmArtworkVo>> collect = artworks.stream().collect(Collectors.groupingBy(EcmArtworkVo::getArtworkStatus));
		JSONObject data = new JSONObject();
		collect.forEach((k,v)->{
			if (k == 2) {
				data.put("已审核", collect.get(k));
			}
			if (k == 4) {
				data.put("已发布", collect.get(k));
			}
		});
		System.out.println("修改数据完成");
	}
	
	/**
	 * java8 流筛选出根节点
	 */
	@Test
	public void selectByArtWorkIdSqlTest() {
		List<EcmArtworkNodesVo> artworkNodes = ecmArtworkNodesDao.selectByArtWorkId(103);
		List<EcmArtworkNodesVo> collect = artworkNodes.stream().filter(item->item.getParentId() == 0).distinct().collect(Collectors.toList());
		if (!collect.isEmpty()) {
			EcmArtworkNodesVo ecmArtworkNodesVo = collect.get(0);
			System.out.println(ecmArtworkNodesVo.getPkDetailId());
		}
	}
	
	/**
	 * java8 查出作品树
	 */
	@Test
	public void selectArtWorkTreeTest() {
		List<EcmArtworkNodesVo> list = ecmArtworkNodesDao.selectByArtWorkId(113);
        List<EcmArtworkNodesVo> collect = list.stream().filter(ecmArtworkNodesVo -> !JudgeConstant.Y.equals(ecmArtworkNodesVo.getIsDeleted())).collect(Collectors.toList());
        EcmArtworkNodesVo ecmArtworkNodesVo = TreeUtil.buildTreeByDetailId(collect, 888).get(0);
        System.out.println(ecmArtworkNodesVo);
	}
	
	/**
	 * 测试邀请码查询sql
	 */
	@Test
	public void selectByEcmInviteCode() {
		EcmInviteCode inviteCode = new EcmInviteCode();
		inviteCode.setInviteCode("123qwe");
		EcmInviteCode ecmInviteCode = ecmInviteCodeDao.selectByEcmInviteCode(inviteCode);
		System.out.println(ecmInviteCode);
	}
	
	/**
	 * 测试openid 查询wxUser sql
	 */
	@Test
	public void selectByWxUser() {
		WxUser user = new WxUser();
		user.setOpenid("opcBH49QSwLe-R5mfVVz4Ilb35DY");
		WxUser selectByWxUser = wxUserDao.selectByWxUser(user);
		System.out.println(selectByWxUser);
	}
	
	/**
	 * 测试vip信息查询sql
	 */
	@Test
	public void selectByVipUser() {
		EcmUserVipQuery vip = new EcmUserVipQuery();
		vip.setFkUserId(1591);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		vip.setCurrentDateTime(pattern.format(now));
		EcmUserVip vipInfo = ecmUserVipDao.selectByVipUser(vip);
		System.out.println(vipInfo);
	}
	
	/**
	 * 测试加油包信息查询sql
	 */
	@Test
	public void selectByExtraflow() {
		EcmUserExtraflowQuery extraflow = new EcmUserExtraflowQuery();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		extraflow.setCurrentDateTime(pattern.format(now));
		extraflow.setFkUserId(1591);
		List<EcmUserExtraflow> selectByExtraflow = ecmUserExtraflowDao.selectByExtraflow(extraflow);
		selectByExtraflow.forEach(item->{
			System.out.println(item);
		});
	}
	
	/**
	 * 获取随机邀请码
	 */
	@Test
	public void getRandom (){
		for (int i = 0; i < 8; i++) {
			String genRandomNum = this.genRandomNum();
			System.out.println(genRandomNum);
		}
	}
	
	private String genRandomNum(){
		int  maxNum = 26;
		int  minNum = 10;
		int i;
		int j;
		int count = 0;
		char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
		    'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
		    'X', 'Y', 'Z'};	
		char[] num = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};	
		StringBuffer pwd1 = new StringBuffer("");
		StringBuffer pwd2 = new StringBuffer("");
		Random r = new Random();
		while(count < 8){
			i = Math.abs(r.nextInt(maxNum));   
			if (i >= 0 && i < str.length) {
				pwd1.append(str[i]);
				count ++;
			}
			j = Math.abs(r.nextInt(minNum));   
			if (j >= 0 && j < num.length) {
				pwd1.append(num[j]);
				count ++;
			}
		}
		return pwd1.toString()+pwd2.toString();
	}

}
