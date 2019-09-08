package com.origin.generation.entity;

import com.origin.generation.utils.CommonUtils;

/**
 * 表字段类
 *
 * @author alex
 * @date 2019-09-08
 */
public class ColumnEntity {

    /**
     * 字段名
     */
    private String columnName;
    /**
     * 数据库字段转成实体类属性的名字
     */
    private String propertyName;
    /**
     * 字段数据类型
     */
    private String dataType;
    /**
     * 字段最大长度
     */
    private Integer maxLength;
    /**
     * 是否可为空
     */
    private Boolean isNullable;
    /**
     * 字段描述
     */
    private String columnComment;
    /**
     * 默认值
     */
    private String columnDefault;
    /**
     * 主键类型
     */
    private String columnKey;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.propertyName = CommonUtils.fromColumnName(columnName);
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Boolean getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(Boolean isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }
}
