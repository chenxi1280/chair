package com.mpic.evolution.chair.controller;

import com.mpic.evolution.chair.pojo.vo.ClickActionVo;
import com.mpic.evolution.chair.service.BroadcastService;
import com.mpic.evolution.chair.service.BroadcastStatisticsService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 *
 * 类名:   BroadcastStatisticsController
 * @author Xuezx
 * @date 2020/6/10 0010 11:07
 * 描述: 播放统计数据
 * 主要与用户播放作品相关
 * 此统计接口未来应该拆出来作为其他项目
 * 并且数据库也应分开，只查入统计的数据，以免读写造成问题
 *
 * todo 应加入id，name,地区，性别四种字段
 */
@Controller
public class BroadcastStatisticsController {

    @Resource
    private
    BroadcastStatisticsService service;

    @Resource
    private
    BroadcastService broadcastService;
    /**
     *
     * 方法名:  userBroadcastInterested
     * @author Xuezx
     * @date 2020/6/10 0010 11:12
     * 描述: 查询所有用户播放过的和没播放过的所有作品统计，csv格式
     *
     * 代表用户是否对某个题材感兴趣
     */
    @RequestMapping(value="/44444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444")
    public void userBroadcastInterested(HttpServletResponse response) throws IOException {

        String fileName = "用户播放" + System.currentTimeMillis() + ".csv";

        //设置文件路径
        File file = service.getUserBroadcastInterested();
        publishFIle(response, fileName, file);

        //删除无用的文件
        FileUtils.deleteQuietly(file);
    }




    /**
     *
     * 方法名:  HttpServletResponse
     * @author Xuezx
     * @date 2020/6/24 0024 11:02
     * @param response  response
     * 描述:完看数——最后一个剧情框的播放数
     * （ 或未点故事线按钮跳出故事线的次数）
     *  ——用户被作品的吸引程度，可能与拍摄表现，
     *  情节设置或内容种类有关
     * 表格纵列：用户id
     * 表格横列：视频id
     * 表格内容：最后一个视频的播放数
     */
    @RequestMapping(value="/getStatisticsEnd")
    public void userBroadcastEnd(HttpServletResponse response) throws IOException {
        String fileName = "用户看完" + System.currentTimeMillis() + ".csv";

        //设置文件路径
        File file = service.getUserBroadcastEnd();
        publishFIle(response, fileName, file);

        //删除无用的文件
        FileUtils.deleteQuietly(file);
    }


    @RequestMapping(value="/getStatisticsClickTimeline")
    public void getUserClickStoryLineTimes(HttpServletResponse response) throws IOException {
        String fileName = "主动点击故事线" + System.currentTimeMillis() + ".csv";

        //设置文件路径
        File file = service.getUserClickStoryLineTimes();
        publishFIle(response, fileName, file);

        //删除无用的文件
        FileUtils.deleteQuietly(file);
    }

    @RequestMapping(value="/getStatisticsClickTimelineVideoPart")
    public void getUserClickStoryLineTimeVideoPart(HttpServletResponse response) throws IOException {
        String fileName = "主动点击故事线" + System.currentTimeMillis() + ".csv";

        //设置文件路径
        File file = service.getUserClickStoryLineTimeVideoPart();
        publishFIle(response, fileName, file);

        //删除无用的文件
        FileUtils.deleteQuietly(file);
    }

    private void publishFIle(HttpServletResponse response, String fileName, File file) throws UnsupportedEncodingException {
        if (file.exists()) {
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            response.setContentType("multipart/form-data;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName="+ new String(fileName.getBytes("GB2312"),"ISO-8859-1"));
            byte[] buffer = new byte[1024];

            try(FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis)) {
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * 方法名:  recordStorylineClick
     * @author Xuezx
     * @date 2020/6/19 0019 13:40
     *
     * 描述: 记录故事线触发事件，有主动点击和被动点击两种
     *                被动点击被记录为作品看完
     *
     */
    @RequestMapping(value="/storylineClick")
    @ResponseBody
    public String recordStorylineClick(@RequestBody ClickActionVo clickActionVo){
        try{
            int intUserId = clickActionVo.getUserId();
            int intPassiveFlag = clickActionVo.getPassiveFlag();
            int intVideoId = clickActionVo.getVideoId();
            int intCurrentPlayTime = clickActionVo.getCurrentPlayTime();
            broadcastService.recordStorylineClick(intUserId, intVideoId, intPassiveFlag, intCurrentPlayTime);
            return "ok";
        }catch (Exception e){
            return "参数不正确";
        }

    }

    /**
     *
     * 方法名:
     * @author Xuezx
     * @date 2020/6/19 0019 16:01
     * @param clickActionVo userId
     * @param clickActionVo, videoId
     * @param clickActionVo intCurrentPlayTime 播放秒数
     * @param clickActionVo actionType 动作类型，各式各样，如exit什么的，随心所欲的写
     * @return java.lang.String json
     * 描述:
     */
    @RequestMapping(value = "/setPlayActionType}")
    @ResponseBody
    public String broadcastPageAction(@RequestBody ClickActionVo clickActionVo) {
        try {
            int intUserId = clickActionVo.getUserId();
            int intVideoId = clickActionVo.getVideoId();
            int intCurrentPlayTime = clickActionVo.getCurrentPlayTime();
            String actionType = clickActionVo.getActionType();
            service.broadcastPageAction(intUserId, intVideoId, actionType, intCurrentPlayTime);
            return "true";
        }catch (Exception e){
            return "参数不正确";
        }
    }
}
