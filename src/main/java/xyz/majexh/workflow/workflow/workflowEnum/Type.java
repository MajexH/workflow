package xyz.majexh.workflow.workflow.workflowEnum;

/**
 * 指示节点类型
 */
public enum Type {

    SYSTEM_BARRIER("system", SystemType.BARRIER),
    SYSTEM_NONE("system", SystemType.NONE),
    USER("user", SystemType.NONE);

    private String typeName;
    private SystemType sysType;

    Type(String name, SystemType sysType) {
        this.typeName = name;
        this.sysType = sysType;
    }

    public String getTypeName() {
        return typeName;
    }

    public SystemType getSysType() {
        return sysType;
    }

    public boolean isSameType(Type type) {
        return this.typeName.equals(type.getTypeName()) && this.sysType.isSameSysType(type.getSysType());
    }
}
