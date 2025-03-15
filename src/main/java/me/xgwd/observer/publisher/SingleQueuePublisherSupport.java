package me.xgwd.observer.publisher;

import me.xgwd.observer.bean.Event;
import me.xgwd.observer.subscriber.*;
import me.xgwd.observer.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gbl.huang
 * @date 2025/03/15 12:03
 **/
public class SingleQueuePublisherSupport implements PublisherSupport, Runnable {

    private BlockingQueue<Event> queue;
    AtomicLong lastID = new AtomicLong(0);
    Map<Class<? extends Event>, Set<Subscriber>> map = new ConcurrentHashMap<>();
    public static final Logger logger = LoggerFactory.getLogger(MultiQueuePublisherSupport.class);
    EventStageBridge eventStageBridge;
    int queueSize;

    public SingleQueuePublisherSupport(int queueSize, EventStageBridge eventStageBridge) {
        this.queueSize = queueSize;
        this.eventStageBridge = eventStageBridge;
        queue = new ArrayBlockingQueue<>(queueSize);
        new Thread(this::run).start();
    }

    @Override
    public void addSubscriber(Subscriber subscriber, Class<? extends Event> clazz) {
        Set<Subscriber> subscribers = map.computeIfAbsent(clazz, k -> new ConcurrentHashSet<>());
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber, Class<? extends Event> clazz) {
        Set<Subscriber> subscribers = map.computeIfAbsent(clazz, k -> new ConcurrentHashSet<>());
        subscribers.remove(subscriber);
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        eventStageBridge.addSubscriber(subscriber, this::addSubscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        eventStageBridge.removeSubscriber(subscriber, this::removeSubscriber);
    }


    @Override
    public boolean publish(Event event) {
        // 阻塞放进去
        boolean success = this.queue.offer(event);
        if (!success) {
            receiveEvent(event);
            return true;
        }
        return true;
    }

    @Override
    public void notifySubscriber(Subscriber subscriber, Event event) {
        // TODO 初始校验
        Runnable runnable = () -> subscriber.resolveEvent(event);
        Executor executor = subscriber.executor();
        try {
            if (executor == null) {
                runnable.run();
            } else {
                executor.execute(runnable);
            }
        } catch (Exception e) {
            subscriber.onExceptionCatch();
            throw new RuntimeException("publish message error {}", e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Event take = queue.take();
                receiveEvent(take);
                lastID.compareAndSet(lastID.get(), take.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveEvent(Event event) {
        // 事件序列号，相当于ID
        final long eventId = event.getId();
        Class<? extends Event> subscribeType = event.getClass();
        for (Class<? extends Event> eventType : map.keySet()) {
            if (subscribeType.equals(eventType)) {
                Set<Subscriber> subscribers = map.get(eventType);
                System.out.println(subscribers.size());
                for (Subscriber subscriber : subscribers) {
                    // TODO 下沉到策略
                    if (subscriber.ignoreExpireEvent() && lastID.get() > eventId) {
                        System.out.println("过滤了一条消息拉:" + lastID.get() + " eventId:" + eventId);
                        continue;
                    }
                    notifySubscriber(subscriber, event);
                }
            }
        }
    }
}