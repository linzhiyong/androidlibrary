package com.lzy.androidlibrary.chain;

/**
 * 责任链任务接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/27
 * @desc
 */
public interface LTaskChain {

    public void setNext(LTaskChain taskChain);

    public LTaskChain getNext();

    public void execute();

}
