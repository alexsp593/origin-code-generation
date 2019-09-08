package com.origin.generation.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * 对属性文件操作的工具类
 *
 * @author alex
 * @date 2019-09-07
 */
public class PropertiesUtils {
    private static Logger logger = Logger.getLogger(PropertiesUtils.class);
    private Properties proper;
    //默认加载配置文件
    private String propFileName = "website.properties";

    public PropertiesUtils() {
        proper = new Properties();
        loadFile();
    }

    public PropertiesUtils(String filename) {
        proper = new Properties();
        this.propFileName = filename;
        loadFile();
    }

    /**
     * 加载属性文件中的内容
     *
     * @return 是否加载成功，文件路径是否正确
     */
    public boolean loadFile() {
        String strPath = PathUtils.getInstance().getClassPath() + propFileName;
        try {
            InputStream in = new FileInputStream(strPath);
            return loadReader(in);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("配置文件加载失败" + strPath);
            return false;
        }
    }

    /**
     * 读取字符流加载内容
     *
     * @param stream 要读取的字符流
     * @return
     */
    public synchronized boolean loadReader(InputStream stream) {
        try {
            proper.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取Properties类型加载器
     *
     * @return
     */
    public synchronized Properties getProper() {
        return proper;
    }

    /**
     * 加载一个字符串。如果没有对应的key，就返回null
     *
     * @param key 键值对中的key
     * @return
     */
    public String getString(String key) {
        return getProper().getProperty(key);
    }

    /**
     * 加载一个String数组，并且以regex正则表达式分割该字符串。如果没有对应的key则返回null
     *
     * @param key   键值对中的key
     * @param regex 正则表达式
     * @return
     */
    public String[] getStrings(String key, String regex) {
        String str = getProper().getProperty(key);
        if (str == null) {
            return null;
        }
        return str.split(regex);
    }

    /**
     * 加载一个Integer数组，并且以regex正则表达式分割该字符串。如果没有对应的key则返回null
     *
     * @param key
     * @param regex
     * @return
     */
    public int[] getIntegers(String key, String regex) {
        String[] strs = getStrings(key, regex);
        if (strs == null) {
            return null;
        }
        int[] ints = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            ints[i] = Integer.parseInt(strs[i]);
        }
        return ints;
    }


    /**
     * 加载一个Character。如果没有对应的key或者value的长度不是1，则返回null
     *
     * @param key
     * @return
     */
    public Character getCharacter(String key) {
        String str = getProper().getProperty(key);
        if (str == null || str.length() != 1) {
            return null;
        }
        return str.charAt(0);
    }

    /**
     * 加载一个Boolean。如果没有对应的key，则返回null；如果值是"true"或"1"，返回true，否则返回false
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        String str = getProper().getProperty(key);
        if (str == null) {
            return null;
        }
        //这里比较"true"时，使用了不区分大小写的比较  
        if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 加载一个Integer。如果没有对应的key或者value无法转换成为Integer，则返回null
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
        String str = getProper().getProperty(key);
        if (str == null) {
            return null;
        }
        Integer value = null;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 加载一个int。如果没有对应的key或者value无法转换成为Integer，则返回0
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        String str = getProper().getProperty(key);
        if (str == null) {
            return 0;
        }
        int value = 0;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 加载一个Double。如果没有对应的key或者value无法转换成为Double，则返回null
     *
     * @param key
     * @return
     */
    public Double getDouble(String key) {
        String str = getProper().getProperty(key);
        if (str == null) {
            return null;
        }
        Double value = null;
        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 文件的键值对中是否包含该键
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        return getProper().containsKey(key);
    }

    /**
     * 获取所有的键
     *
     * @return
     */
    public Set<Object> getKeySet() {
        return getProper().keySet();
    }

    /**
     * 获取键值对的集合
     *
     * @return
     */
    public Set<Entry<Object, Object>> getEntrySet() {
        return getProper().entrySet();
    }

    /**
     * MAP形式返回获取键值对集合
     *
     * @return map
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, String> getKeyValue() {

        Set<Entry<Object, Object>> keyValues = getEntrySet();
        Iterator it3 = keyValues.iterator();
        Map<String, String> map = new HashMap<String, String>();
        while (it3.hasNext()) {
            Entry<Object, Object> en = (Entry<Object, Object>) it3.next();
            map.put(en.getKey().toString(), en.getValue().toString());
        }
        return map;
    }

    /**
     * 将已经加载的和改变的所有的东西写入文件中，如果该文件存在则覆盖，没有则创建，使用文件系统默认编码
     *
     * @param filename
     * @return
     */
    public boolean writeToFile(String filename) {
        return writeToFile(filename, System.getProperty("file.encoding"));
    }

    /**
     * 将已经加载的和改变的所有的东西写入文件中，如果该文件存在则覆盖，没有则创建
     *
     * @param filename 要写入的文件的路径
     * @param encoding 要写入的文件的路径
     * @return
     */
    public boolean writeToFile(String filename, String encoding) {
        //检查该文件是否存在，如果没有则创建  
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        //创建输出流  
        try {
            FileOutputStream stream = new FileOutputStream(file);
            return writeToStream(stream, encoding);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将已经加载的和改变的所有的东西写入字节流中，使用文件指定的编码
     *
     * @param stream   要写入的字节流
     * @param encoding 字符编码
     * @return
     */
    public boolean writeToStream(OutputStream stream, String encoding) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(stream, encoding);
            return writeToWriter(writer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将已经加载的和改变的所有的东西写入字节流中，使用文件系统默认编码
     *
     * @param stream 要写入的字节流
     * @return
     */
    public boolean writeToStream(OutputStream stream) {
        return writeToStream(stream, System.getProperty("file.encoding"));
    }

    /**
     * 将已经加载的和改变的所有东西写入字符流中
     *
     * @param writer 要写入的字符流
     * @return
     */
    public boolean writeToWriter(Writer writer) {
        try {
            getProper().store(writer, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 为KEY设置值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        getProper().setProperty(key, value);
    }

    /**
     * @param args
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) {
        PropertiesUtils proUtil = new PropertiesUtils();
        String filename = new File("src/express.properties").getAbsolutePath();
        System.out.println("filename===" + filename);
        //System.out.println("加载结果==="+proUtil.loadFile(filename,"UTF-8"));  
        System.out.println("**********下面输出一些键值对********");
        System.out.println("username==" + proUtil.getString("SF_CHECKWORD"));
        System.out.println("**********下面输出所有的Key********");
        for (Object obj : proUtil.getKeySet()) {
            System.out.println("key=" + obj);
        }
        System.out.println("**********下面输出所有的Key-Value********");
        Set<Entry<Object, Object>> keyValues = proUtil.getEntrySet();
        Iterator it3 = keyValues.iterator();
        while (it3.hasNext()) {
            Entry<Object, Object> en = (Entry<Object, Object>) it3.next();
            System.out.println("key-> " + en.getKey());
            System.out.println("value-> " + en.getValue());
        }
    }

}
