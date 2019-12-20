package xyz.majexh.workflow.utils;

import xyz.majexh.workflow.workflow.entity.message.MessageEntity;

public class MessageUtils {

    public static boolean isSuccess(MessageEntity entity) {
        return entity.getStatus().equals("success");
    }

    public static boolean isFail(MessageEntity entity) {
        return entity.getStatus().equals("fail");
    }

    public static boolean isPick(MessageEntity entity) {
        return entity.getStatus().equals("pick");
    }
}
