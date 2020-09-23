/**
  * Copyright 2020 bejson.com 
  */
package com.mpic.evolution.chair.pojo.tencent.video;
import lombok.Data;

import java.util.List;

/**
 * Date: 2020-09-23 17:16:41
 *
 * @author cxd
 */
@Data
public class ProcedureStateChangeEvent {

    private String TaskId;
    private String Status;
    private int ErrCode;
    private String Message;
    private String FileId;
    private String FileName;
    private String FileUrl;
    private MetaData MetaData;
    private List<String> AiAnalysisResultSet;
    private List<String> AiRecognitionResultSet;
    private List<AiContentReviewResultSet> AiContentReviewResultSet;
    private List<MediaProcessResultSet> MediaProcessResultSet;
    private String SessionContext;
    private String SessionId;
    private int TasksPriority;
    private String TasksNotifyMode;

}