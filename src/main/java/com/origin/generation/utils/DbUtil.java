package com.origin.generation.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接池
 *
 * @author alex
 * @date 2019-09-07
 */
public class DbUtil {

    private static DataSource ds = null;

    static {
        PropertiesUtils propertiesUtils = new PropertiesUtils("jdbc.properties");
        try {
            ds = DruidDataSourceFactory.createDataSource(propertiesUtils.getProper());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection openConnection() throws SQLException {
        return ds.getConnection();
    }

    public static List<Map<String, Object>> executeQueryRs(String sql) throws SQLException {
        return executeQueryRs(sql, null);
    }

    public static List<Map<String, Object>> executeQueryRs(String sql, Object[] params) throws SQLException {
        ResultSet resultSet = null;
        Connection connection = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            connection = openConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            // 参数赋值
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            // 执行
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>(32);
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rsmd.getColumnLabel(i), resultSet.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return list;
    }
}
