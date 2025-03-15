package me.xgwd.observer.subscriber;

import me.xgwd.observer.bean.Event;

import java.util.Set;

/**
 * @author gbl.huang
 * @date 2025/03/14 20:17
 **/
public abstract class MultiEventSubscriber extends Subscriber {

    public abstract Set<Class<? extends Event>> subscribeTypes();

    public abstract void onEvent(Event event);

    @Override
    public void resolveEvent(Event event) {
        if (subscribeTypes().contains(event.getClass())) {
            onEvent(event);
        }
    }

    @Override
    public boolean suit(Event event) {
        return true;
    }
}