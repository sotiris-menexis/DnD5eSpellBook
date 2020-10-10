package com.example.skyknight.dnd5epocketassistant;

import java.util.concurrent.Semaphore;

public class addSpellThread extends Thread{
    private static volatile Semaphore lock = new Semaphore(1);
    private Spell spell;
    public addSpellThread(Spell temp){
        spell = temp;
    }

    @Override
    public void run() {
        super.run();
        try {
            lock.acquire();
            StartScreenActivity.allSpells.add(spell);
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
