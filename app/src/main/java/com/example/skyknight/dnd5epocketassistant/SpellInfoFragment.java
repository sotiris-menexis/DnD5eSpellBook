package com.example.skyknight.dnd5epocketassistant;

import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

public class SpellInfoFragment extends Fragment {
    private static View view;
    public static int position;
    public static int prevFrag;

    private static ImageView dice_roll_btn;

    private static TextView spell_info_title;
    private static TextView desc_txt;
    private static TextView higherlvl_txt;
    private static TextView comp_txt;
    private static TextView class_txt;
    private static TextView subclass_txt;
    private static TextView page_txt;
    private static TextView range_txt;
    private static TextView mat_txt;
    private static TextView ritual_txt;
    private static TextView dur_txt;
    private static TextView conc_txt;
    private static TextView cast_time_txt;
    private static TextView lvl_txt;
    private static TextView school_txt;
    private static Button cancel_btn;
    private static TextView add_spell_btn;
    private static ImageView set_btn;
    private static TextView user_txt;
    private static ProgressBar pb;

    private static TextView desc_title;
    private static TextView higherlvl_title;
    private static TextView comp_title;
    private static TextView class_title;
    private static TextView subclass_title;
    private static TextView page_title;
    private static TextView range_title;
    private static TextView mat_title;
    private static TextView ritual_title;
    private static TextView dur_title;
    private static TextView conc_title;
    private static TextView cast_time_title;
    private static TextView lvl_title;
    private static TextView school_title;


    //Spell details information variables.
    private String fullName = "";
    private String fullDescription = "";
    private String fullHigherLvl = "";
    private String fullComponents = "";
    private String fullClasses = "";
    private String fullSubclasses = "";
    private String fullPage = "";
    private String fullRange = "";
    private String fullMaterial = "";
    private String fullRitual = "";
    private String fullDuration = "";
    private String fullConcentration = "";
    private String fullCasting_time = "";
    private String fullLevel = "";
    private String fullSchool = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.m_spell_info_layout,container,false);

        initializeView();
        new fetchSpellDetails().execute();
        setUpListeners();
        return view;
    }

    private class fetchSpellDetails extends AsyncTask<Void,Void,Void>{
        String data = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideLayout();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetchSpellDetails();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setText();
            if(prevFrag == 0) { //If coming from spellbook.
                showLayout();
            }else if(prevFrag == 1){ //If coming from spell list (want to add new spell).
                showLayout();
                add_spell_btn.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void initializeView(){
        spell_info_title = (TextView) view.findViewById(R.id.spell_Info_Title);

        desc_txt = (TextView) view.findViewById(R.id.spell_desc_txt);
        higherlvl_txt = (TextView) view.findViewById(R.id.spell_higher_lvl_txt);
        comp_txt = (TextView) view.findViewById(R.id.spell_comp_txt);
        class_txt = (TextView) view.findViewById(R.id.spell_classes_txt);
        subclass_txt = (TextView) view.findViewById(R.id.spell_subclasses_txt);
        page_txt = (TextView) view.findViewById(R.id.spell_page_txt);
        range_txt = (TextView) view.findViewById(R.id.spell_range_txt);
        mat_txt = (TextView) view.findViewById(R.id.spell_materials_txt);
        ritual_txt = (TextView) view.findViewById(R.id.spell_ritual_txt);
        dur_txt = (TextView) view.findViewById(R.id.spell_duration_txt);
        conc_txt = (TextView) view.findViewById(R.id.spell_concentration_txt);
        cast_time_txt = (TextView) view.findViewById(R.id.spell_casting_time_txt);
        lvl_txt = (TextView) view.findViewById(R.id.spell_lvl_txt);
        school_txt = (TextView) view.findViewById(R.id.spell_school_txt);

        desc_title = (TextView) view.findViewById(R.id.spell_desc_title);
        higherlvl_title = (TextView) view.findViewById(R.id.spell_higher_lvl_title);
        comp_title = (TextView) view.findViewById(R.id.spell_comp_title);
        class_title = (TextView) view.findViewById(R.id.spell_classes_title);
        subclass_title = (TextView) view.findViewById(R.id.spell_subclasses_title);
        page_title= (TextView) view.findViewById(R.id.spell_page_title);
        range_title = (TextView) view.findViewById(R.id.spell_range_title);
        mat_title = (TextView) view.findViewById(R.id.spell_materials_title);
        ritual_title = (TextView) view.findViewById(R.id.spell_ritual_title);
        dur_title = (TextView) view.findViewById(R.id.spell_duration_title);
        conc_title = (TextView) view.findViewById(R.id.spell_concentration_title);
        cast_time_title = (TextView) view.findViewById(R.id.spell_casting_time_title);
        lvl_title = (TextView) view.findViewById(R.id.spell_lvl_title);
        school_title = (TextView) view.findViewById(R.id.spell_school_title);

        dice_roll_btn = (ImageView) view.findViewById(R.id.dice_roll_spell_info);
        cancel_btn = (Button) view.findViewById(R.id.spell_info_done);
        add_spell_btn = (TextView) view.findViewById(R.id.add_spell_btn);
        set_btn = (ImageView) view.findViewById(R.id.set_btn_spell_info);
        user_txt = (TextView) view.findViewById(R.id.user_txt_spellinfo);
        pb = (ProgressBar) view.findViewById(R.id.progressBar_spellinfo);

        pb.setVisibility(View.INVISIBLE);

        user_txt.setText(" Welcome, " + StartScreenActivity.currentUser.getUsername());
    }

    public void setUpListeners(){
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.cancel_anim));
                if(prevFrag == 0) {
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            , new SpellListFragment()
                            , UtilitiesClass.SpellList).commit();
                }else if(prevFrag == 1) {
                    getFragmentManager().beginTransaction().replace(R.id.container_2
                            , new SpellBookFragment()
                            , UtilitiesClass.MspellBook).commit();
                }
            }
        });

        add_spell_btn.setOnClickListener(new View.OnClickListener() {
            boolean pressedOnce = false;
            @Override
            public void onClick(View v) {
                if(pressedOnce == false) {
                    pressedOnce = true;
                    add_spell_btn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.text_buttons_anim));
                    add_spell_btn.setVisibility(View.INVISIBLE);
                    DatabaseHelper.getInstance(getContext()).insertSpell(StartScreenActivity.allSpells.get(position));
                    SpellBookFragment.changeMadeFlag = true;
                    Toast.makeText(getContext(), "Spell successfully added", Toast.LENGTH_SHORT).show();
                    StartScreenActivity.allSpells.remove(position);
                    Collections.sort(StartScreenActivity.allSpells, Spell.spellComparator);
                }
            }
        });

        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.set_anim));
                SettingsFragment.prevFrag = UtilitiesClass.SpellInfo;
                getFragmentManager().beginTransaction().replace(R.id.container_2
                        ,new SettingsFragment()
                        ,UtilitiesClass.Settings).commit();
            }
        });

        dice_roll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice_roll_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.dice_act_btn_anim));
                DiceRoller.prevFrag = 2; // Return to this particular fragment if Dice Roller is used.
                getFragmentManager().beginTransaction().replace(R.id.container_2
                        ,new DiceRoller()
                        ,UtilitiesClass.DiceRoller).commit();
            }
        });
    }

    private void setText() {
        if(fullName.length() > 0 || !fullName.trim().equals("")){
            spell_info_title.setText(fullName);
        }

        if(fullDescription.length() > 0 || !fullDescription.trim().equals("")){
            desc_txt.setText(fullDescription);
        }
        if(fullHigherLvl.length() > 0 || !fullHigherLvl.trim().equals("")){
            higherlvl_txt.setText(fullHigherLvl);
        }
        if(fullComponents.length() > 0 || !fullComponents.trim().equals("")){
            comp_txt.setText(fullComponents);
        }
        if(fullClasses.length() > 0 || !fullClasses.trim().equals("")){
            class_txt.setText(fullClasses);
        }
        if(fullSubclasses.length() > 0 || !fullSubclasses.trim().equals("")){
            subclass_txt.setText(fullSubclasses);
        }
        if(fullPage.length() > 0 || !fullPage.trim().equals("")){
            page_txt.setText(fullPage);
        }
        if(fullRange.length() > 0 || !fullRange.trim().equals("")){
            range_txt.setText(fullRange);
        }
        if(fullMaterial.length() > 0 || !fullMaterial.trim().equals("")){
            mat_txt.setText(fullMaterial);
        }
        if(fullRitual.length() > 0 || !fullRitual.trim().equals("")){
            ritual_txt.setText(fullRitual);
        }
        if(fullDuration.length() > 0 || !fullDuration.trim().equals("")){
            dur_txt.setText(fullDuration);
        }
        if(fullConcentration.length() > 0 || !fullConcentration.trim().equals("")){
            conc_txt.setText(fullConcentration);
        }
        if(fullCasting_time.length() > 0 || !fullCasting_time.trim().equals("")){
            cast_time_txt.setText(fullCasting_time);
        }
        if(fullLevel.length() > 0 || !fullLevel.trim().equals("")){
            lvl_txt.setText(fullLevel);
        }
        if(fullSchool.length() > 0 || !fullSchool.trim().equals("")){
            school_txt.setText(fullSchool);
        }
    }

    public void showLayout(){
        spell_info_title.setVisibility(View.VISIBLE);
        add_spell_btn.setVisibility(View.VISIBLE);
        cancel_btn.setVisibility(View.VISIBLE);
        set_btn.setVisibility(View.VISIBLE);
        dice_roll_btn.setVisibility(View.VISIBLE);

        desc_title.setVisibility(View.VISIBLE);
        higherlvl_title.setVisibility(View.VISIBLE);
        comp_title.setVisibility(View.VISIBLE);
        class_title.setVisibility(View.VISIBLE);
        subclass_title.setVisibility(View.VISIBLE);
        page_title.setVisibility(View.VISIBLE);
        range_title.setVisibility(View.VISIBLE);
        mat_title.setVisibility(View.VISIBLE);
        ritual_title.setVisibility(View.VISIBLE);
        dur_title.setVisibility(View.VISIBLE);
        conc_title.setVisibility(View.VISIBLE);
        cast_time_title.setVisibility(View.VISIBLE);
        lvl_title.setVisibility(View.VISIBLE);
        school_title.setVisibility(View.VISIBLE);

        user_txt.setVisibility(View.VISIBLE);
        desc_txt.setVisibility(View.VISIBLE);
        higherlvl_txt.setVisibility(View.VISIBLE);
        comp_txt.setVisibility(View.VISIBLE);
        class_txt.setVisibility(View.VISIBLE);
        subclass_txt.setVisibility(View.VISIBLE);
        page_txt.setVisibility(View.VISIBLE);
        range_txt.setVisibility(View.VISIBLE);
        mat_txt.setVisibility(View.VISIBLE);
        ritual_txt.setVisibility(View.VISIBLE);
        dur_txt.setVisibility(View.VISIBLE);
        conc_txt.setVisibility(View.VISIBLE);
        cast_time_txt.setVisibility(View.VISIBLE);
        lvl_txt.setVisibility(View.VISIBLE);
        school_txt.setVisibility(View.VISIBLE);

        pb.setVisibility(View.INVISIBLE);
    }

    public void hideLayout(){
        spell_info_title.setVisibility(View.INVISIBLE);
        add_spell_btn.setVisibility(View.INVISIBLE);
        cancel_btn.setVisibility(View.INVISIBLE);
        set_btn.setVisibility(View.INVISIBLE);
        dice_roll_btn.setVisibility(View.INVISIBLE);

        desc_title.setVisibility(View.INVISIBLE);
        higherlvl_title.setVisibility(View.INVISIBLE);
        comp_title.setVisibility(View.INVISIBLE);
        class_title.setVisibility(View.INVISIBLE);
        subclass_title.setVisibility(View.INVISIBLE);
        page_title.setVisibility(View.INVISIBLE);
        range_title.setVisibility(View.INVISIBLE);
        mat_title.setVisibility(View.INVISIBLE);
        ritual_title.setVisibility(View.INVISIBLE);
        dur_title.setVisibility(View.INVISIBLE);
        conc_title.setVisibility(View.INVISIBLE);
        cast_time_title.setVisibility(View.INVISIBLE);
        lvl_title.setVisibility(View.INVISIBLE);
        school_title.setVisibility(View.INVISIBLE);

        user_txt.setVisibility(View.INVISIBLE);
        desc_txt.setVisibility(View.INVISIBLE);
        higherlvl_txt.setVisibility(View.INVISIBLE);
        comp_txt.setVisibility(View.INVISIBLE);
        class_txt.setVisibility(View.INVISIBLE);
        subclass_txt.setVisibility(View.INVISIBLE);
        page_txt.setVisibility(View.INVISIBLE);
        range_txt.setVisibility(View.INVISIBLE);
        mat_txt.setVisibility(View.INVISIBLE);
        ritual_txt.setVisibility(View.INVISIBLE);
        dur_txt.setVisibility(View.INVISIBLE);
        conc_txt.setVisibility(View.INVISIBLE);
        cast_time_txt.setVisibility(View.INVISIBLE);
        lvl_txt.setVisibility(View.INVISIBLE);
        school_txt.setVisibility(View.INVISIBLE);

        pb.setVisibility(View.VISIBLE);
    }


    public void fetchSpellDetails(){
        String data = "";
        Spell temp = new Spell();
        if(prevFrag == 0){
            temp = StartScreenActivity.allSpells.get(position);
        }
        else if(prevFrag == 1){
            temp = StartScreenActivity.userSpells.get(position);
        }

        fullName = temp.getName();
        try {
            temp.initializeDetails();
            URL url = new URL(temp.getUrl());
            Log.d("-----URL", url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject JO = new JSONObject(data);
            JSONArray JA;

            if (JO.has("desc")) {
                JA = JO.getJSONArray("desc");
                for (int j = 0; j < JA.length(); j++) {
                    temp.getDetails().getDescription().add((String) JA.get(j));
                    Log.d("-----DESCRIPTION", "-----------------------------------------------------ADDED!");
                }
            }

            if (JO.has("higher_level")) {
                JA = JO.getJSONArray("higher_level");
                for (int j = 0; j < JA.length(); j++) {
                    temp.getDetails().getHigher_level().add((String) JA.get(j));
                }
            }

            if (JO.has("page"))
                temp.getDetails().setPage((String) JO.get("page"));

            if (JO.has("range"))
                temp.getDetails().setRange((String) JO.get("range"));

            if (JO.has("components")) {
                JA = JO.getJSONArray("components");
                for (int j = 0; j < JA.length(); j++) {
                    temp.getDetails().getComponents().add((String) JA.get(j));
                }
            }

            if (JO.has("ritual"))
                temp.getDetails().setRitual((String) JO.get("ritual"));

            if (JO.has("duration"))
                temp.getDetails().setDuration((String) JO.get("duration"));

            if (JO.has("concentration"))
                temp.getDetails().setConcentration((String) JO.get("concentration"));

            if (JO.has("casting_time"))
                temp.getDetails().setCasting_time((String) JO.get("casting_time"));

            if (JO.has("level"))
                temp.getDetails().setLevel((int) JO.get("level"));

            if (JO.has("school")) {
                JSONObject JOO = (JSONObject) JO.get("school");
                if (JOO.has("name"))
                    temp.getDetails().setSchool((String) JOO.get("name"));
            }

            if (JO.has("classes")) {
                JSONArray JOA = JO.getJSONArray("classes");
                for (int j = 0; j < JOA.length(); j++) {
                    JSONObject JOAO = (JSONObject) JOA.get(j);
                    temp.getDetails().getClasses().add((String) JOAO.get("name"));
                }
            }

            if (JO.has("subclasses")) {
                JSONArray JOA = JO.getJSONArray("subclasses");
                for (int j = 0; j < JOA.length(); j++) {
                    JSONObject JOAO = (JSONObject) JOA.get(j);
                    temp.getDetails().getSubclasses().add((String) JOAO.get("name"));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!temp.getDetails().getDescription().isEmpty()) {
            for (int i = 0; i < temp.getDetails().getDescription().size(); i++) {
                fullDescription = fullDescription +
                        temp.getDetails().getDescription().get(i) + "\n";
            }
        }
        if (!temp.getDetails().getHigher_level().isEmpty()) {
            for (int i = 0; i < temp.getDetails().getHigher_level().size(); i++) {
                fullHigherLvl = fullHigherLvl +
                        temp.getDetails().getHigher_level().get(i) + "\n";
            }
        }
        if (!temp.getDetails().getComponents().isEmpty()) {
            for (int i = 0; i < temp.getDetails().getComponents().size(); i++) {
                fullComponents = fullComponents +
                        temp.getDetails().getComponents().get(i) + "\n";
            }
        }
        if (!temp.getDetails().getClasses().isEmpty()) {
            for (int i = 0; i < temp.getDetails().getClasses().size(); i++) {
                fullClasses = fullClasses +
                        temp.getDetails().getClasses().get(i) + "\n";
            }
        }
        if (!temp.getDetails().getSubclasses().isEmpty()) {
            for (int i = 0; i < temp.getDetails().getSubclasses().size(); i++) {
                fullSubclasses = fullSubclasses +
                        temp.getDetails().getSubclasses().get(i) + "\n";
            }
        }
        if (temp.getDetails().getPage().length() > 0
                || !temp.getDetails().getPage().equals("")) {
            fullPage = temp.getDetails().getPage();
        }
        if (temp.getDetails().getRange().length() > 0
                || !temp.getDetails().getRange().equals("")) {
            fullRange = temp.getDetails().getRange();
        }
        if (temp.getDetails().getMaterial().length() > 0
                || !temp.getDetails().getMaterial().equals("")) {
            fullMaterial = temp.getDetails().getMaterial();
        }
        if (temp.getDetails().getRitual().length() > 0
                || !temp.getDetails().getRitual().equals("")) {
            fullRitual = temp.getDetails().getRitual();
        }
        if (temp.getDetails().getDuration().length() > 0
                || !temp.getDetails().getDuration().equals("")) {
            fullDuration = temp.getDetails().getDuration();
        }
        if (temp.getDetails().getConcentration().length() > 0
                || !temp.getDetails().getConcentration().equals("")) {
            fullConcentration = temp.getDetails().getConcentration();
        }
        if (temp.getDetails().getCasting_time().length() > 0
                || !temp.getDetails().getCasting_time().equals("")) {
            fullCasting_time = temp.getDetails().getCasting_time();
        }
        if (String.valueOf(temp.getDetails().getLevel()).length() > 0
                || !String.valueOf(temp.getDetails().getLevel()).equals("")) {
            fullLevel = String.valueOf(temp.getDetails().getLevel());
        }
        if (temp.getDetails().getSchool().length() > 0
                || !temp.getDetails().getSchool().equals("")) {
            fullSchool = temp.getDetails().getSchool();
        }
    }


}
