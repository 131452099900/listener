package me.xgwd.observer.publisher;

import me.xgwd.observer.bean.Event;
import me.xgwd.observer.publisher.Publisher;
import me.xgwd.observer.subscriber.Subscriber;
import me.xgwd.observer.util.ConcurrentHashSet;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gbl.huang
 * @date 2025/03/14 21:09
 **/
public class DefaultPublisher implements Publisher, Runnable {
    /**
     * 事件对应的监听者
     */
    private final Set<Subscriber> subscribersSet = new ConcurrentHashSet<>();

    private int queueSize = 1024;

    private BlockingQueue<Event> queue;

    AtomicLong lastID = new AtomicLong(0);


    public DefaultPublisher(int queueSize) {
        this.queueSize = queueSize;
        queue = new ArrayBlockingQueue<>(queueSize);
        new Thread(this::run).start();
    }


    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribersSet.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscribersSet.remove(subscriber);
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
        for (Subscriber subscriber : subscribersSet) {
            // TODO 下沉到策略
            if (subscriber.ignoreExpireEvent() && lastID.get() > eventId) {
                continue;
            }
            notifySubscriber(subscriber, event);
        }
    }


}