package xyz.majexh.workflow.workflow.entity.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用于传输数据的消息定义类
 * 定义如下
 * {
 *     "taskId": "",
 *     // "success" || "failed"
 *     "status": "",
 *     // 返回给控制端的详细消息
 *     // success 其会传递给下一个task作为输入
 *     // failed 会读取res中的message字段 作为错误消息
 *     "res": {
 *
 *     }
 * }
 */
@Data
@Slf4j
public class MessageEntity implements Serializable {

    private String taskId;
    private String status;
    private JSON res;

    public static MessageEntity getMessageFromJSON(JSON json) {

        MessageEntity res = null;
        try {
            res = JSON.parseObject(json.toString(), MessageEntity.class);

        } catch (Exception e) {
            log.error(String.format("format message json error: %s", Arrays.toString(e.getStackTrace())));
            // TODO: Exception
            return null;
        }
        if (res.getRes() == null) res.setRes(new JSONObject());
        return res;
    }
}
