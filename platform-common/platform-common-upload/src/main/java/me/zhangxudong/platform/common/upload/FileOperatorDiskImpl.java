package me.zhangxudong.platform.common.upload;


import me.zhangxudong.platform.common.utils.FileHelper;

import java.io.File;

/**
 * 文件硬盘操作
 * Created by zhangxd on 15/10/20.
 */
public class FileOperatorDiskImpl extends AbstractFileOperator implements FileOperator {

    private String workFolderName;

	public void deleteFile(String realName) {
		FileHelper.delFile(FileHelper.addEndSlash(workFolderName) + realName);
	}
	
	public String getWorkFolderName() {
		return workFolderName;
	}


	public void saveFile(File file, String realName) {
        FileHelper.copy(file.getAbsolutePath(), FileHelper.addEndSlash(workFolderName)  + realName);
	}

	/**
	 * 设置存放文件的目录名称,可以是相对于当前appPath的目录(例如../files, d:/tmp, /tmp).
	 * 
	 * @param workFolderName
	 */
	public void setWorkFolderName(String workFolderName) {
		this.workFolderName = workFolderName;
	}

}
