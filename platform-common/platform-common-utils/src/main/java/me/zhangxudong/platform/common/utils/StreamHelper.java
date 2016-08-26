package me.zhangxudong.platform.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 流处理工具类
 */
public class StreamHelper {

    private static Logger logger = LoggerFactory.getLogger(StreamHelper.class);

    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转换成String
     *
     * @param in InputStream
     * @return String
     */
    public static String inputStream2String(InputStream in) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        String string = null;
        int count;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            logger.error("InputStream转换成String异常", e);
        }

        try {
            string = new String(outStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("InputStream转换成String异常", e);
        }
        return string;
    }

    /**
     * 将InputStream转换成某种字符编码的String
     *
     * @param in       InputStream
     * @param encoding 编码
     * @return String
     */
    public static String inputStream2String(InputStream in, String encoding) {
        String string = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            logger.error("InputStream转换成某种字符编码的String异常", e);
        }

        try {
            string = new String(outStream.toByteArray(), encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("InputStream转换成某种字符编码的String异常", e);
        }
        return string;
    }

    /**
     * 将String转换成InputStream
     *
     * @param in String
     * @return InputStream
     * @throws Exception
     */
    public static InputStream string2InputStream(String in) throws Exception {

        return new ByteArrayInputStream(in.getBytes("UTF-8"));
    }

    /**
     * 将String转换成byte[]
     *
     * @param in String
     * @return byte[]
     */
    public static byte[] string2Byte(String in) {
        byte[] bytes = null;
        try {
            bytes = inputStream2Byte(string2InputStream(in));
        } catch (Exception e) {
            logger.error("String转换成byte[]异常", e);
        }
        return bytes;
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] inputStream2Byte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        return outStream.toByteArray();
    }

    /**
     * 将byte数组转换成InputStream
     *
     * @param in byte[]
     * @return InputStream
     */
    public static InputStream byte2InputStream(byte[] in) {

        return new ByteArrayInputStream(in);
    }

    /**
     * 将byte数组转换成String
     *
     * @param in byte[]
     * @return String
     */
    public static String byte2String(byte[] in) {

        InputStream is = null;
        try {
            is = byte2InputStream(in);
        } catch (Exception e) {
            logger.error("byte数组转换成String", e);
        }
        return is == null ? null : inputStream2String(is, "UTF-8");
    }


    /**
     * 根据文件路径创建文件输入流处理
     * 以字节为单位（非 unicode ）
     *
     * @param filepath 文件路径
     * @return FileInputStream
     */
    public static FileInputStream getFileInputStream(String filepath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
        }
        return fileInputStream;
    }

    /**
     * 根据文件对象创建文件输入流处理
     * 以字节为单位（非 unicode ）
     *
     * @param file File
     * @return FileInputStream
     */
    public static FileInputStream getFileInputStream(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
        }
        return fileInputStream;
    }

    /**
     * 根据文件对象创建文件输出流处理
     * 以字节为单位（非 unicode ）
     *
     * @param file   File
     * @param append true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return FileOutputStream
     */
    public static FileOutputStream getFileOutputStream(File file, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, append);
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
        }
        return fileOutputStream;
    }

    /**
     * 根据文件路径创建文件输出流处理
     * 以字节为单位（非 unicode ）
     *
     * @param filepath 文件路径
     * @param append   true:文件以追加方式打开,false:则覆盖原文件的内容
     * @return FileOutputStream
     */
    public static FileOutputStream getFileOutputStream(String filepath, boolean append) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filepath, append);
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
        }
        return fileOutputStream;
    }

    /**
     * 获得文件
     *
     * @param filepath 文件路径
     * @return File
     */
    public static File getFile(String filepath) {
        return new File(filepath);
    }

}