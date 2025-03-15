package me.xgwd.observer.subscriber;

import me.xgwd.observer.bean.Event;

import java.util.concurrent.Executor;

/**
 * @author gbl.huang
 * @date 2025/03/14 19:59
 **/
public abstract class Subscriber  {
    Executor executor;
    public Executor executor() {
        return executor;
    }
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }


    public abstract void resolveEvent(Event event);

    public void onExceptionCatch() {

    }

    /**
     * 后续拓展使用
     * @param event
     * @return
     */
    public abstract boolean suit(Event event);

    public boolean ignoreExpireEvent() {
        return false;
    }

}