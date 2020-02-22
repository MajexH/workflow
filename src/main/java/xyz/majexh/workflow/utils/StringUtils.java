package xyz.majexh.workflow.utils;

import java.util.UUID;

public class StringUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getTaskId(String chainId, String nodeId) {
        return String.format("%s:%s", chainId, nodeId);
    }

    public static String extractNodeIdFromTaskId(String taskId) {
        return taskId.split(":")[1];
    }

    public static String extractChainIdFromTaskId(String taskId) {
        return taskId.split(":")[0];
    }

    /**
     * check the str is null or ''
     * @param str
     * @return
     */
    public static boolean checkNone(String str) {
        return str == null || str.equals("");
    }

}
