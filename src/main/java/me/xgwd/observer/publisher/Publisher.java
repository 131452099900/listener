package me.xgwd.observer.publisher;

import me.xgwd.observer.bean.Event;
import me.xgwd.observer.subscriber.Subscriber;

/**
 * @author gbl.huang
 * @date 2025/03/14 21:10
 **/
public interface Publisher {
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