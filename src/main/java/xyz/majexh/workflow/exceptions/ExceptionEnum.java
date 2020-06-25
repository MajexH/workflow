package xyz.majexh.workflow.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author majexh
 */

public enum ExceptionEnum {

    /**
     * 异常定义
     */
    DUPLICATE_NODE_ID(HttpStatus.BAD_REQUEST, "重复的节点ID"),
    TOPOLOGY_ARGS_NOT_CAPABLE(HttpStatus.BAD_REQUEST, "topology参数错误"),
    START_TOPOLOGY_ERROR(HttpStatus.BAD_REQUEST, "创建拓扑任务失败"),
    INPUT_PARAMS_ERROR(HttpStatus.BAD_REQUEST, "输入参数错误"),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "找不到任务"),
    OUTPUT_NOT_SATISFY(HttpStatus.BAD_REQUEST, "任务输出参数错误"),
    INSUFFICIENT_PARAMS(HttpStatus.BAD_REQUEST, ""),
    WRONG_LOGIN(HttpStatus.BAD_REQUEST, "账户名或密码错误"),
    USER_EXITS(HttpStatus.BAD_REQUEST, "注册失败，用户名已经被占用"),
    TOKEN_EXPIRE(HttpStatus.UNAUTHORIZED, "登录token超时"),
    TOKEN_WRONG(HttpStatus.UNAUTHORIZED, "验证token失败"),
    TOPOLOGY_NOT_FOUND(HttpStatus.NOT_FOUND, "未找到对应拓扑"),
    WRONG_PARAMS(HttpStatus.BAD_REQUEST, "请求参数错误，请检查"),
    WRONG_TOPOLOGY_NAME(HttpStatus.BAD_REQUEST, "创建chain失败，请检查参数"),
    CHAIN_NOT_FOUND(HttpStatus.NOT_FOUND, "未找到对应运行链"),
    WRONG_RESUBMIT_TASK_STATE(HttpStatus.BAD_REQUEST, "无法重启正在运行的任务"),
    CANNOT_FIND_TIME_RANGE(HttpStatus.BAD_REQUEST, "找不到时间范围，请检查数据"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "内部错误");

    private HttpStatus status;
    private String message;

    ExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
