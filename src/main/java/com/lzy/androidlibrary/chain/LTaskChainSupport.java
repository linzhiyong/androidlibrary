package com.lzy.androidlibrary.chain;

/**
 * 责任链任务接口抽象实现类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/27
 * @desc
 */
public abstract class LTaskChainSupport implements LTaskChain {

    private LTaskChain taskChain;

    @Override
    public void execute() {
        doWork();
        doNext();
    }

    public abstract void doWork();

    public void doNext() {
        if (getNext() != null) {
            getNext().execute();
        }
    }

    @Override
    public void setNext(LTaskChain taskChain) {
        this.taskChain = taskChain;
    }

    @Override
    public LTaskChain getNext() {
        return taskChain;
    }
}
