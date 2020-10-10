package com.example.skyknight.dnd5epocketassistant;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SpellsActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    public static int listIndex;
    public static int listTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spells_act);

        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null) {
            fragmentManager.
                    beginTransaction().
                    replace(R.id.container_2
                            ,new SpellBookFragment()
                            ,UtilitiesClass.MspellBook).commit();
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Return button is disabled to ensure application stability. Please use the in-app buttons to navigate.", Toast.LENGTH_LONG).show();
    }

}
