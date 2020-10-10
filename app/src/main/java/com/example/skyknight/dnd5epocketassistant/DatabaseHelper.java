package com.example.skyknight.dnd5epocketassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME_1 = "UserLogInInfo";
    private static final String TABLE_NAME_2 = "UserSpells";
    //                          COL_Num_ArrayNum
    private static final String COL_1_1 = "ID";
    private static final String COL_2_1 = "EMAIL";
    private static final String COL_3_1 = "USERNAME";
    private static final String COL_4_1 = "PASSWORD";
    private static final String COL_1_2 = "ID";
    private static final String COL_2_2 = "USERNAME";
    private static final String COL_3_2 = "SPELLNAME";
    private static final String COL_4_2 = "URL";

    public static final String EnKey = "ThisIsASecureDatabase";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
Log.d("Tag", "Database created");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_1 + " (" + COL_1_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2_1 + " TEXT, " + COL_3_1 + " TEXT, " + COL_4_1 + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + " (" + COL_1_2 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2_2 + " TEXT, " + COL_3_2 + " TEXT, " + COL_4_2 + " TEXT)");
Log.d("Tag", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }


    static public synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    public boolean TableIsEmpty(){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);
        String count = "SELECT count(*) FROM " + TABLE_NAME_1;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        mcursor.close();
        db.close();
        if(icount>0){
            return false;
        }else {
            return true;
        }
    }

    public boolean insertData(User u){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);
        ContentValues cv = new ContentValues();
        cv.put(COL_2_1,u.getEmail());
        cv.put(COL_3_1,u.getUsername());
        cv.put(COL_4_1,u.getPassword());
        long result = db.insert(TABLE_NAME_1, null, cv);
        db.close();
        if(result == -1){
            return false;
        }else {
            return true;
        }

    }


    public ArrayList<User> getUserLogInInfo(){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);

        ArrayList<User> users = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME_1 + " ORDER BY ID DESC",null);

        if(cursor.moveToFirst()) {
            do{
                User temp = new User();
                temp.setId(cursor.getInt(0));
                temp.setEmail(cursor.getString(1));
                temp.setUsername(cursor.getString(2));
                temp.setPassword(cursor.getString(3));
                users.add(temp);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public ArrayList<Spell> getUserSpells(){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);

        ArrayList<Spell> spells = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2 + " WHERE USERNAME='"
                + StartScreenActivity.currentUser.getUsername() + "' ORDER BY ID DESC", null);

        if(cursor.moveToFirst()){
            do{
                Spell temp = new Spell();
                temp.setName(cursor.getString(2));
                temp.setUrl(cursor.getString(3));
                spells.add(temp);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return spells;
    }

    public boolean insertSpell(Spell spell){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);
        ContentValues cv = new ContentValues();

        cv.put(COL_2_2,StartScreenActivity.currentUser.getUsername());
        if(spell.getName().contains("'")){
            spell.setName(spell.getName().replace("'","΄"));
        }
        cv.put(COL_3_2,spell.getName());
        cv.put(COL_4_2,spell.getUrl());
        long result = db.insert(TABLE_NAME_2, null, cv);
        db.close();
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public void deleteAllSpells(User user){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);

        db.execSQL("DELETE FROM " + TABLE_NAME_2 + " WHERE USERNAME='" + user.getUsername() + "'");
        db.close();
    }

    public void deleteSpell(User user, Spell spell){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);
        if(spell.getName().contains("'")){
            spell.setName(spell.getName().replace("'","΄"));
        }
        db.execSQL("DELETE FROM " + TABLE_NAME_2 + " WHERE USERNAME='" + user.getUsername()
                + "' AND SPELLNAME='" + spell.getName() + "'");
        db.close();
    }

    public void deleteAccount(User user){
        SQLiteDatabase db = this.getWritableDatabase(EnKey);

        db.execSQL("DELETE FROM " + TABLE_NAME_1 + " WHERE USERNAME='" + user.getUsername() + "'");
        db.execSQL("DELETE FROM " + TABLE_NAME_2 + " WHERE USERNAME='" + user.getUsername() + "'");
        db.close();
    }

}
