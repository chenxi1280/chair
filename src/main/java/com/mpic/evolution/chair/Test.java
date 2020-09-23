package com.mpic.evolution.chair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mpic.evolution.chair.pojo.tencent.video.TencentVideoResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class Test {

    public static void main(String[] args) {
        String s = "{;\n" +
                "  \"EventType\": \"ProcedureStateChanged\",\n" +
                "  \"ProcedureStateChangeEvent\": {\n" +
                "    \"TaskId\": \"1259692143-procedurev2-babcddc3f41a6ffd7b0ed37abdfb3394t0\",\n" +
                "    \"Status\": \"FINISH\",\n" +
                "    \"ErrCode\": 0,\n" +
                "    \"Message\": \"SUCCESS\",\n" +
                "    \"FileId\": \"5285890807869927353\",\n" +
                "    \"FileName\": \"0827\",\n" +
                "    \"FileUrl\": \"http://1259692143.vod2.myqcloud.com/6fda56ccvodcq1259692143/54d935dd5285890807869927353/YbQKS0R4kSIA.mp4\",\n" +
                "    \"MetaData\": {\n" +
                "      \"AudioDuration\": 2.1362359523773193,\n" +
                "      \"AudioStreamSet\": [{\n" +
                "        \"Bitrate\": 43440,\n" +
                "        \"Codec\": \"aac\",\n" +
                "        \"SamplingRate\": 44100\n" +
                "      }],\n" +
                "      \"Bitrate\": 1072792,\n" +
                "      \"Container\": \"mov,mp4,m4a,3gp,3g2,mj2\",\n" +
                "      \"Duration\": 2.200000047683716,\n" +
                "      \"Height\": 960,\n" +
                "      \"Rotate\": 0,\n" +
                "      \"Size\": 297247,\n" +
                "      \"VideoDuration\": 2.200000047683716,\n" +
                "      \"VideoStreamSet\": [{\n" +
                "        \"Bitrate\": 1029352,\n" +
                "        \"Codec\": \"h264\",\n" +
                "        \"Fps\": 29,\n" +
                "        \"Height\": 960,\n" +
                "        \"Width\": 544\n" +
                "      }],\n" +
                "      \"Width\": 544\n" +
                "    },\n" +
                "    \"AiAnalysisResultSet\": [],\n" +
                "    \"AiRecognitionResultSet\": [],\n" +
                "    \"AiContentReviewResultSet\": [{\n" +
                "      \"Type\": \"Porn\",\n" +
                "      \"Progress\": 100,\n" +
                "      \"PornTask\": {\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"\",\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"Progress\": 100,\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 20\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Confidence\": 90,\n" +
                "          \"Suggestion\": \"review\",\n" +
                "          \"Label\": \"sexy\",\n" +
                "          \"SegmentSet\": [{\n" +
                "            \"Confidence\": 90,\n" +
                "            \"EndTimeOffset\": 0.033,\n" +
                "            \"Label\": \"sexy\",\n" +
                "            \"PicUrlExpireTime\": \"2020-09-30T07:41:03.646Z\",\n" +
                "            \"PicUrlExpireTimeStamp\": 1601451663,\n" +
                "            \"StartTimeOffset\": 0.033,\n" +
                "            \"Suggestion\": \"review\",\n" +
                "            \"Url\": \"http://251000800.vod2.myqcloud.com/1a168d62vodcq251000800/54d935dd5285890807869927353/1334470068_33.jpg\"\n" +
                "          }]\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"Type\": \"Terrorism\",\n" +
                "      \"Progress\": 100,\n" +
                "      \"TerrorismTask\": {\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"\",\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"Progress\": 100,\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 20\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Confidence\": 0,\n" +
                "          \"Suggestion\": \"pass\",\n" +
                "          \"Label\": \"\",\n" +
                "          \"SegmentSet\": []\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"Type\": \"Political\",\n" +
                "      \"Progress\": 100,\n" +
                "      \"PoliticalTask\": {\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"\",\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"Progress\": 100,\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 20\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Confidence\": 0,\n" +
                "          \"Suggestion\": \"pass\",\n" +
                "          \"Label\": \"\",\n" +
                "          \"SegmentSet\": []\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"Type\": \"Political.Ocr\",\n" +
                "      \"Progress\": 100,\n" +
                "      \"PoliticalOcrTask\": {\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"\",\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"Progress\": 100,\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 20\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Confidence\": 0,\n" +
                "          \"Suggestion\": \"pass\",\n" +
                "          \"SegmentSet\": []\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"Type\": \"Porn.Ocr\",\n" +
                "      \"Progress\": 100,\n" +
                "      \"PornOcrTask\": {\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"\",\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"Progress\": 100,\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 20\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Confidence\": 0,\n" +
                "          \"Suggestion\": \"pass\",\n" +
                "          \"SegmentSet\": []\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"Type\": \"Porn.Asr\",\n" +
                "      \"Progress\": 100,\n" +
                "      \"PornAsrTask\": {\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"\",\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"Progress\": 100,\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 20\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Confidence\": 0,\n" +
                "          \"Suggestion\": \"pass\",\n" +
                "          \"SegmentSet\": []\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"Type\": \"Political.Asr\",\n" +
                "      \"Progress\": 100,\n" +
                "      \"PoliticalAsrTask\": {\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"\",\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"Progress\": 100,\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 20\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Confidence\": 0,\n" +
                "          \"Suggestion\": \"pass\",\n" +
                "          \"SegmentSet\": []\n" +
                "        }\n" +
                "      }\n" +
                "    }],\n" +
                "    \"MediaProcessResultSet\": [{\n" +
                "      \"Type\": \"Transcode\",\n" +
                "      \"TranscodeTask\": {\n" +
                "        \"Status\": \"SUCCESS\",\n" +
                "        \"ErrCode\": 0,\n" +
                "        \"Message\": \"SUCCESS\",\n" +
                "        \"Input\": {\n" +
                "          \"Definition\": 100020,\n" +
                "          \"WatermarkSet\": []\n" +
                "        },\n" +
                "        \"Output\": {\n" +
                "          \"Url\": \"http://1259692143.vod2.myqcloud.com/46c44474vodtranscq1259692143/54d935dd5285890807869927353/v.f100020.mp4\",\n" +
                "          \"Size\": 228950,\n" +
                "          \"Container\": \"mov,mp4,m4a,3gp,3g2,mj2\",\n" +
                "          \"Height\": 952,\n" +
                "          \"Width\": 540,\n" +
                "          \"Bitrate\": 847962,\n" +
                "          \"Md5\": \"a1c891951a8ace05c696f6c7dbb1374d\",\n" +
                "          \"Duration\": 2.16,\n" +
                "          \"VideoStreamSet\": [{\n" +
                "            \"Bitrate\": 770785,\n" +
                "            \"Codec\": \"h264\",\n" +
                "            \"Fps\": 25,\n" +
                "            \"Height\": 952,\n" +
                "            \"Width\": 540\n" +
                "          }],\n" +
                "          \"AudioStreamSet\": [{\n" +
                "            \"Bitrate\": 68508,\n" +
                "            \"Codec\": \"aac\",\n" +
                "            \"SamplingRate\": 44100\n" +
                "          }],\n" +
                "          \"Definition\": 100020\n" +
                "        }\n" +
                "      }\n" +
                "    }],\n" +
                "    \"SessionContext\": \"\",\n" +
                "    \"SessionId\": \"\",\n" +
                "    \"TasksPriority\": 0,\n" +
                "    \"TasksNotifyMode\": \"\"\n" +
                "  }\n" +
                "}";

        TencentVideoResult tencentVideoResult = JSON.parseObject(s, TencentVideoResult.class);
        System.out.println(tencentVideoResult.toString());


    }



}
