package me.zhangxudong.platform.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP工具.
 */
public abstract class GZipUtil {

    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";

    private static Logger logger = LoggerFactory.getLogger(GZipUtil.class);

    /**
     * 数据压缩.
     *
     * @param data byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 压缩  
        compress(bais, baos);

        byte[] output = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return output;
    }

    /**
     * 文件压缩.
     *
     * @param file File
     * @throws Exception
     */
    public static void compress(File file) throws Exception {
        compress(file, true);
    }

    /**
     * 文件压缩.
     *
     * @param file   File
     * @param delete 是否删除原始文件
     * @throws Exception
     */
    public static void compress(File file, boolean delete) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(file.getPath() + EXT);

            compress(fis, fos);
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }

        if (delete) {
            if (file.delete()) {
                logger.error("文件删除失败");
            }
        }
    }

    /**
     * 数据压缩.
     *
     * @param is InputStream
     * @param os OutputStream
     * @throws Exception
     */
    public static void compress(InputStream is, OutputStream os)
            throws Exception {

        GZIPOutputStream gos = new GZIPOutputStream(os);

        int count;
        byte[] data = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();

        gos.flush();
        gos.close();
    }

    /**
     * 文件压缩.
     *
     * @param path 文件路径
     * @throws Exception
     */
    public static void compress(String path) throws Exception {
        compress(path, true);
    }

    /**
     * 文件压缩.
     *
     * @param path   文件路径
     * @param delete 是否删除原始文件
     * @throws Exception
     */
    public static void compress(String path, boolean delete) throws Exception {
        File file = new File(path);
        compress(file, delete);
    }

    /**
     * 数据解压缩.
     *
     * @param data byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 解压缩  
        decompress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return data;
    }

    /**
     * 文件解压缩.
     *
     * @param file File
     * @throws Exception
     */
    public static void decompress(File file) throws Exception {
        decompress(file, true);
    }

    /**
     * 文件解压缩.
     *
     * @param file   File
     * @param delete 是否删除原始文件
     * @throws Exception
     */
    public static void decompress(File file, boolean delete) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(file.getPath().replace(EXT, ""));
            decompress(fis, fos);
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }

        if (delete) {
            if (file.delete()) {
                logger.error("文件删除失败");
            }
        }
    }

    /**
     * 数据解压缩.
     *
     * @param is InputStream
     * @param os OutputStream
     * @throws Exception
     */
    public static void decompress(InputStream is, OutputStream os)
            throws Exception {

        GZIPInputStream gis = new GZIPInputStream(is);

        int count;
        byte[] data = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    /**
     * 文件解压缩.
     *
     * @param path 文件路径
     * @throws Exception
     */
    public static void decompress(String path) throws Exception {
        decompress(path, true);
    }

    /**
     * 文件解压缩.
     *
     * @param path   文件路径
     * @param delete 是否删除原始文件.
     * @throws Exception
     */
    public static void decompress(String path, boolean delete) throws Exception {
        File file = new File(path);
        decompress(file, delete);
    }
}  