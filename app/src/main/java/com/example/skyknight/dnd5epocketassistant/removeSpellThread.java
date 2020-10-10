package com.example.skyknight.dnd5epocketassistant;

import android.util.Log;

import java.util.concurrent.Semaphore;

public class removeSpellThread extends Thread {
    private static volatile Semaphore lock = new Semaphore(1);
    private int pos;

    public removeSpellThread(int pos){
        this.pos = pos;
    }

    @Override
    public void run() {
        super.run();
        if(StartScreenActivity.userSpells.get(pos).getName().contains("΄")){
            StartScreenActivity.userSpells.get(pos).setName(StartScreenActivity.userSpells.get(pos).getName().replace("΄","'"));
        }

        for(int j = 0; j < StartScreenActivity.allSpells.size(); j++){
            Log.d(">>>>>>>>>>",StartScreenActivity.allSpells.get(j).getName());
            if(StartScreenActivity.userSpells.get(pos).getName().equals(StartScreenActivity.allSpells.get(j).getName())){
                try {
                    lock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StartScreenActivity.allSpells.remove(j);
                lock.release();
                break;
            }
        }

    }
}
