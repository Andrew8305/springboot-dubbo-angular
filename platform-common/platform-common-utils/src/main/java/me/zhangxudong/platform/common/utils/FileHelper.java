package me.zhangxudong.platform.common.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;

/**
 * 文件操作工具类
 * 实现文件的创建、删除、复制以及目录的创建、删除、复制等功能
 */
public class FileHelper extends FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    public static final int BUF_SIZE = 1024 * 100;

    public static final String DEFAULT_ENCODING = "utf8";

    public static final long ONE_KB = 1024;
    public static final long ONE_MB = ONE_KB * 1024;
    public static final long ONE_GB = ONE_MB * 1024;
    public static final long ONE_TB = ONE_GB * (long) 1024;
    public static final long ONE_PB = ONE_TB * (long) 1024;

    /**
     * 复制文件，可以复制单个文件或文件夹
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @return 如果复制成功，则返回true，否是返回false
     */
    public static boolean copy(String srcFileName, String descFileName) {
        File file = new File(srcFileName);
        if (!file.exists()) {
            logger.debug(srcFileName + " 文件不存在!");
            return false;
        } else {
            if (file.isFile()) {
                return !copyFile(srcFileName, descFileName);
            } else {
                return !copyDirectory(srcFileName, descFileName);
            }
        }
    }

    /**
     * 复制单个文件，如果目标文件存在，则不覆盖
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName) {
        return FileHelper.copyFileCover(srcFileName, descFileName, false);
    }

    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @param coverlay     如果目标文件已存在，是否覆盖
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFileCover(String srcFileName,
                                        String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            logger.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
            return false;
        }
        // 判断源文件是否是合法的文件
        else if (!srcFile.isFile()) {
            logger.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
            return false;
        }
        File descFile = new File(descFileName);
        // 判断目标文件是否存在
        if (descFile.exists()) {
            // 如果目标文件存在，并且允许覆盖
            if (coverlay) {
                logger.debug("目标文件已存在，准备删除!");
                if (FileHelper.delFile(descFileName)) {
                    logger.debug("删除目标文件 " + descFileName + " 失败!");
                    return false;
                }
            } else {
                logger.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建目录
                logger.debug("目标文件所在的目录不存在，创建目录!");
                // 创建目标文件所在的目录
                if (!descFile.getParentFile().mkdirs()) {
                    logger.debug("创建目标文件所在的目录失败!");
                    return false;
                }
            }
        }

        // 准备复制文件
        // 读取的位数
        int readByte;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // 打开源文件
            ins = new FileInputStream(srcFile);
            // 打开目标文件的输出流
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            // 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
            while ((readByte = ins.read(buf)) != -1) {
                // 将读取的字节流写入到输出流
                outs.write(buf, 0, readByte);
            }
            logger.debug("复制单个文件 " + srcFileName + " 到" + descFileName
                    + "成功!");
            return true;
        } catch (Exception e) {
            logger.debug("复制文件失败：" + e.getMessage());
            return false;
        } finally {
            // 关闭输入输出流，首先关闭输出流，然后再关闭输入流
            close(outs);
            close(ins);
        }
    }

    /**
     * 复制整个目录的内容，如果目标目录存在，则不覆盖
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return FileHelper.copyDirectoryCover(srcDirName, descDirName,
                false);
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @param coverlay    如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectoryCover(String srcDirName,
                                             String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            logger.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            logger.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            if (coverlay) {
                // 允许覆盖目标目录
                logger.debug("目标目录已存在，准备删除!");
                if (FileHelper.delFile(descDirNames)) {
                    logger.debug("删除目录 " + descDirNames + " 失败!");
                    return false;
                }
            } else {
                logger.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
                return false;
            }
        } else {
            // 创建目标目录
            logger.debug("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                logger.debug("创建目标目录失败!");
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        if (files != null) {
            for (File file : files) {
                // 如果是一个单个文件，则直接复制
                if (file.isFile()) {
                    flag = FileHelper.copyFile(file.getAbsolutePath(),
                            descDirName + file.getName());
                    // 如果拷贝文件失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
                // 如果是子目录，则继续复制目录
                if (file.isDirectory()) {
                    flag = FileHelper.copyDirectory(file
                            .getAbsolutePath(), descDirName + file.getName());
                    // 如果拷贝目录失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
            }
        }

        if (!flag) {
            logger.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
            return false;
        }
        logger.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
        return true;

    }

    /**
     * Stream copy, use default buf_size.
     *
     * @param is InputStream
     * @param os OutputStream
     * @throws IOException
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {
        copy(is, os, BUF_SIZE);
    }

    /**
     * copy data from reader to writer.
     *
     * @param reader Reader
     * @param writer Writer
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[BUF_SIZE];
        int len;
        try {
            while ((len = reader.read(buf)) != -1) {
                writer.write(buf, 0, len);
            }
        } finally {
            close(reader);
        }

    }

    /**
     * Stream copy.
     *
     * @param is      InputStream
     * @param os      OutputStream
     * @param bufsize int
     * @throws IOException
     */
    public static void copy(InputStream is, OutputStream os, int bufsize) throws IOException {
        byte[] buf = new byte[bufsize];
        int c;
        try {
            while ((c = is.read(buf)) != -1) {
                os.write(buf, 0, c);
            }
        } finally {
            close(is);
        }
    }

    /**
     * 将目标的文件或目录移动到新位置上.
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @return 如果移动成功，则返回true，否是返回false
     */
    public static boolean move(String srcFileName, String descFileName) {
        return copy(srcFileName, descFileName) && delFile(srcFileName);
    }

    /**
     * 删除文件，可以删除单个文件或文件夹
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.debug(fileName + " 文件不存在!");
            return false;
        } else {
            if (file.isFile()) {
                return FileHelper.deleteFile(fileName);
            } else {
                return FileHelper.deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                logger.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            logger.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            logger.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        if (files != null) {
            for (File file : files) {
                // 删除子文件
                if (file.isFile()) {
                    flag = FileHelper.deleteFile(file.getAbsolutePath());
                    // 如果删除文件失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
                // 删除子目录
                else if (file.isDirectory()) {
                    flag = FileHelper.deleteDirectory(file
                            .getAbsolutePath());
                    // 如果删除子目录失败，则退出循环
                    if (!flag) {
                        break;
                    }
                }
            }
        }

        if (!flag) {
            logger.debug("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            logger.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            logger.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }

    /**
     * 清空一个目录.
     *
     * @param dirName 需要清除的目录.如果该参数实际上是一个file,不处理,返回true,
     * @return 是否清除成功
     */
    public static boolean clearFolder(String dirName) {
        File file = new File(dirName);
        return file.isFile() || _clearFolder(file);
    }

    /**
     * 清空目录
     *
     * @param folder 目标目录
     * @return 是否清除成功
     */
    private static boolean _clearFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File child : files) {
                boolean flag = child.isFile() ? deleteFile(child.getPath()) : deleteDirectory(child.getPath());
                if (!flag) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 创建单个文件
     *
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            logger.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            logger.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                logger.debug("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                logger.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                logger.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    /**
     * 创建目录
     *
     * @param descDirName 目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            logger.debug("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            logger.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            logger.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }

    }

    /**
     * 从指定Reader中读取数据字符串.
     *
     * @param reader Reader
     * @return String
     * @throws IOException
     */
    public static String read(Reader reader) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        copy(reader, writer);
        return writer.toString();
    }

    /**
     * 从指定文件中读取数据字符串.
     *
     * @param file File
     * @return String
     * @throws IOException
     */
    public static String readData(File file) throws IOException {
        return readData(new FileInputStream(file));
    }

    /**
     * 读取数据流.
     *
     * @param is InputStream
     * @return String
     * @throws IOException
     */
    public static String readData(InputStream is) throws IOException {
        return new String(StreamHelper.inputStream2Byte(is), DEFAULT_ENCODING);
    }

    /**
     * 从指定文件中读取数据字符串.
     *
     * @param file 路径
     * @return String
     * @throws IOException
     */
    public static String readData(String file) throws IOException {
        return readData(new File(file));
    }

    /**
     * 保存一个数据到指定文件中.
     *
     * @param file 文件
     * @param data 内容
     * @throws IOException
     */
    public static void saveData(File file, String data) throws IOException {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) return;
        }
        saveData(new FileOutputStream(file), data);
    }

    /**
     * 将数据保存到指定位置上.
     *
     * @param file   文件
     * @param data   内容
     * @param append 是否追加
     * @throws IOException
     */
    public static void saveData(String file, String data, Boolean append) throws IOException {
        saveData(new File(file), data, append);
    }

    /**
     * 保存一个数据到指定文件中
     *
     * @param file   文件
     * @param data   内容
     * @param append 是否追加
     * @throws IOException
     */
    public static void saveData(File file, String data, Boolean append) throws IOException {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) return;
        }
        saveData(new FileOutputStream(file, append), data);
    }

    /**
     * 保存bytes到一个输出流中并且关闭它
     *
     * @param os   输出流
     * @param data 内容
     * @throws IOException
     */
    public static void saveData(OutputStream os, byte[] data) throws IOException {
        try {
            os.write(data);
        } finally {
            close(os);
        }
    }

    /**
     * 保存String到指定输出流中.
     *
     * @param os   输出流
     * @param data 内容
     * @throws IOException
     */
    public static void saveData(OutputStream os, String data) throws IOException {
        BufferedOutputStream bos = null;
        try {
            byte[] bs = data.getBytes(DEFAULT_ENCODING);
            bos = new BufferedOutputStream(os, BUF_SIZE);
            bos.write(bs);
        } finally {
            close(bos);
        }
    }

    /**
     * 将数据保存到指定位置上.
     *
     * @param file 文件路径
     * @param data 内容
     * @throws IOException
     */
    public static void saveData(String file, String data) throws IOException {
        saveData(new File(file), data);
    }

    /**
     * 获取待压缩文件在ZIP文件中entry的名字，即相对于根目录的相对路径名
     *
     * @param dirPath 目录名
     * @param file    entry文件名
     * @return entry名
     */
    private static String getEntryName(String dirPath, File file) {
        String dirPaths = dirPath;
        if (!dirPaths.endsWith(File.separator)) {
            dirPaths = dirPaths + File.separator;
        }
        String filePath = file.getAbsolutePath();
        // 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
        if (file.isDirectory()) {
            filePath += "/";
        }
        int index = filePath.indexOf(dirPaths);

        return filePath.substring(index + dirPaths.length());
    }

    /**
     * 修复路径，将 \\ 或 / 等替换为 File.separator
     *
     * @param path 路径
     * @return 路径
     */
    public static String path(String path) {
        String p = StringHelper.replace(path, "\\", "/");
        p = StringHelper.join(StringHelper.split(p, "/"), "/");
        if (!StringHelper.startsWithAny(p, "/") && StringHelper.startsWithAny(path, "\\", "/")) {
            p += "/";
        }
        if (!StringHelper.endsWithAny(p, "/") && StringHelper.endsWithAny(path, "\\", "/")) {
            p = p + "/";
        }
        return p;
    }

    /**
     * 获得文件经过Base64编码后的字符串
     *
     * @param file 文件
     * @return Base64字符串
     */
    public static String getBase64File(File file) {
        String fileString = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] b = StreamHelper.inputStream2Byte(fis);
            fileString = EncodeHelper.encodeBase64(b);
        } catch (Exception e) {
            logger.error("文件Base64转码失败", e);
        }
        return fileString;
    }

    /**
     * 关闭流.
     *
     * @param closeable 流
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.error("数据流关闭失败.");
            }
        }
    }

    /**
     * 获得格式化的文件大小
     *
     * @param fileSize 文件大小
     * @return String
     */
    public static String getHumanReadableFileSize(Long fileSize) {
        if (fileSize == null) return null;
        return getHumanReadableFileSize(fileSize.longValue());
    }

    /**
     * 获得格式化的文件大小
     *
     * @param fileSize 文件大小
     * @return String
     */
    public static String getHumanReadableFileSize(long fileSize) {
        if (fileSize < 0) {
            return String.valueOf(fileSize);
        }
        String result = getHumanReadableFileSize(fileSize, ONE_PB, "PB");
        if (result != null) {
            return result;
        }

        result = getHumanReadableFileSize(fileSize, ONE_TB, "TB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_GB, "GB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_MB, "MB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_KB, "KB");
        if (result != null) {
            return result;
        }
        return String.valueOf(fileSize) + "B";
    }

    /**
     * 获得格式化的文件大小
     *
     * @param fileSize 文件大小
     * @param unit     单位
     * @param unitName 单位名称
     * @return String
     */
    private static String getHumanReadableFileSize(long fileSize, long unit, String unitName) {
        if (fileSize == 0) return "0";

        if (fileSize / unit >= 1) {
            double value = fileSize / (double) unit;
            DecimalFormat df = new DecimalFormat("######.##" + unitName);
            return df.format(value);
        }
        return null;
    }

    public static String addEndSlash(String name) {
        return StringHelper.isEmpty(name) || name.endsWith("/") ? name : name + "/";
    }

    public static String clearEndSlash(String name) {
        return StringHelper.isEmpty(name) || !name.endsWith("/") ? name : name.substring(0, name.length() - 1);
    }

}
