package com.origin.generation.entity;

import com.origin.generation.utils.CommonUtils;

/**
 * 表字段关联/外键信息
 *
 * @author alex
 * @date 2019-09-08
 */
public class ReferencedEntity {

    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String columnName;
    /**
     * 关联表名
     */
    private String referencedTableName;
    /**
     * 关联字段名
     */
    private String referencedColumnName;
    private String propertyName;
    private String propertyDName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getReferencedTableName() {
        return referencedTableName;
    }

    public void setReferencedTableName(String referencedTableName) {
        this.setPropertyName(CommonUtils.fromColumnName(referencedTableName));
        this.setPropertyDName(CommonUtils.formatterClassName(referencedTableName));
        this.referencedTableName = referencedTableName;
    }

    public String getReferencedColumnName() {
        return referencedColumnName;
    }

    public void setReferencedColumnName(String referencedColumnName) {
        this.referencedColumnName = referencedColumnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyDName() {
        return propertyDName;
    }

    public void setPropertyDName(String propertyDName) {
        this.propertyDName = propertyDName;
    }

}
