package com.hqvoyage.platform.admin.web.common.config;

import com.hqvoyage.platform.admin.web.utils.upload.FileManager;
import com.hqvoyage.platform.common.upload.FileOperatorOssImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传配置
 * Created by zhangxd on 16/7/14.
 */
@Configuration
@ConfigurationProperties("upload")
public class UploadConfig {

    private String endpoint;
    private String key;
    private String secret;
    private String bucket;
    private String accessUrl;

    @Bean
    public FileManager fileManager() {
        FileManager fileManager = new FileManager();
        FileOperatorOssImpl fileOperator = new FileOperatorOssImpl();
        fileOperator.setEndpoint(endpoint);
        fileOperator.setKey(key);
        fileOperator.setSecret(secret);
        fileOperator.setBucket(bucket);
        fileOperator.setAccessUrl(accessUrl);
        fileManager.setFileOperator(fileOperator);
        return fileManager;
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

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }
}
