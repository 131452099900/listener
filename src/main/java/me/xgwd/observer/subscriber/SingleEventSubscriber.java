package me.xgwd.observer.subscriber;

import me.xgwd.observer.bean.Event;

/**
 * @author gbl.huang
 * @date 2025/03/14 20:01
 **/
public abstract class SingleEventSubscriber<T extends Event> extends Subscriber{

    public abstract void onEvent(T event);

    public abstract Class<? extends Event> subscribeType();

    @Override
    public void resolveEvent(Event event) {
        if (subscribeType().isInstance(event)) {
            onEvent((T) event);
        }
    }
}