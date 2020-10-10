package com.example.skyknight.dnd5epocketassistant;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StartScreenActivity extends AppCompatActivity {
    public static User currentUser = new User();
    public static ArrayList<Spell> allSpells = new ArrayList<>();
    public static ArrayList<Spell> userSpells = new ArrayList<>();
    private static FragmentManager fragmentManager;
    public static net.sqlcipher.database.SQLiteDatabase db;

    private static Dialog popupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        net.sqlcipher.database.SQLiteDatabase.loadLibs(getApplicationContext());
        db = DatabaseHelper.getInstance(this).getWritableDatabase(DatabaseHelper.EnKey);
        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null) {
            fragmentManager.
                    beginTransaction().replace(R.id.container
                    ,new LogInFragment()
                    ,UtilitiesClass.LogInFragName)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        launchPopUpWindow();
    }



    public void launchPopUpWindow() {
        popupDialog = new Dialog(this);
        popupDialog.setContentView(R.layout.confirmation_popup);
        TextView popupTitle = popupDialog.findViewById(R.id.confirm_title);
        popupTitle.setText("Are you sure you want to exit?");
        Button yes_btn = (Button) popupDialog.findViewById(R.id.popup_yes_btn);
        Button no_btn = (Button) popupDialog.findViewById(R.id.popup_no_btn);

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
                finishAffinity(); //Terminate application run.
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

}
