package me.zhangxudong.platform.common.upload;


import me.zhangxudong.platform.common.utils.FileHelper;
import me.zhangxudong.platform.common.utils.StringHelper;

/**
 * 文件处理器.
 * Created by zhangxd on 15/10/20.
 */
public abstract class AbstractFileOperator implements FileOperator {

    /**
     * 文件服务器地址
     */
    private String accessUrl;

    /**
     * 获得一个文件的web访问url
     *
     * @param realPath 文件的存放路径,在数据库中保存该信息.
     * @return
     */
    public String getFileUrl(String realPath) {
        if (StringHelper.isEmpty(accessUrl)) {
            throw new NullPointerException("文件服务器访问地址为空.");
        }
        return StringHelper.isEmpty(realPath) ? "" : FileHelper.addEndSlash(accessUrl)  + realPath;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }
}


