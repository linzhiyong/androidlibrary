package com.lzy.androidlibrary.chain;

/**
 * 责任链执行器接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/27
 * @desc
 */
public interface LTaskChainBuilder {

    public void addFirstTaskChain(LTaskChain taskChain);

    public void addLastTaskChain(LTaskChain taskChain);

    public void execute();

}
