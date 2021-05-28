package com.example.projectmkw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class ActivityAllRooms extends AppCompatActivity {

    private LinearLayout allRoomsLayout;
    private ProgressBar progressBar;
    private List<Room> activeRooms;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rooms);

        //////////Init action bar//////////////////////////////
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(StringUtils.capitalize(getString(R.string.allRooms)));



        //////////Pull to refresh implementation//////////////////
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setEnabled(false);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });

        //////////Links xml elements to the activity one////////////
        allRoomsLayout = findViewById(R.id.allRoomsLayout);
        progressBar = findViewById(R.id.progressBar);

        new RetrieveMKWRooms().execute(this.getApplicationContext());

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

    private class RetrieveMKWRooms extends AsyncTask<Context, Void, Context> {

        @Override
        protected Context doInBackground(Context... contexts) {
            activeRooms = Util.getAllRooms();
            return contexts[0];
        }

        @Override
        protected void onPostExecute(Context context) {
            try {
                Collections.sort(activeRooms);
                progressBar.setVisibility(View.GONE);
                for (Room aRoom : activeRooms) {

                    LinearLayout mainVbox = new LinearLayout(context);
                    mainVbox.setOrientation(LinearLayout.VERTICAL);
                    mainVbox.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    mainVbox.setPadding(0, 0, 0, 40);
                    allRoomsLayout.addView(mainVbox);


                    LinearLayout metaRoomHbox = new LinearLayout(context);
                    metaRoomHbox.setOrientation(LinearLayout.HORIZONTAL);
                    metaRoomHbox.setGravity(Gravity.CENTER);
                    metaRoomHbox.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    metaRoomHbox.setBackgroundColor(Color.parseColor("#6643AD"));
                    mainVbox.addView(metaRoomHbox);


                    TextView roomType = new TextView(context);
                    roomType.setText(StringUtils.capitalize((aRoom.isPrivate() ? getString(R.string.priv) : getString(R.string.pub)) + " " + getString(R.string.room) + " " + aRoom.getRoom_name()));
                    roomType.setTextSize(20);
                    roomType.setPadding(0, 0, 10, 0);
                    metaRoomHbox.addView(roomType);


                    ImageButton eyeRoom = new ImageButton(context);
                    eyeRoom.setBackgroundResource(R.drawable.eye_logo);
                    eyeRoom.setLayoutParams(new ViewGroup.LayoutParams(
                            100,
                            100
                    ));
                    eyeRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lookAtAGame(aRoom.getRoom_id(), RoomLookingType.room);
                        }
                    });
                    metaRoomHbox.addView(eyeRoom);


                    TextView lastRace = new TextView(context);
                    lastRace.setText(StringUtils.capitalize((Integer.parseInt(aRoom.getTrack()[0]) == 0 ? getString(R.string.noTrackPlayed) : getString(R.string.lastTrack) + " : " + aRoom.getTrack()[1])));
                    lastRace.setGravity(Gravity.CENTER);
                    lastRace.setTextSize(15);
                    lastRace.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    lastRace.setBackgroundColor(Color.parseColor("#6643AD"));
                    lastRace.setPadding(0, 0, 0, 10);
                    mainVbox.addView(lastRace);



                    LinearLayout columnsTitleHbox = new LinearLayout(context);
                    columnsTitleHbox.setOrientation(LinearLayout.HORIZONTAL);
                    columnsTitleHbox.setWeightSum(1);
                    columnsTitleHbox.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    columnsTitleHbox.setWeightSum(1);
                    columnsTitleHbox.setPadding(0, 10, 0, 5);
                    mainVbox.addView(columnsTitleHbox);


                    TextView vp = new TextView(context);
                    vp.setText(StringUtils.capitalize(getString(R.string.vp)));
                    vp.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            .375f
                    ));
                    vp.setGravity(Gravity.CENTER);
                    vp.setTextSize(16);
                    columnsTitleHbox.addView(vp);


                    TextView vb = new TextView(context);
                    vb.setText(StringUtils.capitalize(getString(R.string.bp)));
                    vb.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            .375f
                    ));
                    vb.setGravity(Gravity.CENTER);
                    vb.setTextSize(16);
                    columnsTitleHbox.addView(vb);


                    TextView mN = new TextView(context);
                    mN.setText(StringUtils.capitalize(getString(R.string.miiName)));
                    mN.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            .25f));
                    mN.setGravity(Gravity.CENTER);
                    mN.setTextSize(16);
                    columnsTitleHbox.addView(mN);


                    for (Member aMember : aRoom.getMembers()) {
                        LinearLayout playerStatsHbox = new LinearLayout(context);
                        playerStatsHbox.setOrientation(LinearLayout.HORIZONTAL);
                        playerStatsHbox.setWeightSum(1f);

                        TextView versusPointsText = new TextView(context);
                        versusPointsText.setText(aMember.getEv() == -1 ? "- - - -" : Integer.toString(aMember.getEv()));
                        versusPointsText.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                .365f
                        ));
                        versusPointsText.setGravity(Gravity.CENTER);
                        versusPointsText.setTextSize(16);
                        playerStatsHbox.addView(versusPointsText);


                        TextView battlePointsText = new TextView(context);
                        battlePointsText.setText(aMember.getEb() == -1 ? "- - - -" : Integer.toString(aMember.getEb()));
                        battlePointsText.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                .365f
                        ));
                        battlePointsText.setGravity(Gravity.CENTER);
                        battlePointsText.setTextSize(16);
                        playerStatsHbox.addView(battlePointsText);


                        TextView miiNameText = new TextView(context);
                        if (!aMember.getNames()[1].equals("")) {
                            miiNameText.setText("1) " + aMember.getNames()[0] + "\n2) " + aMember.getNames()[1]);
                        } else {
                            miiNameText.setText(aMember.getNames()[0]);
                        }
                        miiNameText.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                .27f
                        ));
                        miiNameText.setGravity(Gravity.LEFT);
                        miiNameText.setTextSize(16);
                        playerStatsHbox.addView(miiNameText);


                        ImageButton eyePlayer = new ImageButton(context);
                        eyePlayer.setBackgroundResource(R.drawable.eye_logo);
                        LinearLayout.LayoutParams eyePlayerParams = new LinearLayout.LayoutParams(
                                80,
                                80
                        );
                        eyePlayer.setLayoutParams(eyePlayerParams);
                        eyePlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                lookAtAGame(aMember.getPid(), RoomLookingType.member);
                            }
                        });
                        playerStatsHbox.addView(eyePlayer);


                        mainVbox.addView(playerStatsHbox);

                    }




                }


            } catch (NullPointerException e) {
                TextView noRoom = new TextView(context);
                noRoom.setText(StringUtils.capitalize(getString(R.string.noRoomFound)));
                noRoom.setTextSize(30);
                noRoom.setGravity(Gravity.CENTER);
                allRoomsLayout.addView(noRoom);

            }
            pullToRefresh.setEnabled(true);


        }
    }


    private void refreshData() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void lookAtAGame(long id, RoomLookingType roomLookingType){
        Intent myIntent = new Intent(this, ActivityRoomFocus.class);
        myIntent.putExtra("id", id);
        myIntent.putExtra("roomType", roomLookingType);
        startActivity(myIntent);

    }

    private void displayFavourites(){
        startActivity(new Intent(this, ActivityFavourites.class));
    }

    private void displaySettings(){
        startActivity(new Intent(this, ActivitySettings.class));
    }
}
