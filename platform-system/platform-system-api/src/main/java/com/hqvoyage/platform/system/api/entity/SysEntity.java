package com.hqvoyage.platform.system.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hqvoyage.platform.common.api.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 系统Entity基类
 * Created by zhangxd on 15/10/20.
 */
public abstract class SysEntity<T> extends DataEntity<T> {

    private static final long serialVersionUID = 1L;

    protected String remarks;    // 备注
    protected Date createDate;    // 创建日期
    protected Date updateDate;    // 更新日期
    protected String delFlag;    // 删除标记(0:正常;1:删除;)

    public SysEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public SysEntity(String id) {
        super(id);
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() {
        super.preInsert();
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate() {
        this.updateDate = new Date();
    }

    @Length(min = 0, max = 255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @JsonIgnore
    @Length(min = 1, max = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
