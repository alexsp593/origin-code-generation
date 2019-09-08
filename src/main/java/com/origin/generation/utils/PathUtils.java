package com.origin.generation.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 项目路径管理
 *
 * @author alex
 * @date 2019-09-07
 */
public class PathUtils {

    private static PathUtils instance = null;

    public static PathUtils getInstance() {
        if (instance == null) {
            instance = new PathUtils();
        }
        return instance;
    }

    /**
     * 获取项目根目录
     *
     * @return 返回项目跟目录
     */
    public String getWebRootPath() {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = path.indexOf("WEB-INF");
        if (index >= 0) {
            path = path.substring(0, index);
            if (path.startsWith("zip")) {
                // 当class文件在war中时，此时返回zip:D:/...这样的路径
                path = path.substring(4);
            } else if (path.startsWith("file")) {
                // 当class文件在class文件中时，此时返回file:/D:/...这样的路径
                path = path.substring(6);
            } else if (path.startsWith("jar")) {
                // 当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径
                path = path.substring(10);
            }
        }
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return path;
    }

    /**
     * 获取项目WEBINF目录
     *
     * @return 返回项目WBEINF目录路径
     */
    public String getWEBINFPath() {
        return this.getWebRootPath() + "WEB-INF" + File.separator;
    }

    /**
     * 获取项目CLASS目录
     *
     * @return 返回项目Class目录路径
     */
    public String getClassPath() {
        String rootPath = this.getWebRootPath();
        if (rootPath.contains("classes")) {
            return rootPath;
        } else {
            return this.getWEBINFPath() + "classes" + File.separator;
        }
    }
}
