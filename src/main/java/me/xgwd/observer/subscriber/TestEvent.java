package me.xgwd.observer.subscriber;

import lombok.Data;
import me.xgwd.observer.bean.Event;

/**
 * @author gbl.huang
 * @date 2025/03/15 13:27
 **/
@Data
public class TestEvent extends Event {
    String name;
}