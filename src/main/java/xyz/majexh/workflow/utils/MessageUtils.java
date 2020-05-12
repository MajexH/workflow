package xyz.majexh.workflow.utils;

import xyz.majexh.message.client.entity.MessageEntity;
import xyz.majexh.message.client.enums.CommandEnum;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;

import java.util.HashMap;

public class MessageUtils {

    public static boolean isSuccess(MessageBody message) {
        return message.getCode() == 200;
    }

    public static boolean isFail(MessageBody message) {
        return message.getCode() != 200;
    }

    public static boolean isPick(MessageEntity message) {
        return message.getCommand().equals(CommandEnum.RESPONSE);
    }
}
