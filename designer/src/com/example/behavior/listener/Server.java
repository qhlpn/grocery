package com.example.behavior.listener;

import java.util.Arrays;
import java.util.List;

public class Server implements Listener {

    private List<Listener> listeners = Arrays.asList(new ListenerServiceA(), new ListenerServiceB());

    public void doServer() {
        System.out.println("finish server ...");
        trigger();
    }

    @Override
    public void trigger() {
        if (listeners != null) {
            for (Listener listener : listeners) {
                listener.trigger();
            }
        }
    }
}
