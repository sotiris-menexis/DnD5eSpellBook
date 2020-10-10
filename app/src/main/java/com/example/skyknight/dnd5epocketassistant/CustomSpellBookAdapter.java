package com.example.skyknight.dnd5epocketassistant;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.concurrent.Semaphore;

public class CustomSpellBookAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private Animation anim;
    private static long mLastClickTime = 0;
    private static volatile Semaphore lock = new Semaphore(1);

    public CustomSpellBookAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return StartScreenActivity.userSpells.size();
    }

    @Override
    public Object getItem(int pos) {
        return StartScreenActivity.userSpells.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return StartScreenActivity.userSpells.indexOf(pos);
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spell_book_adapter, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(StartScreenActivity.userSpells.get(position).getName());


        //Handle buttons and add onClickListeners
        final Button delBtn = (Button)view.findViewById(R.id.del_btn_adapter);
        delBtn.setFocusable(false);
        anim = AnimationUtils.loadAnimation(context,R.anim.add_scale_anim);
        delBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 700) {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();

                delBtn.startAnimation(anim);
Log.d("DELETE", "Position: " + position);
                Spell temp = StartScreenActivity.userSpells.get(position);
                StartScreenActivity.userSpells.remove(position);
                StartScreenActivity.userSpells.trimToSize();
                Collections.sort(StartScreenActivity.userSpells,Spell.spellComparator);
                notifyDataSetChanged();
                new deleteFromDb(temp).start();
                Toast.makeText(context, "Spell successfully deleted", Toast.LENGTH_SHORT);
                StartScreenActivity.allSpells.add(temp);
                Collections.sort(StartScreenActivity.allSpells,Spell.spellComparator);
            }
        });

        return view;
    }

    private class deleteFromDb extends Thread {
        private Spell temp;

        private deleteFromDb(Spell temp){
            this.temp = temp;
        }

        @Override
        public void run() {
            super.run();
            try {
                lock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DatabaseHelper.getInstance(context).deleteSpell(StartScreenActivity.currentUser,temp);
            lock.release();
        }
    }

}