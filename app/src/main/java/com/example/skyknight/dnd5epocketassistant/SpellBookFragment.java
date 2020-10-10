package com.example.skyknight.dnd5epocketassistant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.Collections;

public class SpellBookFragment extends Fragment  {
    private static View view;
    private static GestureDetector gesture;

    private static ImageView dice_roll_btn;
    
    private static TextView spell_book_title;
    private static TextView add_spell_btn;
    private static ListView mSpellsList;
    private static ProgressBar pb;

    public static String dataAllSpells = "";
    private static boolean noDataReceived = false;

    public static boolean processRunFlag = false;
    public static boolean changeMadeFlag = true;

    private static ImageView set_btn;
    private static TextView user_txt;
    public static String[] noSpellsAddedYet = {"NO SPELL ADDED YET"};

    private int index = SpellsActivity.listIndex;
    private int top = SpellsActivity.listTop;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("View","Created");
        Log.d("noDataReceived","" + noDataReceived);

        view = inflater.inflate(R.layout.m_spell_book_layout, container,false);

        initializeView(view);

        if(!processRunFlag) {
            processRunFlag = true;
            new fetchSpells().execute();
        }

        if(changeMadeFlag) {
            new fetchUserSpells().execute();
            changeMadeFlag = false;
        }else {
            setAdapter();
        }

mSpellsList.setSelectionFromTop(index, top);

        gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        Log.d("Touch","OnGestureListener");
                        float diffY = e2.getY() - e1.getY();
                        final int SWIPE_MIN_DISTANCE = 0;
                        final int SWIPE_THRESHOLD_VELOCITY = 0;

                        if(Math.abs(diffY) > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                            if (diffY > 0 && noDataReceived) {
                                    //swipe down gesture
                                noDataReceived = false;
                                new fetchSpells().execute();
                                while(AsyncTask.Status.RUNNING.equals("RUNNING")){

                                }
                                    new fetchUserSpells().execute();
                                Toast.makeText(getContext(),"Retrying Connection",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Touch","OnTouchListener");
                gesture.onTouchEvent(event);
                return true;
            }
        });
        setUpListeners();
        return view;
    }

    public void initializeView(View view){
        spell_book_title = (TextView) view.findViewById(R.id.spell_book_Title);
        add_spell_btn = (TextView) view.findViewById(R.id.add_spell_btn);
        mSpellsList = (ListView) view.findViewById(R.id.my_spell_list);
        pb = (ProgressBar) view.findViewById(R.id.progressBarSpell);
        dice_roll_btn = (ImageView) view.findViewById(R.id.dice_roll_spell_book);
        set_btn = (ImageView) view.findViewById(R.id.set_btn_spell_book);
        user_txt = (TextView) view.findViewById(R.id.user_txt_spellbook);

        user_txt.setText(" Welcome, " + StartScreenActivity.currentUser.getUsername());



        pb.setVisibility(View.INVISIBLE);
    }

    public void setUpListeners(){
        add_spell_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noDataReceived){
                    return;
                }
                SpellsActivity.listIndex = 0;
                SpellsActivity.listTop = 0;

                getFragmentManager().beginTransaction()
                        .replace(R.id.container_2,
                        new SpellListFragment(),
                        UtilitiesClass.SpellList).commit();
            }
        });
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.set_anim));
                SettingsFragment.prevFrag = UtilitiesClass.MspellBook;
                getFragmentManager().beginTransaction().replace(R.id.container_2
                        ,new SettingsFragment()
                        ,UtilitiesClass.Settings).commit();
            }
        });

        mSpellsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!StartScreenActivity.userSpells.isEmpty()) {
                    SpellInfoFragment.position = position;
                    SpellInfoFragment.prevFrag = 1;
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            , new SpellInfoFragment()
                            , UtilitiesClass.SpellInfo).commit();

                    SpellsActivity.listIndex = mSpellsList.getFirstVisiblePosition();
                    View v = mSpellsList.getChildAt(0);
                    SpellsActivity.listTop = (v == null) ? 0 : (v.getTop() - mSpellsList.getPaddingTop());
                }
            }
        });
        dice_roll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice_roll_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_act_btn_anim));
                DiceRoller.prevFrag = 0;
                getFragmentManager().beginTransaction().replace(R.id.container_2
                        ,new DiceRoller()
                        ,UtilitiesClass.DiceRoller).commit();
            }
        });
    }

    public void showLayout(){
        set_btn.setVisibility(View.VISIBLE);
        dice_roll_btn.setVisibility(View.VISIBLE);
        mSpellsList.setVisibility(View.VISIBLE);
        add_spell_btn.setVisibility(View.VISIBLE);
        spell_book_title.setVisibility(View.VISIBLE);
        user_txt.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
    }

    public void hideLayout(){
        set_btn.setVisibility(View.INVISIBLE);
        dice_roll_btn.setVisibility(View.INVISIBLE);
        add_spell_btn.setVisibility(View.INVISIBLE);
        mSpellsList.setVisibility(View.INVISIBLE);
        spell_book_title.setVisibility(View.INVISIBLE);
        user_txt.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
    }

    public void setAdapter(){
        ListAdapter listAdapter;
        listAdapter = new CustomSpellBookAdapter(getContext());
        mSpellsList.setAdapter(listAdapter);
        mSpellsList.setClickable(true);
        add_spell_btn.setText("+Add More Spells");
    }

    private class fetchSpells extends AsyncTask<Void,Void,Void>{
        int dataCount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideLayout();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                dataAllSpells = "";
                URL url = new URL("http://www.dnd5eapi.co/api/spells/");
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                } catch (IOException e) {
                    Log.d("Conn", e.toString());
                    noDataReceived = true;
                }

                if(!noDataReceived){
                    String line = "";
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    while (line != null) {
                        line = bufferedReader.readLine();
                        dataAllSpells = dataAllSpells + line;
                    }

                        JSONObject SpellsJO = new JSONObject(dataAllSpells);

                        dataCount = (int) SpellsJO.get("count");
                        JSONArray SpellsJA = SpellsJO.getJSONArray("results");

                        for (int i = 0; i < SpellsJA.length(); i++) {
                            JSONObject JAO = (JSONObject) SpellsJA.get(i);

                            Spell temp = new Spell();
                            temp.setName(JAO.getString("name"));
                            temp.setUrl(JAO.getString("url"));
                            new addSpellThread(temp).start();
                            Log.d(">>>>>>>>>>", temp.getUrl());
                            //StartScreenActivity.allSpells.add(temp);
                        }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(noDataReceived){
                Toast.makeText(getContext()
                        ,"No connection established.Please check your internet connection and swipe down to update."
                        ,Toast.LENGTH_LONG).show();
            }
        }
    }

    private class fetchUserSpells extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideLayout();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StartScreenActivity.userSpells.clear();
            StartScreenActivity.userSpells.trimToSize();
            StartScreenActivity.userSpells = DatabaseHelper.getInstance(getContext()).getUserSpells();
Log.d("Tag","Last Index " + StartScreenActivity.userSpells.size());

            for(int i = 0; i < StartScreenActivity.userSpells.size(); i++) {
                new removeSpellThread(i).start();
                //if(StartScreenActivity.userSpells.get(i).getName().contains("΄")){
                  //  StartScreenActivity.userSpells.get(i).setName(StartScreenActivity.userSpells.get(i).getName().replace("΄","'"));
                //}
               /* for(int j = 0; j < StartScreenActivity.allSpells.size(); j++){
Log.d(">>>>>>>>>>",StartScreenActivity.allSpells.get(j).getName());
                    if(StartScreenActivity.userSpells.get(i).getName().equals(StartScreenActivity.allSpells.get(j).getName())){
                        StartScreenActivity.allSpells.remove(j);
                        break;
                    }
                }*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListAdapter listAdapter;
            if(StartScreenActivity.userSpells.isEmpty()){
                listAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1, noSpellsAddedYet);
                mSpellsList.setAdapter(listAdapter);
                mSpellsList.setClickable(false);
                add_spell_btn.setText("+Add Spells");
            }else {
                Collections.sort(StartScreenActivity.userSpells,Spell.spellComparator);
                listAdapter = new CustomSpellBookAdapter(getContext());
                mSpellsList.setAdapter(listAdapter);
                mSpellsList.setClickable(true);
                add_spell_btn.setText("+Add More Spells");
            }
            showLayout();
        }
    }
}
