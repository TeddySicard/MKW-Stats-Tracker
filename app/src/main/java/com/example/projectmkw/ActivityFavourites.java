package com.example.projectmkw;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ActivityFavourites extends AppCompatActivity {

    private LinearLayout mainLinearLayout;
    private List<Favourite> favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //////////Init action bar//////////////////////////////
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(StringUtils.capitalize(getString(R.string.favs)));



        mainLinearLayout = findViewById(R.id.mainVerticalLayout);



        favourites = Util.getFavourites(this);

        for (Favourite aFav : favourites){
            LinearLayout favHbox = new LinearLayout(this);
            favHbox.setOrientation(LinearLayout.HORIZONTAL);
            favHbox.setWeightSum(1f);

            TextView fav = new TextView(this);
            fav.setText(aFav.getName());
            favHbox.addView(fav);

            ImageButton eyePlayer = new ImageButton(this);
            eyePlayer.setBackgroundResource(R.drawable.eye_logo);
            LinearLayout.LayoutParams eyePlayerParams = new LinearLayout.LayoutParams(
                    80,
                    80
            );
            eyePlayer.setLayoutParams(eyePlayerParams);
            eyePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookAtAGame(aFav.getPid(), RoomLookingType.member);
                }
            });
            favHbox.addView(eyePlayer);

            ImageButton deletePlayer = new ImageButton(this);
            deletePlayer.setBackgroundResource(R.drawable.bin_logo);
            LinearLayout.LayoutParams deletePlayerParams = new LinearLayout.LayoutParams(
                    80,
                    80
            );
            deletePlayer.setLayoutParams(deletePlayerParams);
            deletePlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.deleteFavourite(favourites, aFav.getPid());
                    Util.updateFavourite(ActivityFavourites.this, favourites);
                    refresh();
                }
            });
            favHbox.addView(deletePlayer);


            mainLinearLayout.addView(favHbox);
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
        overridePendingTransition(0, 0);
        startActivity(new Intent(this, ActivityFavourites.class));
        overridePendingTransition(0, 0);
    }

    private void displaySettings(){
        finish();
        startActivity(new Intent(this, ActivitySettings.class));
    }

    private void lookAtAGame(long id, RoomLookingType roomLookingType){
        finish();
        Intent myIntent = new Intent(this, ActivityRoomFocus.class);
        myIntent.putExtra("id", id);
        myIntent.putExtra("roomType", roomLookingType);
        startActivity(myIntent);

    }

    private void refresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
