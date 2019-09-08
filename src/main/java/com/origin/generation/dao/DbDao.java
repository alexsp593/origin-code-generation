package com.origin.generation.dao;

import com.origin.generation.entity.ColumnEntity;
import com.origin.generation.entity.ReferencedEntity;
import com.origin.generation.utils.DbUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 *
 * @author alex
 * @date 2019-09-08
 */
public class DbDao {

    /**
     * 获取指定表的列结构
     *
     * @param dbName    数据库名
     * @param tableName 数据表名
     * @return 获取列结构
     * @throws SQLException 异常
     */
    public List<ColumnEntity> getColumns(String dbName, String tableName) throws SQLException {
        String sql = "select distinct COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE, COLUMN_COMMENT, COLUMN_DEFAULT, COLUMN_KEY from information_schema.columns WHERE table_schema = '" + dbName + "' AND table_name =  '" + tableName + "' ";
        List<Map<String, Object>> maps = DbUtil.executeQueryRs(sql);

        List<ColumnEntity> columnList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(map.get("COLUMN_NAME").toString());
            columnEntity.setDataType(map.get("DATA_TYPE").toString());
            Object macLength = map.get("CHARACTER_MAXIMUM_LENGTH");
            columnEntity.setMaxLength(macLength == null ? 0 : Integer.parseInt(macLength.toString()));
            columnEntity.setIsNullable("NO".equals(map.get("IS_NULLABLE").toString()));
            columnEntity.setColumnComment(map.get("COLUMN_COMMENT").toString());
            Object columnDefault = map.get("COLUMN_DEFAULT");
            columnEntity.setColumnDefault(columnDefault == null ? null : columnDefault.toString());
            Object columnKey = map.get("COLUMN_KEY");
            columnEntity.setColumnKey(columnKey == null ? "" : columnKey.toString());
            columnList.add(columnEntity);
        }
        return columnList;
    }

    /**
     * 查询表字段关联/外键
     *
     * @param dbName    数据库名
     * @param tableName 数据表名
     * @return 返回表字段关联/外键
     * @throws SQLException 异常
     */
    public List<ReferencedEntity> getReferencedColumnDatas(String dbName, String tableName) throws SQLException {
        String sql = "select table_name,column_name,referenced_table_name,referenced_column_name from information_schema.key_column_usage WHERE table_schema = '" + dbName + "' AND table_name =  '" + tableName + "' AND position_in_unique_constraint = 1";

        List<ReferencedEntity> referencedData = new ArrayList<>();
        List<Map<String, Object>> maps = DbUtil.executeQueryRs(sql);
        for (Map<String, Object> map : maps) {
            ReferencedEntity rd = new ReferencedEntity();
            rd.setTableName(map.get("table_name").toString());
            rd.setColumnName(map.get("column_name").toString());
            rd.setReferencedTableName(map.get("referenced_table_name").toString());
            rd.setReferencedColumnName(map.get("referenced_column_name").toString());
            referencedData.add(rd);
        }
        return referencedData;
    }
}
