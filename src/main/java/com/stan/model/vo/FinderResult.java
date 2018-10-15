package com.stan.model.vo;

public class FinderResult {

    private Long locId;
    private String typeName;
    private String columnName;
    private String target;

    public FinderResult(Long locId, String typeName, String columnName, String target) {
        this.locId = locId;
        this.typeName = typeName;
        this.columnName = columnName;
        this.target = target;
    }

    public Long getLocId() {
        return locId;
    }

    public void setLocId(Long locId) {
        this.locId = locId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
