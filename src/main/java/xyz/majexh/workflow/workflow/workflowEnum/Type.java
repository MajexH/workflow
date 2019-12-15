package xyz.majexh.workflow.workflow.workflowEnum;

/**
 * 指示节点类型
 */
public enum Type {
    SYSTEM("system"),
    USER("user");

    private String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSameType(Type type) {
        if (this.name.equals(type.getName())) return true;
        return false;
    }
}
