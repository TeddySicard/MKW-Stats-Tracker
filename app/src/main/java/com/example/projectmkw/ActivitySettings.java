package com.example.projectmkw;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import org.apache.commons.lang3.StringUtils;

public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        //////////Init action bar//////////////////////////////
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(StringUtils.capitalize(getString(R.string.title_activity_settings)));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_favorite:
                displayFavourites();
                return true;
            /*
            case R.id.action_settings:
                displaySettings();
                return true;

             */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayFavourites(){
        finish();
        startActivity(new Intent(this, ActivityFavourites.class));
    }

    private void displaySettings(){
        finish();
        startActivity(new Intent(this, ActivitySettings.class));
    }
}