package me.zhangxudong.platform.common.upload;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

import java.io.File;

/**
 * 文件OSS操作
 * Created by zhangxd on 15/10/20.
 */
public class FileOperatorOssImpl extends AbstractFileOperator implements FileOperator {

    private String endpoint;
    private String key;
    private String secret;
    private String bucket;

	public void deleteFile(String realName) {
        // 创建ClientConfiguration实例，按照您的需要修改默认参数
        ClientConfiguration conf = new ClientConfiguration();
        // 开启支持CNAME选项
        conf.setSupportCname(true);
        // 创建OSSClient实例
        OSSClient client = new OSSClient(endpoint, key, secret, conf);

        client.deleteObject(bucket, realName);

        // 关闭client
        client.shutdown();
	}
	
	public void saveFile(File file, String realName) {
        // 创建ClientConfiguration实例，按照您的需要修改默认参数
        ClientConfiguration conf = new ClientConfiguration();
        // 开启支持CNAME选项
        conf.setSupportCname(true);
        // 创建OSSClient实例
        OSSClient client = new OSSClient(endpoint, key, secret, conf);

        client.putObject(bucket, realName, file);

        // 关闭client
        client.shutdown();
	}

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
