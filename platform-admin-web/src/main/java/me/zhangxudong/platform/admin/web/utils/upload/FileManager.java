package me.zhangxudong.platform.admin.web.utils.upload;

import me.zhangxudong.platform.admin.web.common.base.ServletContextHolder;
import me.zhangxudong.platform.common.upload.FileOperator;
import me.zhangxudong.platform.common.utils.RandomHelper;
import me.zhangxudong.platform.common.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件管理助手类.
 * 提供文件的保存，获取，删除等与文件管理相关的方法.
 * 为了实现统一的文件处理方案，所有要交由系统保存的文件均通过此方式处理.
 *
 * @author zhangxd
 * @date 2015年7月19日
 */
public class FileManager {

    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

    /**
     * 删除文件,包括数据库中的记录和实际文件.
     *
     * @param filepath
     * @return
     */
    public boolean delete(String filepath) {

        // 调用文件存储器删除文件.
        getFileOperator().deleteFile(filepath);

        return true;
    }

    /**
     * 删除文件集合,包括数据库中的记录和实际文件.
     *
     * @param filePathList
     * @return
     */
    public boolean deleteList(final List<String> filePathList) {
        if (filePathList == null || filePathList.size() == 0) {
            throw new RuntimeException("Parameter filePathList must not null");
        }

        if (filePathList.size() == 1) {
            return delete(filePathList.get(0));
        }

        for (String path : filePathList) {
            // 调用文件存储器删除文件.
            getFileOperator().deleteFile(path);
        }

        return true;
    }

    /**
     * 将一个"未保存"的文件保存,返回保存后的信息.
     *
     * @param ufi
     * @return
     */
    public FileIndex save(FileIndex ufi) {
        FileIndex[] fs = saves(ufi);
        return fs[0];
    }

    /**
     * 保存文件到文件管理系统.
     *
     * @param ufis
     * @return
     */
    public FileIndex[] saves(FileIndex... ufis) {

        List<FileIndex> arr = new ArrayList<>();
        for (FileIndex ufi : ufis) {

            String trueName = Objects.toString(ufi.getTruename());

            if (StringHelper.isEmpty(trueName)) {
                continue;
            }

            String id = RandomHelper.uuid();

            ufi.setFileid(id);

            String folderPath = ufi.getMcode() + "/" + id.substring(0, 2);
            String suffix = "";
            int pos = trueName.lastIndexOf(".");
            if (pos > -1) {
                suffix = trueName.substring(pos + 1);
            }
            String saveFile = id + (StringHelper.isNotEmpty(suffix) ? ("." + suffix) : "");
            String filePath = folderPath + "/" + saveFile;
            ufi.setPath(filePath);

            File upFile = ufi.getUpfile();
            if (upFile == null || !upFile.exists()) {
                upFile = getFile(saveFile);
                try {
                    ufi.getmUpfile().transferTo(upFile);
                } catch (IllegalStateException | IOException ex) {
                    throw new IllegalStateException(ex.getMessage());
                }
            }

            getFileOperator().saveFile(upFile, filePath);
            if (ufi.getUpfile() == null && ufi.getmUpfile() != null) {
                if (!upFile.delete()) {
                    logger.warn("上传临时文件删除失败.");
                }
            }

            arr.add(ufi);
        }
        return arr.toArray(new FileIndex[0]);
    }


    /**
     * 获得系统当前配置的文件保存方式.
     * 系统当前支持的文件获取方式有：硬盘(相对或绝对路径)，http.
     * 硬盘方式支持 局域网通过共享文件夹的方式.
     *
     * @return
     */
    public FileOperator getFileOperator() {
        if (fileOperator == null) {
            throw new RuntimeException("File Operator cannot be null.");
        }
        return fileOperator;
    }

    /**
     * 获得文件的访问路径.
     *
     * @param realpath
     * @return
     */
    public String getFileUrlByRealpath(String realpath) {
        return getFileOperator().getFileUrl(realpath);
    }


    /**
     * 设置(修改)文件保存方式.
     *
     * @param fileOperator
     */
    public void setFileOperator(FileOperator fileOperator) {
        this.fileOperator = fileOperator;
    }

    private FileOperator fileOperator;


    /**
     * 是否是一个绝对路径.
     *
     * @param path
     * @return
     */
    private boolean isAbsolutePath(String path) {
        return path.startsWith("/") || path.startsWith("\\") || path.contains(":");
    }


    /**
     * 获取文件,如果是相对路径,则以AppPath为Parent
     *
     * @param name
     * @return
     */
    private File getFile(String name) {
        String appPath = ServletContextHolder.getAppPath();
        return isAbsolutePath(name) ? new File(name) : new File(new File(appPath), name);
    }
}
