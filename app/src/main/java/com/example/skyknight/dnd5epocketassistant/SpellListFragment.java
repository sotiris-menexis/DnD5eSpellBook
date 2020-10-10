package com.example.skyknight.dnd5epocketassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SpellListFragment extends Fragment {
    private static View view;

    private static ImageView dice_roll_btn;

    private static ListView all_spell_list;
    private static Button cancel_btn;
    private static ImageView set_btn;
    private static TextView user_txt;

    private int listIndex = SpellsActivity.listIndex;
    private int listTop = SpellsActivity.listTop;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.m_spell_list_layout,container,false);

        initializeView();
        setUpListeners();
        return view;
    }

    public void initializeView(){
        all_spell_list = (ListView) view.findViewById(R.id.all_spell_list);
        cancel_btn = (Button) view.findViewById(R.id.spell_info_done);
        dice_roll_btn = (ImageView) view.findViewById(R.id.dice_roll_spell_list);
        set_btn = (ImageView) view.findViewById(R.id.set_btn_spell_list);
        user_txt = (TextView) view.findViewById(R.id.user_txt_spelllist);

        user_txt.setText(" Welcome, " + StartScreenActivity.currentUser.getUsername());

        ListAdapter listAdapter = new CustomSpellListAdapter(getContext());
        all_spell_list.setAdapter(listAdapter);



//all_spell_list.setSelection(SpellInfoFragment.position);
        all_spell_list.setSelectionFromTop(listIndex, listTop);

    }

    public void setUpListeners(){
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.cancel_anim));

                SpellsActivity.listIndex = 0;
                SpellsActivity.listTop = 0;

                getFragmentManager().beginTransaction().replace(R.id.container_2,new SpellBookFragment(),UtilitiesClass.MspellBook).commit();
            }
        });

        all_spell_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
Log.d("------------- LIST ITEM","CLICKED!!!!!!!!!!!!");
                    SpellInfoFragment.position = position;
                    SpellInfoFragment.prevFrag = 0;
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            ,new SpellInfoFragment()
                            ,UtilitiesClass.SpellInfo).commit();
                SpellsActivity.listIndex = all_spell_list.getFirstVisiblePosition();
                View v = all_spell_list.getChildAt(0);
                SpellsActivity.listTop = (v == null) ? 0 : (v.getTop() - all_spell_list.getPaddingTop());
            }
        });


        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.set_anim));
                SettingsFragment.prevFrag = UtilitiesClass.SpellList;
                getFragmentManager().beginTransaction().replace(R.id.container_2
                        ,new SettingsFragment()
                        ,UtilitiesClass.Settings).commit();
            }
        });

        dice_roll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice_roll_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_act_btn_anim));
                DiceRoller.prevFrag = 1;
                getFragmentManager().beginTransaction().replace(R.id.container_2
                        ,new DiceRoller()
                        ,UtilitiesClass.DiceRoller).commit();
            }
        });
    }

}
