package com.gridweaver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Service;

@Service
public class EventLogService {
    private static final int MAX_EVENTS = 200;
    private final ConcurrentLinkedDeque<String> eventLog = new ConcurrentLinkedDeque<>();

    public void addEvent(String message) {
        eventLog.addFirst(message);
        while (eventLog.size() > MAX_EVENTS) {
            eventLog.removeLast();
        }
    }

    public List<String> getRecentEvents() {
        return new ArrayList<>(eventLog);
    }

    public void clear() {
        eventLog.clear();
    }
}
