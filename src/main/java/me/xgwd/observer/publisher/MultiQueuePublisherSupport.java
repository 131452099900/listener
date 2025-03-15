package me.xgwd.observer.publisher;

import me.xgwd.observer.bean.Event;
import me.xgwd.observer.subscriber.EventStageBridge;
import me.xgwd.observer.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gbl.huang
 * @date 2025/03/15 11:11
 **/
public class MultiQueuePublisherSupport implements PublisherSupport {
    public static final Logger logger = LoggerFactory.getLogger(MultiQueuePublisherSupport.class);
    Map<Class<? extends Event>, DefaultPublisher> map = new ConcurrentHashMap<>();

    EventStageBridge eventStageBridge;
    int queueSize;
    public MultiQueuePublisherSupport(int queueSize, EventStageBridge eventStageBridge) {
        this.queueSize = queueSize;
        this.eventStageBridge = eventStageBridge;
    }


    @Override
    public void addSubscriber(Subscriber subscriber) {
        eventStageBridge.addSubscriber(subscriber, this::addSubscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        eventStageBridge.removeSubscriber(subscriber, this::addSubscriber);
    }

    /**
     * 队列通知
     */
    @Override
    public boolean publish(Event event) {
        DefaultPublisher defaultPublisher = map.get(event.getClass());
        if (defaultPublisher == null) {
            logger.warn("publisher is not exist, event type is {}", event.getClass().getSimpleName());
        }
        defaultPublisher.publish(event);
        return false;
    }

    /**
     * 即时通知
     */
    @Override
    public void notifySubscriber(Subscriber subscriber, Event event) {
        DefaultPublisher defaultPublisher = map.get(event.getClass());
        if (defaultPublisher == null) {
            logger.warn("publisher is not exist, event type is {}", event.getClass().getSimpleName());
        }
        defaultPublisher.notifySubscriber(subscriber, event);
    }

    @Override
    public void addSubscriber(Subscriber subscriber, Class<? extends Event> clazz) {
        DefaultPublisher defaultPublisher = map.computeIfAbsent(clazz, k -> new DefaultPublisher(queueSize));
        defaultPublisher.addSubscriber(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber, Class<? extends Event> clazz) {
        DefaultPublisher defaultPublisher = map.computeIfAbsent(clazz, k -> new DefaultPublisher(queueSize));
        defaultPublisher.removeSubscriber(subscriber);
    }
}