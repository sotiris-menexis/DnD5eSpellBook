package com.example.skyknight.dnd5epocketassistant;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class DiceRoller extends Fragment {

    private static Button done_btn;

    public static int prevFrag;

    private static View view;

    private static ImageView d20_btn;
    private static ImageView d4_btn;
    private static ImageView d6_btn;
    private static ImageView d8_btn;
    private static ImageView d10_btn;
    private static ImageView d12_btn;
    private static ImageView d100_btn;

    private static TextView d20_txt;
    private static TextView d4_txt;
    private static TextView d6_txt;
    private static TextView d8_txt;
    private static TextView d10_txt;
    private static TextView d12_txt;
    private static TextView d100_txt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dice_roller_layout,container,false);

        initializeView();
        setUpListeners();

        return view;
    }

    public void setUpListeners(){
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.cancel_anim));

                if(prevFrag == 0){ // If coming from Spellbook.
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            ,new SpellBookFragment(),UtilitiesClass.MspellBook).commit();
                }else if(prevFrag == 1){ // If coming from SpellList.
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            ,new SpellListFragment(),UtilitiesClass.SpellList).commit();
                }else if(prevFrag == 2){ // If coming from Spell Details.
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            ,new SpellInfoFragment(),UtilitiesClass.SpellInfo).commit();
                }

                d20_txt.setText("");
                d4_txt.setText("");
                d6_txt.setText("");
                d8_txt.setText("");
                d10_txt.setText("");
                d12_txt.setText("");
                d100_txt.setText("");
            }
        });

        d20_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d20_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_roll));
                d20_txt.setText(String.valueOf(rollD20()));
            }
        });
        d4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d4_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_roll));
                d4_txt.setText(String.valueOf(rollD4()));
            }
        });
        d6_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d6_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_roll));
                d6_txt.setText(String.valueOf(rollD6()));
            }
        });
        d8_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d8_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_roll));
                d8_txt.setText(String.valueOf(rollD8()));
            }
        });
        d10_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d10_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_roll));
                d10_txt.setText(String.valueOf(rollD10()));
            }
        });
        d12_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d12_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_roll));
                d12_txt.setText(String.valueOf(rollD12()));
            }
        });
        d100_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d100_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_roll));
                String result = String.valueOf(rollD100());
                if(result.equals("0")){
                    result = "00";
                }
                d100_txt.setText(result);
            }
        });
    }

    public void initializeView(){
        done_btn = (Button) view.findViewById(R.id.done_btn_roll);

        d20_btn = (ImageView) view.findViewById(R.id.d20_btn);
        d4_btn = (ImageView) view.findViewById(R.id.d4_btn);
        d6_btn = (ImageView) view.findViewById(R.id.d6_btn);
        d8_btn = (ImageView) view.findViewById(R.id.d8_btn);
        d10_btn = (ImageView) view.findViewById(R.id.d10_btn);
        d12_btn = (ImageView) view.findViewById(R.id.d12_btn);
        d100_btn = (ImageView) view.findViewById(R.id.d100_btn);


        d20_txt = (TextView) view.findViewById(R.id.d20);
        d4_txt = (TextView) view.findViewById(R.id.d4);
        d6_txt = (TextView) view.findViewById(R.id.d6);
        d8_txt = (TextView) view.findViewById(R.id.d8);
        d10_txt = (TextView) view.findViewById(R.id.d10);
        d12_txt = (TextView) view.findViewById(R.id.d12);
        d100_txt = (TextView) view.findViewById(R.id.d100);
    }

    public int rollD20(){
        Random rand = new Random();
        int rand_num = rand.nextInt(19)+1;
        return rand_num;
    }

    public int rollD4(){
        Random rand = new Random();
        int rand_num = rand.nextInt(4)+1;
        return rand_num;
    }

    public int rollD6(){
        Random rand = new Random();
        int rand_num = rand.nextInt(6)+1;
        return rand_num;
    }

    public int rollD8(){
        Random rand = new Random();
        int rand_num = rand.nextInt(8)+1;
        return rand_num;
    }

    public int rollD10(){
        Random rand = new Random();
        int rand_num = rand.nextInt(10);
        return rand_num;
    }

    public int rollD12(){
        Random rand = new Random();
        int rand_num = rand.nextInt(12)+1;
        return rand_num;
    }

    public int rollD100(){
        Random rand = new Random();
        int rand_num = rand.nextInt(10)*10;
        return rand_num;
    }

}
