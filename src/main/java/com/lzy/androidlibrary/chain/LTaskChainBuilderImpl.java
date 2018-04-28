package com.lzy.androidlibrary.chain;

/**
 * 责任链执行器实现类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/27
 * @desc
 */
public class LTaskChainBuilderImpl implements LTaskChainBuilder {

    private LTaskChain firstTaskChain;
    private LTaskChain lastTaskChain;

    public LTaskChainBuilderImpl() {
        this.firstTaskChain = null;
        this.lastTaskChain = null;
    }

    @Override
    public void addFirstTaskChain(LTaskChain taskChain) {
        if (taskChain == null) {
            return;
        }

        if (this.firstTaskChain == null) {
            this.firstTaskChain = taskChain;
            this.lastTaskChain = taskChain;
        } else {
            taskChain.setNext(this.firstTaskChain);
            this.firstTaskChain = taskChain;
        }
    }

    @Override
    public void addLastTaskChain(LTaskChain taskChain) {
        if (taskChain == null) {
            return;
        }

        if (this.firstTaskChain == null) {
            this.firstTaskChain = taskChain;
            this.lastTaskChain = taskChain;
        } else {
            this.lastTaskChain.setNext(taskChain);
            this.lastTaskChain = taskChain;
        }
    }

    @Override
    public void execute() {
        if (this.firstTaskChain == null) {
            return;
        }
        this.firstTaskChain.execute();
    }

}
