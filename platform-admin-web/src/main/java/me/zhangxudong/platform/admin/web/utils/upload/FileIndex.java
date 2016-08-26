package me.zhangxudong.platform.admin.web.utils.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileIndex {
	
	private File upfile;
    private MultipartFile mUpfile;

    private String fileid;
    private String truename;
    private String description;
    private String mcode;
    private String keyword;
    private String path;
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public FileIndex(File upfile, String truename, String description, String mcode) {
		super();
		this.upfile = upfile;
		this.truename = truename;
		this.description = description;
		this.mcode = mcode;
	}
	
	public FileIndex() { }
	
	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public File getUpfile() {
		return upfile;
	}

	public void setUpfile(File upfile) {
		this.upfile = upfile;
	}

	public MultipartFile getmUpfile() {
		return mUpfile;
	}
	
	public void setmUpfile(MultipartFile mUpfile) {
		this.mUpfile = mUpfile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
