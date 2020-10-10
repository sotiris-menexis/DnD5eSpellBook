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
import java.util.concurrent.TimeUnit;

public class CustomSpellListAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private Animation anim;
    private static long mLastClickTime = 0;
    private static volatile Semaphore lock = new Semaphore(1);

    public CustomSpellListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return StartScreenActivity.allSpells.size();
    }

    @Override
    public Object getItem(int pos) {
        return StartScreenActivity.allSpells.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return StartScreenActivity.allSpells.indexOf(pos);
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spell_list_adapter, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        if(StartScreenActivity.allSpells.get(position).getName().contains("΄")){
            StartScreenActivity.allSpells.get(position).
                    setName(StartScreenActivity.allSpells.get(position).getName().replace("΄","'"));
        }
        listItemText.setText(StartScreenActivity.allSpells.get(position).getName());
        //Handle buttons and add onClickListeners
        final Button addBtn = (Button) view.findViewById(R.id.add_btn_adapter);
        addBtn.setFocusable(false);
        anim = AnimationUtils.loadAnimation(context, R.anim.add_scale_anim);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 700) {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();
                Spell temp = StartScreenActivity.allSpells.get(position);
                StartScreenActivity.allSpells.remove(position);
                Collections.sort(StartScreenActivity.allSpells, Spell.spellComparator);
                notifyDataSetChanged();
                addBtn.startAnimation(anim);
                Log.d("POSITION","Adding item with position: " + position + ", " + StartScreenActivity.allSpells.get(position).getName());
                new addToDb(temp).start();
                StartScreenActivity.userSpells.add(temp);
                Collections.sort(StartScreenActivity.userSpells,Spell.spellComparator);
                Log.d("POSITION","Added item with position: " + position + ", " + StartScreenActivity.allSpells.get(position).getName());


                SpellBookFragment.changeMadeFlag = true;
                Toast.makeText(context, "Spell successfully added", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private class addToDb extends Thread{
        private Spell temp;
        private addToDb(Spell temp){
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
            DatabaseHelper.getInstance(context).insertSpell(temp);
            lock.release();
        }
    }

}
