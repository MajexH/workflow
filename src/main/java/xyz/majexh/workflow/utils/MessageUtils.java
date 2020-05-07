package xyz.majexh.workflow.utils;

import xyz.majexh.message.client.enums.CommandEnum;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;

import java.util.HashMap;

public class MessageUtils {

    public static boolean isSuccess(HashMap<String, Object> message) {
        return message.get("status").equals("SUCC");
    }

    public static boolean isFail(HashMap<String, Object> message) {
        return message.get("status").equals("FAIL");
    }

    public static boolean isPick(HashMap<String, Object> message) {
        return message.get("status").equals("PICK");
    }
}
