package com.hqvoyage.platform.common.upload;

import java.io.File;

/**
 * 文件处理器.
 * Created by zhangxd on 15/10/20.
 */
public interface FileOperator {

	/**
	 * 删除一个文件 
	 * 
	 * @param realName 相对路径名.
	 */
	void deleteFile(String realName);
	
	/**
	 * 
	 * @param file 文件
	 * @param realName 相对路径名(访问时候使用,也是存放在数据库中的字段) 
	 */
	void saveFile(File file, String realName);

    /**
     * 获得一个文件的web访问url
     *
     * @param realpath 文件的存放路径,在数据库中保存该信息.
     * @return
     */
    String getFileUrl(String realpath);
	
}


