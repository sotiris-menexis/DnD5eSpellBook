package com.example.skyknight.dnd5epocketassistant;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class Spell {
    private String name;
    private String url;
    private SpellDetails details;


    public Spell() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SpellDetails getDetails() {
        return details;
    }

    public void setDetails(SpellDetails details) {
        this.details = details;
    }

    public void initializeDetails(){ this.details = new SpellDetails(new ArrayList<String>(),
            new ArrayList<String>(),
            "Not available.",
            "Not available.",
            new ArrayList<String>(),
            "Not available.",
            "Not available.",
            "Not available.",
            "Not available.",
            "Not available.",
            -1,
            "Not available.",
            new ArrayList<String>(),
            new ArrayList<String>());
    }

    public static Comparator<Spell> spellComparator = new Comparator<Spell>() {
        public int compare(Spell s1, Spell s2) {
            String SpellName1 = s1.getName().toUpperCase();
            String SpellName2 = s2.getName().toUpperCase();

            //ascending order
            return SpellName1.compareTo(SpellName2);

            //descending order
            //return SpellName2.compareTo(SpellName1);
        }};

}