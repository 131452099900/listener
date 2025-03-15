package me.xgwd.observer.publisher;

import me.xgwd.observer.bean.Event;
import me.xgwd.observer.subscriber.Subscriber;

public interface PublisherManager {
    void addSubscriber(Subscriber subscriber, Class<? extends Event> clazz);

    void removeSubscriber(Subscriber subscriber, Class<? extends Event> clazz);
}
