package com.example.skyknight.dnd5epocketassistant;

import java.io.BufferedReader;
import java.util.concurrent.Semaphore;

public class JsonReadThread extends Thread {
    public static volatile Semaphore lock = new Semaphore(1);
    private static BufferedReader bf;

    public JsonReadThread(BufferedReader bf){
        this.bf = bf;
    }

    @Override
    public void run() {
        super.run();
        try {
            lock.acquire();

            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
