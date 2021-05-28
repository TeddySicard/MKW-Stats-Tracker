package com.example.projectmkw;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityRoomFocus extends AppCompatActivity {

    private long lookedID;
    private RoomLookingType roomLookingType;
    private LinearLayout roomFocusLayout;
    private ProgressBar progressBar;
    private Room activeRoom;
    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_focus);


        //////////Init action bar//////////////////////////////
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(StringUtils.capitalize(getString(R.string.roomDetails)));


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
        roomFocusLayout = findViewById(R.id.roomFocusLayout);
        progressBar = findViewById(R.id.progressBar);

        new RetrieveFocusedRoom().execute(this.getApplicationContext());
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


    private class RetrieveFocusedRoom extends AsyncTask<Context, Void, Context> {

        @Override
        protected Context doInBackground(Context... contexts) {
            //////////Retrieve intent parameters//////////////////
            Intent myIntent = getIntent();
            lookedID = myIntent.getLongExtra("id", 0);
            roomLookingType = (RoomLookingType) myIntent.getSerializableExtra("roomType");
            activeRoom = Util.extractRoomFromJSON(Util.getJSON(lookedID, roomLookingType));
            return contexts[0];
        }

        @Override
        protected void onPostExecute(Context context) {
            try {
                progressBar.setVisibility(View.GONE);


                TextView focusedText = new TextView(context);
                focusedText.setText(StringUtils.capitalize(getString(R.string.lookingAt) + " "
                        + (roomLookingType.equals(RoomLookingType.room) ? getString(R.string.room) + " " + activeRoom.getRoom_name() :
                        getString(R.string.player) + " " + activeRoom.getMember(lookedID).getNames()[0])));
                focusedText.setTextSize(24);
                focusedText.setGravity(Gravity.CENTER);
                focusedText.setPadding(0, 20, 0, 0);
                roomFocusLayout.addView(focusedText);



                if (roomLookingType.equals(RoomLookingType.member)){
                    TextView lookedPlayerFriendCode = new TextView(context);
                    SpannableString content = new SpannableString(StringUtils.capitalize(activeRoom.getMember(lookedID).getFc()));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    lookedPlayerFriendCode.setTextColor(Color.BLUE);
                    lookedPlayerFriendCode.setPadding(0, 10, 0, 0);
                    lookedPlayerFriendCode.setTextSize(30);
                    lookedPlayerFriendCode.setGravity(Gravity.CENTER);
                    lookedPlayerFriendCode.setText(content);
                    lookedPlayerFriendCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List <Favourite> updatedFavs = Util.addFavourite(Util.getFavourites(context), activeRoom.getMember(lookedID));
                            Util.updateFavourite(context, updatedFavs);
                            Toast.makeText(context, activeRoom.getMember(lookedID).getNames()[0] + " " + getString(R.string.addedAsFav), Toast.LENGTH_SHORT).show();
                        }
                    });
                    roomFocusLayout.addView(lookedPlayerFriendCode);

                }

                LinearLayout mainVbox = new LinearLayout(context);
                mainVbox.setOrientation(LinearLayout.VERTICAL);
                mainVbox.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                mainVbox.setPadding(0, 30, 0, 50);
                roomFocusLayout.addView(mainVbox);


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
                roomType.setText(StringUtils.capitalize((activeRoom.isPrivate() ? getString(R.string.priv) : getString(R.string.pub)) + " " + getString(R.string.room) + " " + activeRoom.getRoom_name()));
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
                        lookAtAGame(activeRoom.getRoom_id(), RoomLookingType.room);
                    }
                });
                metaRoomHbox.addView(eyeRoom);


                TextView lastRace = new TextView(context);
                lastRace.setText(StringUtils.capitalize((Integer.parseInt(activeRoom.getTrack()[0]) == 0 ? getString(R.string.noTrackPlayed) : getString(R.string.lastTrack) + " : " + activeRoom.getTrack()[1])));
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


                for (Member aMember : activeRoom.getMembers()) {
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

                TextView hostingGuy = new TextView(context);
                hostingGuy.setText(StringUtils.capitalize(getString(R.string.roomHost) + " : " + (activeRoom.getHost() == null ? getString(R.string.unknown) : activeRoom.getHost().getNames()[0])));
                hostingGuy.setTextSize(24);
                roomFocusLayout.addView(hostingGuy);

                if(!activeRoom.isPrivate()) {
                    TextView averagePoints = new TextView(context);
                    averagePoints.setText(StringUtils.capitalize(getString(R.string.avgVP) + " : " + Util.averageEv(activeRoom)));
                    averagePoints.setTextSize(24);
                    roomFocusLayout.addView(averagePoints);
                }

            } catch (NullPointerException e) {
                TextView noRoom = new TextView(context);
                noRoom.setText(StringUtils.capitalize(getString(R.string.noRoomFound)));
                noRoom.setTextSize(30);
                noRoom.setGravity(Gravity.CENTER);
                roomFocusLayout.addView(noRoom);            }
            pullToRefresh.setEnabled(true);


        }

    }

    private void lookAtAGame(long id, RoomLookingType roomLookingType) {
        finish();
        Intent myIntent = new Intent(this, ActivityRoomFocus.class);
        myIntent.putExtra("id", id);
        myIntent.putExtra("roomType", roomLookingType);
        startActivity(myIntent);

    }

    private void refreshData() {
        finish();
        overridePendingTransition(0, 0);
        Intent myIntent = new Intent(this, ActivityRoomFocus.class);
        myIntent.putExtra("id", this.lookedID);
        myIntent.putExtra("roomType", this.roomLookingType);
        startActivity(myIntent);
        overridePendingTransition(0, 0);
    }

    private void displayFavourites(){
        finish();
        startActivity(new Intent(this, ActivityFavourites.class));
    }

    private void displaySettings(){
        startActivity(new Intent(this, ActivitySettings.class));
    }
}
