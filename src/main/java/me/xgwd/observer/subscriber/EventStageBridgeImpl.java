package me.xgwd.observer.subscriber;

import me.xgwd.observer.bean.Event;
import java.util.function.BiConsumer;

/**
 * @author gbl.huang
 * @date 2025/03/15 11:17
 **/
public class EventStageBridgeImpl implements EventStageBridge {

    @Override
    public void addSubscriber(Subscriber subscriber, BiConsumer<Subscriber, Class<? extends Event>> consumer) {
        if (subscriber instanceof SingleEventSubscriber) {
            SingleEventSubscriber<?> singleEventSubscriber = (SingleEventSubscriber<?>) subscriber;
            Class<? extends Event> subscribeType = singleEventSubscriber.subscribeType();
            consumer.accept(singleEventSubscriber, subscribeType);
        } else if (subscriber instanceof MultiEventSubscriber) {
            MultiEventSubscriber multiEventSubscriber = (MultiEventSubscriber) subscriber;
            multiEventSubscriber.subscribeTypes().forEach(subscribeType ->
                    consumer.accept(multiEventSubscriber, subscribeType)
            );
        }
    }

    @Override
    public void removeSubscriber(Subscriber subscriber, BiConsumer<Subscriber, Class<? extends Event>> consumer) {
        if (subscriber instanceof SingleEventSubscriber) {
            SingleEventSubscriber<?> singleEventSubscriber = (SingleEventSubscriber<?>) subscriber;
            Class<? extends Event> subscribeType = singleEventSubscriber.subscribeType();
            consumer.accept(singleEventSubscriber, subscribeType);
        } else if (subscriber instanceof MultiEventSubscriber) {
            MultiEventSubscriber multiEventSubscriber = (MultiEventSubscriber) subscriber;
            multiEventSubscriber.subscribeTypes().forEach(subscribeType ->
                    consumer.accept(multiEventSubscriber, subscribeType)
            );
        }
    }

}