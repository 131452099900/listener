package me.xgwd.observer.subscriber;

import me.xgwd.observer.bean.Event;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author gbl.huang
 * @date 2025/03/15 11:12
 * 桥接
 **/
public interface EventStageBridge {
    void addSubscriber(Subscriber subscriber, BiConsumer<Subscriber, Class<? extends Event>> consumer);

    void removeSubscriber(Subscriber subscriber, BiConsumer<Subscriber, Class<? extends Event>> consumer);

}