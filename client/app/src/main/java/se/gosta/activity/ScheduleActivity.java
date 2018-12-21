package se.gosta.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Event;
import se.gosta.storage.FairFetcher;
import se.gosta.storage.Sponsor;

public class ScheduleActivity extends AppCompatActivity {

    private static final String LOG_TAG = ScheduleActivity.class.getSimpleName();

    private ArrayAdapter<Event> adapter;
    private ListView listView;
    private List<Event> events;


    /**
     * On creation of this activity it calls the method for setting up the desired list of events
     * and initiates the bottom navigation bar with navigation options handled by a switch.
     * @param savedInstanceState Bundle containing the activity's previously frozen state, if there was one.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setupEventList();


        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_schedule);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(ScheduleActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_schedule:
                                // intent = new Intent(StartActivity.this, ScheduleActivity.class);
                                // startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(ScheduleActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });


    }

    /**
     * Method for setting up the list view for the schedule of events.
     * Includes an OnItemClickListener to enable initialization of a popup window for information
     * about the events.
     */
    public void setupEventList(){

        events = new ArrayList<>();
        listView = (ListView) findViewById(R.id.schedule_list);

        adapter = new ArrayAdapter<Event>(this, R.layout.layout_listview,
                events);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            /**
             * Method for handling a click on a list item calling the method for initialization of
             * a popup window, displaying information about the clicked event in text views.
             * @param parent The AdapterView where the click happened.
             * @param view The view within the AdapterView that was clicked.
             * @param position The position of the view in the adapter.
             * @param id The row id of the item (event) that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    final View view,
                                    int position,
                                    long id) {
                Log.d(LOG_TAG, "item clicked, pos:" + position + " id: " + id);

                Event event = (Event)listView.getItemAtPosition(position);

                initiatePopupWindow();

                ((TextView)pw.getContentView().findViewById(R.id.schedpopupname)).setText(event.eventName());
                ((TextView)pw.getContentView().findViewById(R.id.schedpopuptime)).setText(event.startTime());
                ((TextView)pw.getContentView().findViewById(R.id.schedpopupinfo)).setText(event.eventInfo());

                dimBehind(pw);


            }
        });


    }

    /**
     * Method for resetting the list view for events.
     * @param events The specific list that is being reset
     */
    private void resetListView(List<Event> events) {
        listView = (ListView) findViewById(R.id.schedule_list);
        adapter = new ArrayAdapter<>(this,
                R.layout.layout_listview, events);
        listView.setAdapter(adapter);
    }


    /**
     * Method for fetching the all the events in the list of events.
     * Calling FairListener for the server data for the events only and populating the list view.
     */
    @Override
    public void onStart() {
        super.onStart();
        for (Event e : events) {
            Log.d(LOG_TAG, e.toString());
        }
        FairFetcher fetcher = new FairFetcher(this);
        fetcher.registerFairListener(new FairFetcher.FairListener() {
            @Override
            public void companiesUpdated(List<Company> companyList) {
                // Do nothing with companies in this Activity
            }

            @Override
            public void casesUpdated(Map<Integer, Integer[]> coordsMap) {
                // Do nothing with cases in this Activity
            }

            @Override
            public void eventsUpdated(List<Event> eventList) {
                for (Event e : eventList) {
                    events.add(e);
                }
            }

            @Override
            public void sponsorsUpdated(Map<String, Sponsor> sponsors) {
                //Do nothing in this activity
            }
        });
        fetcher.getEvents();
        resetListView(events);

    }
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_schedule);
        setupEventList();
        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_schedule);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(ScheduleActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:

                                return true;
                            case R.id.action_settings:
                                intent = new Intent(ScheduleActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });
    }




    private PopupWindow pw;

    /**
     * Initiates a popup window for displaying events that are happening during the fair and
     * dismisses it if the outside is touched.
     */
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) ScheduleActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.schedulepopup,
                    (ViewGroup) findViewById(R.id.schedulepopup));

            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.setAnimationStyle(R.style.popup_window_animation);
            pw.setOutsideTouchable(true);
            pw.setFocusable(true);
            pw.setTouchable(true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Dims the outside of the popup window.
     * @param pw The popup window for which this settings apply to
     */
    public static void dimBehind(PopupWindow pw) {
        View container = pw.getContentView().getRootView();
        Context context = pw.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);

    }


}
