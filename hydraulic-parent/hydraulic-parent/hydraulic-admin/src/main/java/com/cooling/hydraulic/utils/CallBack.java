package com.cooling.hydraulic.utils;

/**
 *@author tangcaijun
 *@date 2022/7/10
 *@desc
 *
 */

public interface CallBack {
    /**
     * 回调执行方法
     */
    void executor();

    /**
     * 本回调任务名称
     * @return /
     */
    default String getCallBackName() {
        return Thread.currentThread().getId() + ":" + this.getClass().getName();
    }
}

