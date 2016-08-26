package me.zhangxudong.platform.common.api;

import me.zhangxudong.platform.common.utils.RandomHelper;

/**
 * 数据Entity类
 * Created by zhangxd on 15/10/20.
 */
public abstract class DataEntity<T> extends BaseEntity<T> {

    private static final long serialVersionUID = 1L;

    public DataEntity() {
        super();
    }

    public DataEntity(String id) {
        super(id);
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord) {
            setId(RandomHelper.uuid());
        }
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate() {

    }

}
