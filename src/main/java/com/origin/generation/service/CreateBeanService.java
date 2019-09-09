package com.origin.generation.service;

import com.origin.generation.entity.ColumnEntity;
import com.origin.generation.entity.ReferencedEntity;
import com.origin.generation.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建Bean
 *
 * @author alex
 * @date 2019-09-08
 */
public class CreateBeanService {

    /**
     * 表别名
     */
    private String[] tableAliases = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
    /**
     * 是否分表
     */
    private boolean tableShard;
    /**
     * 是否自增长ID
     */
    private int primaryKeyMode;

    public CreateBeanService(Boolean tableShard, int primaryKeyMode) {
        this.tableShard = tableShard;
        this.primaryKeyMode = primaryKeyMode;
    }

    /**
     * 生成实体Bean 的属性和set,get方法
     *
     * @param columnList  字段名
     * @param refDataList 外键信息
     * @return 生成Bean
     */
    public String getBeanFields(List<ColumnEntity> columnList, List<ReferencedEntity> refDataList) {
        StringBuilder str = new StringBuilder();
        StringBuilder getSet = new StringBuilder();
        for (ColumnEntity d : columnList) {
            String name = d.getPropertyName();
            String type = CommonUtils.getJavaType(d.getDataType());
            String comment = d.getColumnComment();
            String maxChar = name.substring(0, 1).toUpperCase();

            List<String> validatorItems = new ArrayList<>();
            if (d.getMaxLength() > 0) {
                validatorItems.add("len = " + d.getMaxLength());
            }
            if (d.getIsNullable() && !"PRI".equals(d.getColumnKey())) {
                validatorItems.add("notNull = true");
            }
            if (validatorItems.size() > 0) {
                str.append("\r\t").append("@Validator(").append("name=\"").append(comment).append("\"");
                for (String validatorItem : validatorItems) {
                    str.append(",").append(validatorItem);
                }
                str.append(")");
            }

            str.append("\r\t").append("@Column(name=\"").append(d.getColumnName()).append("\")");
            str.append("\r\t").append("private ").append(type).append(" ").append(name).append(";//").append(comment);
            String method = maxChar + name.substring(1);

            getSet.append("\r\t").append("public ").append(type).append(" ").append("get").append(method).append("() {\r\t");
            getSet.append("    return this.").append(name).append(";\r\t}");
            getSet.append("\r\t").append("public void ").append("set").append(method).append("(").append(type).append(" ").append(name).append(") {\r\t");
            if (StringUtils.isNotBlank(d.getColumnDefault()) && NumberUtils.isNumber(d.getColumnDefault())) {
                getSet.append("    this.").append(name).append(" = (").append(name).append("== null ? 0 :").append(name).append(")");
            } else {
                getSet.append("    this.").append(name).append("=").append(name);
            }
            getSet.append(";\r\t}");
        }
        if (refDataList.size() > 0) {
            str.append("\r\t");
            int index = 1;
            for (ReferencedEntity rd : refDataList) {
                String type = rd.getPropertyDName() + "Entity";
                String name = rd.getPropertyName();
                String comment = "";
                String maxChar = name.substring(0, 1).toUpperCase();
                String method = maxChar + name.substring(1);
                str.append("\r\t").append("@ManyToOne(name=\"").append(tableAliases[index]).append("\")");
                str.append("\r\t").append("private ").append(type).append(" ").append(name).append(";//").append(comment);
                getSet.append("\r\t").append("public ").append(type).append(" ").append("get").append(method).append("() {\r\t");
                getSet.append("    return this.").append(name).append(";\r\t}");
                getSet.append("\r\t").append("public void ").append("set").append(method).append("(").append(type).append(" ").append(name).append(") {\r\t");
                getSet.append("    this.").append(name).append("=").append(name).append(";\r\t}");
                index++;
            }
        }

        return str.toString() + "\r\t" + getSet.toString();
    }

    /**
     * 生成实体Model的属性和set,get方法
     *
     * @param columnList  字段名
     * @param refDataList 外键信息
     * @return 生成实体
     */
    public String getModelFields(List<ColumnEntity> columnList, List<ReferencedEntity> refDataList) {
        StringBuilder str = new StringBuilder();
        StringBuilder getSet = new StringBuilder();
        for (ColumnEntity d : columnList) {
            String name = d.getPropertyName();
            String comment = d.getColumnComment();
            String type = CommonUtils.getJavaType(d.getDataType());
            String maxChar = name.substring(0, 1).toUpperCase();
            str.append("\r\t/** ").append("\r\t* ").append(comment).append("\r\t*/");
            str.append("\r\t").append("private ").append(type).append(" ").append(name).append(";");
            String method = maxChar + name.substring(1);

            getSet.append("\r\t").append("public ").append(type).append(" ").append("get").append(method).append("() {\r\t");
            getSet.append("    return this.").append(name).append(";\r\t}");
            getSet.append("\r\t").append("public void ").append("set").append(method).append("(").append(type).append(" ").append(name).append(") {\r\t");
            getSet.append("    this.").append(name).append("=").append(name).append(";\r\t}");
        }
        if (refDataList.size() > 0) {
            str.append("\r\t");
            for (ReferencedEntity rd : refDataList) {
                String type = rd.getPropertyDName() + "Model";
                String name = rd.getPropertyName();
                String comment = "";
                String maxChar = name.substring(0, 1).toUpperCase();
                String method = maxChar + name.substring(1);
                str.append("\r\t").append("private ").append(type).append(" ").append(name).append(";//   ").append(comment);
                getSet.append("\r\t").append("public ").append(type).append(" ").append("get").append(method).append("() {\r\t");
                getSet.append("    return this.").append(name).append(";\r\t}");
                getSet.append("\r\t").append("public void ").append("set").append(method).append("(").append(type).append(" ").append(name).append(") {\r\t");
                getSet.append("    this.").append(name).append("=").append(name).append(";\r\t}");
            }
        }
        return str.append("\r\t").append(getSet).toString();
    }

    public Map<String, Object> getAutoCreateSql(String tableName, List<ColumnEntity> columnLists, List<ReferencedEntity> refDataList) {
        if (tableShard) {
            tableName = "#tableName";
        }
        Map<String, Object> sqlMap = new HashMap<>(32);
        String columns = this.getColumnSplit(columnLists);
        String properties = this.getPropertySplit(columnLists);
        //表所有字段
        String[] columnList = getColumnList(columns);
        //表字段对应实体属性名
        String[] propertyList = getColumnList(properties);
        //表所有字段 按","隔开
        String columnFields = getColumnFields(columns);
        //查询用的字段列表
        String queryColumnFields = getColumnFields(getQueryColumnSplit(columnLists));
        String insert = getInsertSql(tableName, columnLists);
        String addBatch = getAddBatchSql(tableName, columnList, propertyList);
        String update = getUpdateSql(tableName, columnList, propertyList);
        String updateSelective = getUpdateSelectiveSql(tableName, columnLists);
        String updateQuery = getUpdateQuerySql(tableName, columnLists);
        String selectById = getSelectByIdSql(tableName, columnList, propertyList, refDataList);
        String delete = getDeleteSql(tableName, columnList, propertyList);
        String deleteBatch = getDelBatchSql(tableName, columnList);

        sqlMap.put("columnList", columnList);
        //主KEY
        sqlMap.put("priKey", CommonUtils.fromColumnName(columnList[0]));
        //实体属性名
        sqlMap.put("properKey", propertyList[0]);
        sqlMap.put("columnFields", columnFields);
        sqlMap.put("queryColumnFields", queryColumnFields);

        StringBuilder exFields = new StringBuilder(tableAliases[0]);
        exFields.append(".*");
        for (int i = 0; i < refDataList.size(); i++) {
            exFields.append(",").append(tableAliases[i + 1]).append(".*");
        }
        sqlMap.put("extendColumnFields", exFields.toString());
        sqlMap.put("insert", insert.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
        sqlMap.put("addBatch", addBatch);
        sqlMap.put("update", update.replace("#{createTime}", "now()").replace("#{updateTime}", "now()"));
        sqlMap.put("delete", delete);
        sqlMap.put("delBatchSql", deleteBatch);
        sqlMap.put("updateSelective", updateSelective);
        sqlMap.put("updateQuery", updateQuery);
        sqlMap.put("selectById", selectById);
        sqlMap.put("findListSql", getSelectByList(tableName, refDataList));
        sqlMap.put("findByAll", getSelectByAllSql(tableName, refDataList));
        sqlMap.put("findByPageSql", getSelectByPageSql(tableName, refDataList));
        sqlMap.put("queryByCount", getQueryByCountSql(tableName, refDataList));

        return sqlMap;
    }

    /**
     * 获取所有列表名字
     *
     * @param columnList 字段信息列表
     * @return 返回字段拼接字符串
     */
    private String getColumnSplit(List<ColumnEntity> columnList) {
        StringBuilder commonColumns = new StringBuilder();
        for (ColumnEntity data : columnList) {
            commonColumns.append(data.getColumnName()).append("|");
        }
        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

    /**
     * 获取所有属性名字
     *
     * @param columnList 字段信息列表
     * @return 返回属性拼接字符串
     */
    private String getPropertySplit(List<ColumnEntity> columnList) {
        StringBuilder commonColumns = new StringBuilder();
        for (ColumnEntity data : columnList) {
            commonColumns.append(data.getPropertyName()).append("|");
        }
        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

    /**
     * 字符串转字符串数组
     *
     * @param columns 字符串
     * @return 字符串数组
     */
    private String[] getColumnList(String columns) {
        return columns.split("[|]");
    }

    /**
     * 获取所有的字段，并按","分割
     *
     * @param columns 包含|的字符串
     * @return 返回，分割的字符串
     */
    private String getColumnFields(String columns) {
        String fields = columns;
        if (fields != null && !"".equals(fields)) {
            fields = fields.replaceAll("[|]", ",");
        }
        return fields;
    }

    /**
     * 字段列表转按|分割的字符串
     *
     * @param columnList 字段列表
     * @return 按|分割的字符串
     */
    private String getQueryColumnSplit(List<ColumnEntity> columnList) {
        StringBuilder commonColumns = new StringBuilder();
        for (ColumnEntity data : columnList) {
            commonColumns.append(tableAliases[0]).append(".").append(data.getColumnName()).append("|");
        }
        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

    /**
     * mybatis.xml 中 addBatch 批量增加
     *
     * @param tableName   表名
     * @param columnsList 字段列表
     * @return 返回拼接成的xml字符串
     */
    private String getAddBatchSql(String tableName, String[] columnsList, String[] propertList) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(tableName).append("(\n\t\t\t");
        for (int i = 0; i < columnsList.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(columnsList[i]);
        }
        sb.append("\n\t\t) values\n\t\t");
        sb.append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n\t\t\t(");
        for (int i = 0; i < columnsList.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("#{item.").append(propertList[i]).append("}");
        }
        sb.append(")\n\t\t</foreach> ");
        return sb.toString();
    }

    /**
     * mybatis.xml 中 修改记录
     *
     * @param tableName   表名
     * @param columnsList 字段列表
     * @return 返回拼接成的xml字符串
     */
    private String getUpdateSql(String tableName, String[] columnsList, String[] propertList) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < columnsList.length; i++) {
            String column = columnsList[i];
            if ("CREATETIME".equals(column.toUpperCase())) {
                continue;
            }
            if ("UPDATETIME".equals(column.toUpperCase())) {
                sb.append(column).append("=now()");
            } else {
                if (tableShard) {
                    sb.append(column).append("=#{entity.").append(propertList[i]).append("}");
                } else {
                    sb.append(column).append("=#{").append(propertList[i]).append("}");
                }
            }

            //最后一个字段不需要","
            if ((i + 1) < columnsList.length) {
                sb.append(",");
            }
        }
        return "update " + tableName + " \n\t\tset " + sb.toString() + " \n\t\twhere " + columnsList[0] + (tableShard ? "=#{entity." : "=#{") + propertList[0] + "}";
    }

    /**
     * mybatis.xml 中 包含外键的查询记录
     *
     * @param tableName 表名
     * @param referData 外键信息
     * @return 返回拼接成的xml字符串
     */
    private String getSelectByList(String tableName, List<ReferencedEntity> referData) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(tableName).append(" ").append(tableAliases[0]);
        if (referData.size() > 0) {
            for (int i = 0, size = referData.size(); i < size; i++) {
                ReferencedEntity refer = referData.get(i);
                sb.append("\n\t\t\t");
                sb.append("left join ");
                sb.append(refer.getReferencedTableName());
                sb.append(" ");
                sb.append(tableAliases[i + 1]);
                sb.append(" on(");
                sb.append(tableAliases[0]).append(".").append(refer.getColumnName()).append(" = ").append(tableAliases[i + 1]).append(".").append(refer.getReferencedColumnName()).append(")");
            }
        }
        return sb.toString();
    }

    /**
     * mybatis.xml 中 查询包含外键的所有的SQL语句
     *
     * @param tableName 表名
     * @param referData 外键信息
     * @return 返回拼接成的xml字符串
     */
    private String getSelectByAllSql(String tableName, List<ReferencedEntity> referData) {
        StringBuilder sb = new StringBuilder();
        sb.append("select \n\t\t\t <include refid=\"Base_Column_List\" /> \n\t\t").append("from ").append(tableName).append(" ").append(tableAliases[0]);
        if (referData.size() > 0) {
            for (int i = 0, size = referData.size(); i < size; i++) {
                ReferencedEntity refer = referData.get(i);
                sb.append("\n\t\t\t");
                sb.append("left join ");
                sb.append(refer.getReferencedTableName());
                sb.append(" ");
                sb.append(tableAliases[i + 1]);
                sb.append(" on(");
                sb.append(tableAliases[0]).append(".").append(refer.getColumnName()).append(" = ").append(tableAliases[i + 1]).append(".").append(refer.getReferencedColumnName()).append(")");
            }
        }
        return sb.toString();
    }

    /**
     * mybatis.xml 中 查询包含外键的分页查询的SQL语句
     *
     * @param tableName 表名
     * @param referData 外键信息
     * @return 返回拼接成的xml字符串
     */
    private String getSelectByPageSql(String tableName, List<ReferencedEntity> referData) {
        StringBuilder sb = new StringBuilder();
        sb.append("select \n\t\t\t <include refid=\"Extend_Column_List\" /> \n\t\t").append("from ").append(tableName).append(" ").append(tableAliases[0]);
        if (referData.size() > 0) {
            for (int i = 0, size = referData.size(); i < size; i++) {
                ReferencedEntity refer = referData.get(i);
                sb.append("\n\t\t\t");
                sb.append("left join ");
                sb.append(refer.getReferencedTableName());
                sb.append(" ");
                sb.append(tableAliases[i + 1]);
                sb.append(" on(");
                sb.append(tableAliases[0]).append(".").append(refer.getColumnName()).append(" = ").append(tableAliases[i + 1]).append(".").append(refer.getReferencedColumnName()).append(")");
            }
        }
        return sb.toString();
    }

    /**
     * 查询符合记录的第一条（目前暂不用）
     *
     * @param tableName 表名
     * @param referData 外键信息
     * @return 返回拼接成的xml字符串
     */
    private String getSelectOneSql(String tableName, List<ReferencedEntity> referData) {
        return this.getSelectByAllSql(tableName, referData) + "\n\t\tLIMIT 0,1 ";
    }

    /**
     * mybatis.xml 中 查询符合条件的记录数
     *
     * @param tableName 表名
     * @param referData 外键信息
     * @return 返回拼接成的xml字符串
     */
    private String getQueryByCountSql(String tableName, List<ReferencedEntity> referData) {
        StringBuilder sb = new StringBuilder(this.getSelectByAllSql(tableName, referData));
        int index = sb.toString().indexOf("from");
        return "select count(1) " + sb.substring(index);
    }

    /**
     * mybatis.xml 中 插入SQL
     *
     * @param tableName  表名
     * @param columnList 字段列表
     * @return 返回拼接成的xml字符串
     */
    private String getInsertSql(String tableName, List<ColumnEntity> columnList) {

        StringBuilder sb = new StringBuilder();
        sb.append("\t\t<trim  suffixOverrides=\",\" >\n");
        for (int i = (primaryKeyMode == 0 ? 1 : 0); i < columnList.size(); i++) {
            ColumnEntity data = columnList.get(i);
            String columnName = data.getColumnName();
            String propertyName = data.getPropertyName();
            sb.append("\t\t\t<if test=\"");
            if (tableShard) {
                sb.append("entity.");
            }
            sb.append(propertyName).append(" != null");
            sb.append("\">");
            sb.append(columnName);
            sb.append(",");
            sb.append("</if>\n");
        }
        sb.append("\t\t</trim>");
        sb.append("\n\t\t) values(\n");

        sb.append("\t\t<trim  suffixOverrides=\",\" >\n");
        for (int i = (primaryKeyMode == 0 ? 1 : 0); i < columnList.size(); i++) {
            ColumnEntity data = columnList.get(i);
            String propertyName = data.getPropertyName();
            sb.append("\t\t\t<if test=\"");
            if (tableShard) {
                sb.append("entity.");
            }
            sb.append(propertyName).append(" != null");
            sb.append("\">");
            sb.append("#{");
            if (tableShard) {
                sb.append("entity.");
            }
            sb.append(propertyName);
            sb.append("},");
            sb.append("</if>\n");
        }
        sb.append("\t\t</trim>");
        sb.append("\n\t\t)");

        return "insert into " + tableName + "( \n" + sb.toString();
    }

    /**
     * mybatis.xml 中 部分更新SQL
     *
     * @param tableName  表名
     * @param columnList 字段列表
     * @return 返回拼接成的xml字符串
     */
    private String getUpdateSelectiveSql(String tableName, List<ColumnEntity> columnList) {
        StringBuilder sb = new StringBuilder();
        //获取第一条记录，主键
        ColumnEntity cd = columnList.get(0);
        sb.append("\t\t<trim  suffixOverrides=\",\" >\n");
        for (int i = 1; i < columnList.size(); i++) {
            ColumnEntity data = columnList.get(i);
            String columnName = data.getColumnName();
            String propertyName = data.getPropertyName();
            sb.append("\t\t\t<if test=\"");
            if (tableShard) {
                sb.append("entity.");
            }
            sb.append(propertyName).append(" != null");
            sb.append(" \"> ");
            sb.append(columnName).append("=#{");
            if (tableShard) {
                sb.append("entity.");
            }
            sb.append(propertyName);
            sb.append("},");
            sb.append("</if>\n");
        }
        sb.append("\t\t</trim>");
        return "update " + tableName + " set \n" + sb.toString() + "\n\t\twhere " + cd.getColumnName() + "=#{" + (tableShard ? "entity." : "") + cd.getPropertyName() + "}";
    }

    /**
     * mybatis.xml 中 部分更新SQL
     *
     * @param tableName  表名
     * @param columnList 字段列表
     * @return 返回拼接成的xml字符串
     */
    private String getUpdateQuerySql(String tableName, List<ColumnEntity> columnList) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t<trim  suffixOverrides=\",\" >\n");
        for (int i = 1; i < columnList.size(); i++) {
            ColumnEntity data = columnList.get(i);
            String columnName = data.getColumnName();
            String propertyName = data.getPropertyName();
            sb.append("\t\t\t<if test=\"");
            sb.append("entity.");
            sb.append(propertyName).append(" != null");
            sb.append(" \"> ");
            sb.append(columnName).append("=#{");
            sb.append("entity.");
            sb.append(propertyName);
            sb.append("},");
            sb.append("</if>\n");
        }
        sb.append("\t\t</trim>");
        return "update " + tableName + " set \n" + sb.toString();
    }

    /**
     * mybatis.xml 中 根据id查询
     *
     * @param tableName    表名
     * @param columnsList  字段列表
     * @param propertyList 属性列表
     * @param referData    外键信息
     * @return 返回拼接成的xml字符串
     */
    private String getSelectByIdSql(String tableName, String[] columnsList, String[] propertyList, List<ReferencedEntity> referData) {
        StringBuilder sb = new StringBuilder(this.getSelectByAllSql(tableName, referData));
        sb.append("\n\t\tWHERE ");
        sb.append(tableAliases[0]).append(".").append(columnsList[0]);
        if (tableShard) {
            sb.append(" = #{id");
        } else {
            sb.append(" = #{").append(propertyList[0]);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * mybatis.xml 中 delete
     *
     * @param tableName    表名
     * @param columnsList  字段列表
     * @param propertyList 属性列表
     * @return 返回拼接成的xml字符串
     */
    private String getDeleteSql(String tableName, String[] columnsList, String[] propertyList) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ").append(tableName).append("\n\t\twhere ");
        sb.append(columnsList[0]);
        if (tableShard) {
            sb.append(" = #{id");
        } else {
            sb.append(" = #{").append(propertyList[0]);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * mybatis.xml 中 addBatch 批量删除
     *
     * @param tableName 表名
     * @param columnsList 字段列表
     * @return 返回拼接成的xml字符串
     */
    private String getDelBatchSql(String tableName, String[] columnsList) {
        return "delete from " + tableName + "\n\t\t" +
                "<where>\n\t\t\t" + columnsList[0] + " in\n\t\t\t" +
                "<foreach item=\"item\" index=\"index\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\n\t\t\t\t" +
                "#{item}" +
                "\n\t\t\t</foreach>\n\t\t" +
                "</where>";
    }
}
