package com.renny.simplebrowser.business.log.impl;


import com.renny.simplebrowser.business.log.ILogControl;

/**
 * 简单打印器
 * Created by yh on 2016/5/9.
 */
public class SimpleLogControl implements ILogControl {
    private boolean enableD = true;
    private boolean enableE = true;
    private boolean enableI = true;
    private boolean enableW = true;

    public SimpleLogControl setEnableD(boolean enableD) {
        this.enableD = enableD;
        return this;
    }

    public SimpleLogControl setEnableE(boolean enableE) {
        this.enableE = enableE;
        return this;
    }

    public SimpleLogControl setEnableI(boolean enableI) {
        this.enableI = enableI;
        return this;
    }

    public SimpleLogControl setEnableW(boolean enableW) {
        this.enableW = enableW;
        return this;
    }

    @Override
    public boolean enableD() {
        return enableD;
    }

    @Override
    public boolean enableE() {
        return enableE;
    }

    @Override
    public boolean enableI() {
        return enableI;
    }

    @Override
    public boolean enableW() {
        return enableW;
    }
}
