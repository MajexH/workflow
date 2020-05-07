package xyz.majexh.workflow.workflow.entity.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.majexh.message.client.enums.CommandEnum;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用于传输数据的消息定义类
 * 这个消息类型是messageEntity的body
 * 定义如下
 * {
 *     "code": 200
 *     // failed message 作为错误消息
 *     "msg": "",
 *     // 返回给控制端的详细消息
 *     "data": {
 *          "taskId": "",
 *          "status": "PICK || FAIL || SUCC",
 *          "params": {}
 *     }
 * }
 */
@Data
@Slf4j
public class MessageBody implements Serializable {

    private int code;
    private String msg;
    private JSON data;
}
