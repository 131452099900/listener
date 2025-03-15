package me.xgwd.observer.bean;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gbl.huang
 * @date 2025/03/13 11:14
 **/
@Data
public class Event {
    private static final AtomicLong AUTO_INCREMENT = new AtomicLong(1);
    private Long id = AUTO_INCREMENT.getAndIncrement();
}