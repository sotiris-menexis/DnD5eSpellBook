package com.example.skyknight.dnd5epocketassistant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.concurrent.Semaphore;

public class SettingsFragment extends Fragment {
    private static View view;

    private static Dialog popupDialog;

    private static volatile Semaphore lock = new Semaphore(1);

    private static ImageButton logOut_btn;
    private static TextView reset_list_btn;
    private static TextView del_acc_btn;
    private static Button cancel_set_btn;
    public static String prevFrag;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_layout, container, false);

        initializeView();
        setUpListeners();

        return view;
    }

    public void initializeView(){
        logOut_btn = (ImageButton) view.findViewById(R.id.log_out_btn);
        reset_list_btn = (TextView) view.findViewById(R.id.reset_list);
        del_acc_btn = (TextView) view.findViewById(R.id.delete_acc_btn);
        cancel_set_btn = (Button) view.findViewById(R.id.cancel_btn_settings);
    }

    public void setUpListeners(){
        logOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.log_out_anim));
                launchPopUpWindow(2);
            }
        });

        cancel_set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_set_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.cancel_anim));
                if(prevFrag == UtilitiesClass.SpellList){
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            ,new SpellListFragment()
                            ,UtilitiesClass.SpellList).commit();
                }else if(prevFrag == UtilitiesClass.SpellInfo){
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            ,new SpellInfoFragment()
                            ,UtilitiesClass.SpellInfo).commit();
                }else if(prevFrag == UtilitiesClass.MspellBook){
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            ,new SpellBookFragment()
                            ,UtilitiesClass.MspellBook).commit();
                }
            }
        });

        del_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del_acc_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.text_buttons_anim));
                launchPopUpWindow(0);
            }
        });

        reset_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_list_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.text_buttons_anim));
                launchPopUpWindow(1);
            }
        });

    }

    public void launchPopUpWindow(final int identifier) {
        popupDialog = new Dialog(getContext());
        popupDialog.setContentView(R.layout.confirmation_popup);
        Button yes_btn = (Button) popupDialog.findViewById(R.id.popup_yes_btn);
        Button no_btn = (Button) popupDialog.findViewById(R.id.popup_no_btn);


        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(identifier == 0){
                    new delUserFromDB().start();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Account has been successfully deleted",Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(getActivity(),StartScreenActivity.class));
                }else if(identifier == 1){
                    for(int i = 0; i < StartScreenActivity.userSpells.size(); i++) {
                        new addSpellBack(i).start();
                    }
                    new delSpellsFromDB().start();
                    SpellBookFragment.changeMadeFlag = true;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"All spells have been successfully deleted",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(identifier == 2){
                    StartScreenActivity.allSpells.clear();
                    StartScreenActivity.allSpells.trimToSize();
                    getActivity().startActivity(new Intent(getActivity(),StartScreenActivity.class));
                }
                popupDialog.dismiss();
Log.d("TRIGGER","YES BUTTON TRIGGERED!");
            }
        });

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }

    private class addSpellBack extends Thread{
        int position;
        private addSpellBack(int position){
            this.position = position;
        }

        @Override
        public void run() {
            super.run();
            try {
                lock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StartScreenActivity.allSpells.add(StartScreenActivity.userSpells.get(position));
            Collections.sort(StartScreenActivity.allSpells,Spell.spellComparator);
            lock.release();

        }
    }

    private class delSpellsFromDB extends Thread{
        private delSpellsFromDB(){
        }

        @Override
        public void run() {
            super.run();
            DatabaseHelper.getInstance(getContext()).deleteAllSpells(StartScreenActivity.currentUser);
        }
    }

    private class delUserFromDB extends Thread{
        private delUserFromDB(){
        }

        @Override
        public void run() {
            super.run();
            DatabaseHelper.getInstance(getContext()).deleteAccount(StartScreenActivity.currentUser);
        }
    }

}
