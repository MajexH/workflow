package xyz.majexh.workflow.exceptions;

public enum ExceptionEnum {

    DUPLICATE_NODE_ID(400, "重复的节点ID"),
    TOPOLOGY_ARGS_NOT_CAPABLE(400, "topology参数错误"),
    START_TOPOLOGY_ERROR(400, "创建拓扑任务失败"),
    INPUT_PARAMS_ERROR(400, "输入参数错误"),
    TASK_NOT_FOUND(404, "找不到任务"),
    OUTPUT_NOT_SATISFY(400, "任务输出参数错误"),
    INSUFFICIENT_PARAMS(400, ""),
    WRONG_LOGIN(400, "账户名或密码错误"),
    TOKEN_EXPIRE(401, "登录token超时"),
    TOKEN_WRONG(400, "验证token失败"),
    INTERNAL_ERROR(500, "内部错误");

    private int status;
    private String message;

    ExceptionEnum(int status, String Message) {
        this.status = status;
        this.message = Message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
