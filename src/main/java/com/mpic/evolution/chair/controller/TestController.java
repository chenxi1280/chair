package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.dao.EcmArtworkBroadcastHistoryDao;
import com.mpic.evolution.chair.pojo.dto.ResponseDTO;
import com.mpic.evolution.chair.pojo.entity.EcmArtworkBroadcastHistory;
import com.mpic.evolution.chair.pojo.entity.EcmGoods;
import com.mpic.evolution.chair.pojo.vo.EcmArtworkVo;
import com.mpic.evolution.chair.pojo.vo.EcmOrderVO;
import com.mpic.evolution.chair.service.*;
import com.mpic.evolution.chair.service.vip.BeanConfig;
import com.mpic.evolution.chair.service.vip.PaymentVipService;
import com.mpic.evolution.chair.util.RedisLock;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
* @author 作者 SJ:
* @date 创建时间：2020-10-8 17:12:42
*/

@Controller
@RequestMapping("/test")
public class TestController extends BaseController{

	@Resource
	private RedisLock redisLock;

	@Resource
	private EcmVipService ecmVipService;

	@Resource
	private BeanConfig beanConfig;

	@Resource
	private EcmOrderService ecmOrderService;

	@Resource
	private EcmGoodsService ecmGoodsService;

    @Resource
    NoticeService noticeService;

    @Resource
    EcmArtworkBroadcastHistoryDao historyDao;

    @Resource
    DeleteUselessMediaInformationService deleteService;

    @Resource
    EcmDownLinkFlowService ecmDownLinkFlowService;

//	private int count = 0;

	/*@RequestMapping("/testRedisLock")
	@ResponseBody
	public void  testRedisLock() throws InterruptedException {
		int clientcount = 100000;
		CountDownLatch countDownLatch = new CountDownLatch(clientcount);

		long start = System.currentTimeMillis();
		for (int i = 0; i < clientcount; i++) {
			threadPool.execute(()->{
				String id = UUID.randomUUID().toString();
				try {
					redisLock.lock(id);
					count++;
					System.out.println("count值为："+ count);
				} finally {
					redisLock.unlock(id);
				}
				countDownLatch.countDown();
			});
		}
		countDownLatch.await();
		long end = System.currentTimeMillis();
		System.out.println(String.format("执行线程数:%d,总耗时:%d,count数为:%d",1000,end-start,count));
	}*/

//	@RequestMapping("/testVip")
//	@ResponseBody
//	public ResponseDTO testVip() {
//		JSONObject userVipInfo = ecmVipService.getUserVipInfo(1617);
//		return ResponseDTO.ok(userVipInfo);
//	}
	@RequestMapping("/testVip")
	public void savaVipPaymentInfo(String orderCode) throws Exception{
		EcmOrderVO ecmOrderVO = ecmOrderService.queryOrderInfo(orderCode);
		EcmGoods goods = ecmGoodsService.getGoodsVOByPkId(ecmOrderVO.getFkGoodsId());
		PaymentVipService updateVipDate = beanConfig.createQueryService(goods.getGoodsType());
		System.out.println(updateVipDate.toString());
		updateVipDate.operationRelateToPayment(goods.getGoodsActionNumber(),ecmOrderVO.getFkUserId(),goods.getGoodsName());
	}



    /**
     * 	测试短信发送接口
     * @return
     */
    @RequestMapping("/sendNotice")
    @ResponseBody
    public ResponseDTO sendNotice() {
        try {
            // 参数可能是 10% 1gb
            String code = "1gb";
            String[] phoneNumbers = {"+8618895397505"};
            //不带参数的 模板id 960224
            String templateId = "960345";
            SendStatus sendStatus = noticeService.sendSMS(code, phoneNumbers, templateId);
            if(!sendStatus.getCode().equals("Ok")){
                return ResponseDTO.ok("短信发送失败");
            }
            return ResponseDTO.ok("短信发送成功");
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return ResponseDTO.ok("短信发送失败");
        }
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response){
        //excel标题
        String title = "播放记录表";
        //excel表名
        String [] headers = {"序号","用户token","观看作品名称","作品节点xid","进入作品时间"};
        //excel文件名
        String fileName = title + System.currentTimeMillis()+".xls";


        //从数据库中查询出数据组装成content
        //String content[][] = new String[list.size()][5];
        //excel元素
        EcmArtworkBroadcastHistory history = new EcmArtworkBroadcastHistory();
        history.setFkUserId(1619);
       /* List<EcmArtworkBroadcastHistory> histories = historyDao.selectByRecord(history);
        String content[][] = new String[histories.size()][5];
        int seriesNum = 0;
        for (int i = 0; i < histories.size(); i++) {
            content[i] = new String[headers.length];
            content[i][0] = String.valueOf(seriesNum++);
            content[i][1] = histories.get(i).getFkUserId().toString();
            content[i][2] = histories.get(i).getFkArtworkId().toString();
            content[i][3] = histories.get(i).getFkArtworkDetailId().toString();
            content[i][4] = histories.get(i).getStartTime().toString();
        }*/
        //创建HSSFWorkbook
//        HSSFWorkbook wb = ExpotExcelUtil.getHSSFWorkbook(title, headers, content);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
//            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 	测试删除云点播
     * @return
     */
    @RequestMapping("/testDelete")
    @ResponseBody
    public ResponseDTO testDelete() {
       return deleteService.deleteUselessMediaInformation();
    }

    /**
     * 	测试预加热
     * @return
     */
    @RequestMapping("/pushUrlCache")
    @ResponseBody
    public void pushUrlCache(@RequestBody EcmArtworkVo ecmArtworkVo) {
        ecmArtworkVo.setFkUserid(getUserIdByHandToken());
        ecmDownLinkFlowService.pushUrlCache(ecmArtworkVo.getPkArtworkId(),ecmArtworkVo.getFkUserid());
    }

}
