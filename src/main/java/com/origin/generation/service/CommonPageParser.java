package com.origin.generation.service;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

/**
 * 按模板生成页面
 *
 * @author alex
 * @date 2019-09-08
 */
public class CommonPageParser {

    private static Logger log = Logger.getLogger(CommonPageParser.class);

    private static VelocityEngine ve;

    private static final String CONTENT_ENCODING = "UTF-8";
    /**
     * 是否可以替换文件 true =可以替换，false =不可以替换
     */
    private static final boolean IS_REPLACE = false;

    static {
        try {
            //获取文件模板根路径
            String path = CommonPageParser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            Properties properties = new Properties();
            properties.setProperty(Velocity.RESOURCE_LOADER, "file");
            properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
            properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path + "template" + File.separator);
            properties.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "true");
            properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
            properties.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
            properties.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
            properties.setProperty("directive.set.null.allowed", "true");
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.init(properties);

            ve = velocityEngine;
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * 生成页面文件
     *
     * @param velocityContext      内容上下文
     * @param templateName 模板文件路径（相对路径）article\\article_main.html
     * @param fileDirPath  生成页面文件路径（相对路径）
     * @param targetFile   生成的文件名
     */
    public static void writerPage(VelocityContext velocityContext, String templateName, String fileDirPath, String targetFile) {
        try {
            String fileName = fileDirPath + targetFile;
            System.out.println("正在生成：" + fileName);
            File file = new File(fileName);
            if (!file.exists()) {
                file.getParentFile().mkdir();
            } else {
                if (IS_REPLACE) {
                    log.info("替换文件" + file.getAbsolutePath());
                }
            }
            Template template = ve.getTemplate(templateName, CONTENT_ENCODING);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, CONTENT_ENCODING));
            template.merge(velocityContext, writer);
            writer.flush();
            writer.close();
            fos.close();
        } catch (Exception e) {
            log.error(e);
            System.out.println(e.toString());
        }
    }

}
