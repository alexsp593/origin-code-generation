package com.origin;

import com.origin.generation.dao.DbDao;
import com.origin.generation.entity.ColumnEntity;
import com.origin.generation.entity.ReferencedEntity;
import com.origin.generation.service.CommonPageParser;
import com.origin.generation.service.CreateBeanService;
import com.origin.generation.utils.DateUtils;
import com.origin.generation.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class Main {
    /**
     * 数据库表名
     */
    private static String dbName = "blog";
    /**
     * 项目代码生成根目录
     */
    private static String ROOT_PATH = "*";
    /**
     * 包路径
     */
    private static String PACKAGE = "com.alex.";
    private static String PACKAGE_PATH = "com.alex.";
    private static String PACKAGE_PATH_XML = "com.alex.";
    /**
     * 子包路径，如：com.origin.core.**.sys
     */
    private static String SUB_PACKAGE_PATH = "";
    /**
     * 是否支持分表
     */
    private static Boolean tableShard = false;
    /**
     * 默认分表数量
     */
    private static int tableShardNum = 20;
    /**
     * 作者
     */
    private static String author = "alex";


    public static void main(String[] args) throws SQLException {

        //表名
        String[] tables = {};
        //表的描述
        String[] codes ={};
        //主键方式0自动生成，1采用UUID
        Integer[] keyModes = {};

        //批量生成
		for (int i = 0; i < tables.length; i++) {
			System.out.println("正在生成:" + tables[i] + " " + codes[i]);
			createSSICode(tables[i], codes[i], keyModes[i]);
		}
    }

    /**
     * 生成SSI框架的代码
     *
     * @param tableName      --表名
     * @param tableNote      --中文注释
     * @param primaryKeyMode --主键生成方式 0-自动生成，1-UUID生成
     */
    public static void createSSICode(String tableName, String tableNote, Integer primaryKeyMode) throws SQLException {

        DbDao dbDao = new DbDao();
        List<ReferencedEntity> referencedColumnDatas = dbDao.getReferencedColumnDatas(dbName, tableName);
        List<ColumnEntity> columns = dbDao.getColumns(dbName, tableName);

        String tableName2 = tableName;
        if (tableShard) {
            tableName2 = tableName.substring(0, tableName.lastIndexOf("_"));
        }

        String className = CommonUtils.formatterClassName(tableName2);
        String lowerName = CommonUtils.fromColumnName(tableName2);


        String modelPath = File.separator + "model" + File.separator + SUB_PACKAGE_PATH + File.separator + className + ".java";
        String beanPath = File.separator + "entity" + File.separator + SUB_PACKAGE_PATH + File.separator + className + "Entity.java";
        String beanPathOther = File.separator + "model" + File.separator + SUB_PACKAGE_PATH + File.separator + className + ".java";
        String mapperPath;
        String servicePath;
        String sqlMapperPath;
        mapperPath = File.separator + "mapper" + File.separator + SUB_PACKAGE_PATH + File.separator + className + "Mapper.java";
        servicePath = File.separator + "service" + File.separator + SUB_PACKAGE_PATH + File.separator + className + "Service.java";
        sqlMapperPath = File.separator + "mybatis" + File.separator + SUB_PACKAGE_PATH + File.separator + className + "Mapper.xml";

        String controllerPath = File.separator + "action" + File.separator + SUB_PACKAGE_PATH + File.separator + className + "Controller.java";
//        //jsp页面路径
//        String pageAddPath = lowerName + File.separator + lowerName + "Add.ftl";
//        String pageEditPath = lowerName + File.separator + lowerName + "Edit.ftl";
//        String pageDetailPath = lowerName + File.separator + lowerName + "Detail.ftl";
//        String pageListPath = lowerName + File.separator + lowerName + "View.ftl";
//        //
//        ////js路径
//        String jsAddPath = lowerName + File.separator + lowerName + "Add.js";
//        String jsEditPath = lowerName + File.separator + lowerName + "Edit.js";
//        String jsDetailPath = lowerName + File.separator + lowerName + "Detail.js";
//        String jsListPath = lowerName + File.separator + lowerName + "View.js";

        VelocityContext context = new VelocityContext();
        context.put("author", author);
        context.put("systemDate", DateUtils.getCurDateStr("yyyy-MM-dd HH:mm:ss"));
        context.put("className", className);
        context.put("lowerName", lowerName);
        context.put("tableNote", tableNote);
        context.put("tableName", tableName2);
        context.put("tableShardNum", tableShardNum);
        context.put("packPath", PACKAGE_PATH);
        context.put("packagePath", PACKAGE);
        context.put("subPackPath", SUB_PACKAGE_PATH);
        context.put("primaryKeyMode", primaryKeyMode);
        String entityFullName = PACKAGE_PATH + beanPathOther;
        String modelFullName = PACKAGE_PATH + modelPath;
        context.put("entityName", entityFullName.substring(0, entityFullName.lastIndexOf(".")).replace(File.separator, ".").replace("..", "."));
        context.put("modelFullName", modelFullName.substring(0, modelFullName.lastIndexOf(".")).replace(File.separator, ".").replace("..", "."));
        String mapperFullName = PACKAGE_PATH_XML + mapperPath;
        context.put("mapperFullName", mapperFullName.substring(0, mapperFullName.lastIndexOf(".")).replace(File.separator, ".").replace("..", "."));

        CreateBeanService createBean = new CreateBeanService(tableShard, primaryKeyMode);
        Map<String, Object> sqlMap = null;
        try {
            sqlMap = createBean.getAutoCreateSql(tableName, columns, referencedColumnDatas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //生成bean
        context.put("columnData", columns);
        //关联/外键
        context.put("ReferData", referencedColumnDatas);
        context.put("SQL", sqlMap);
        context.put("tableKey", StringUtils.capitalize(sqlMap.get("properKey").toString()));
        context.put("properKey", sqlMap.get("properKey").toString());

        //生成bean
        context.put("fields", createBean.getBeanFields(columns, referencedColumnDatas));
        //生成bean
        context.put("modelFields", createBean.getModelFields(columns, referencedColumnDatas));


        String filePath = ROOT_PATH + PACKAGE_PATH.replace(".", File.separator);
        //生成实体Bean
        CommonPageParser.writerPage(context, "TempBean.java", filePath, beanPath);
        //生成Model
        CommonPageParser.writerPage(context, "TempModel.java", filePath, modelPath);
        if (tableShard) {
            //生成MybatisMapper接口 相当于Dao
            CommonPageParser.writerPage(context, "TempShardMapper.java", filePath, mapperPath);
            //生成Service
            CommonPageParser.writerPage(context, "TempShardService.java", filePath, servicePath);
            //生成Mybatis xml配置文件
            CommonPageParser.writerPage(context, "TempShardMapper.xml", filePath, sqlMapperPath);
        } else {
            //生成MybatisMapper接口 相当于Dao
            CommonPageParser.writerPage(context, "TempMapper.java", filePath, mapperPath);
            //生成ServiceImpl
            CommonPageParser.writerPage(context, "TempServiceImpl.java", filePath, servicePath);
            //生成Service
            CommonPageParser.writerPage(context, "TempService.java", filePath, servicePath);
            //生成Mybatis xml配置文件
            CommonPageParser.writerPage(context, "TempMapper.xml", filePath, sqlMapperPath);
        }
        //生成Controller 相当于接口
        CommonPageParser.writerPage(context, "TempController.java", filePath, controllerPath);

        //页面路径，放到WEB-INF下面是为了不让手动输入路径访问jsp页面，起到安全作用
//        context.put("alert", "$alert");
//        context.put("api", "$api");
//        context.put("mod", "${mod!}");
//        String webPath = ROOT_PATH + "WebRoot" + File.separator +"view" + File.separator;
//        CommonPageParser.writerPage(context, "TempWebAdd.ftl", webPath, pageAddPath);//生成Controller 相当于接口
//        CommonPageParser.writerPage(context, "TempWebEdit.ftl", webPath, pageEditPath);//生成Controller 相当于接口
//        CommonPageParser.writerPage(context, "TempWebDetail.ftl", webPath, pageDetailPath);//生成Controller 相当于接口
//        CommonPageParser.writerPage(context, "TempWebView.ftl", webPath, pageListPath);//生成Controller 相当于接口
//
//        //生成js文件
//        CommonPageParser.writerPage(context, "TempWebAdd.js", webPath, jsAddPath);//
//        CommonPageParser.writerPage(context, "TempWebEdit.js", webPath, jsEditPath);//
//        CommonPageParser.writerPage(context, "TempWebDetail.js", webPath, jsDetailPath);//
//        CommonPageParser.writerPage(context, "TempWebView.js", webPath, jsListPath);//
    }
}
