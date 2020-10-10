package com.example.skyknight.dnd5epocketassistant;

import java.util.ArrayList;

public class SpellDetails {
    private ArrayList<String> description;
    private ArrayList<String> higher_level;
    private ArrayList<String> components;
    private ArrayList<String> classes;
    private ArrayList<String> subclasses;
    private String page;
    private String range;
    private String material;
    private String ritual;
    private String duration;
    private String concentration;
    private String casting_time;
    private int level;
    private String school;


    public SpellDetails(ArrayList<String> description, ArrayList<String> higher_level, String page, String range, ArrayList<String> components, String material, String ritual, String duration, String concentration, String casting_time, int level, String school, ArrayList<String> classes, ArrayList<String> subclasses) {
        this.description = description;
        this.higher_level = higher_level;
        this.page = page;
        this.range = range;
        this.components = components;
        this.material = material;
        this.ritual = ritual;
        this.duration = duration;
        this.concentration = concentration;
        this.casting_time = casting_time;
        this.level = level;
        this.school = school;
        this.classes = classes;
        this.subclasses = subclasses;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    public ArrayList<String> getHigher_level() {
        return higher_level;
    }

    public void setHigher_level(ArrayList<String> higher_level) {
        this.higher_level = higher_level;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public ArrayList<String> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<String> components) {
        this.components = components;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getRitual() {
        return ritual;
    }

    public void setRitual(String ritual) {
        this.ritual = ritual;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getCasting_time() {
        return casting_time;
    }

    public void setCasting_time(String casting_time) {
        this.casting_time = casting_time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public ArrayList<String> getSubclasses() {
        return subclasses;
    }

    public void setSubclasses(ArrayList<String> subclasses) {
        this.subclasses = subclasses;
    }
}
