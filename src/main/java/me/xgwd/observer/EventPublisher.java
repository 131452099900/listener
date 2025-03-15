package me.xgwd.observer;

import me.xgwd.observer.bean.Event;
import me.xgwd.observer.subscriber.Subscriber;

/**
 * 单事件广播者
 */
public interface EventPublisher {
    /**
     * 添加监听者
     * @param subscriber 添加监听者
     */
    void addSubscriber(Subscriber subscriber);

    /**
     * 删除监听者
     * @param subscriber
     */
    void removeSubscriber(Subscriber subscriber);

    boolean publish(Event event);

    void notifySubscriber(Subscriber subscriber, Event event);
}
