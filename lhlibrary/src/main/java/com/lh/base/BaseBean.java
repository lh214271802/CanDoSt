package com.lh.base;

import java.io.Serializable;

/**
 * Created by liaohui on 2017/7/20.
 */

public class BaseBean<T> implements Serializable {
    /**
     * status : SUCCESS
     * msg : 成功
     * timeline : 1471931578
     * errorCode : 0
     */

    public String status;
    public String msg;
    public int timeline;
    public String errorCode;
    public T data;

}
