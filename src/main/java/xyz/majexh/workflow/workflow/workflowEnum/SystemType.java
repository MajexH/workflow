package xyz.majexh.workflow.workflow.workflowEnum;

public enum SystemType {

    NONE("none"),
    BARRIER("barrier");

    private String sysTypeName;

    SystemType(String sysTypeName) {
        this.sysTypeName = sysTypeName;
    }

    public String getSysTypeName() {
        return sysTypeName;
    }

    public void setSysTypeName(String sysTypeName) {
        this.sysTypeName = sysTypeName;
    }

    public boolean isSameSysType(SystemType sysType) {
        return this.sysTypeName.equals(sysType.getSysTypeName());
    }
}