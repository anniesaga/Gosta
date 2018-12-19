package se.gosta.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Event;
import se.gosta.storage.FairFetcher;
import se.gosta.storage.Session;
import se.gosta.storage.Sponsor;
import se.gosta.storage.Utils;

import static se.gosta.storage.Session.currentCompanyName;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Sensor related variables for shake function
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y,last_z;
    private static final int SHAKE_THRESHOLD = 20;

    //Listview related variables
    private ArrayAdapter<Company> adapter;
    private ListView listView;
    private SearchView searchView;
    private List<Company> companies = new ArrayList<>();;

    public static Map<Integer, Company> companyMap = new HashMap<>();


    private static final String DEFAULT_URL = "http://10.0.2.2:8080";

 //   private static final String DEFAULT_URL = "http://192.168.43.128:8080";

    /**
     * On creation of this activity it calls the method for setting up the desired list of companies
     * and initiates the bottom navigation bar with navigation options handled by a switch.
     * In this block we also grant access to the systems sensors in order to use the accelerometer
     * for detection movements.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupList();
        resetListView(companies);

        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_companies);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Session.setCurrentCompanyName(null);
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                return true;
                            case R.id.action_map:
                                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent (MainActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent (MainActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });

        /*Invoke getSystemService to fetch the systems SensorManager instance to access the systems sensors
          and gets a reference to the systems sensor service by passing the name of it*/
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Gets a reference to the systems accelerometer by invoking getDefaultSensor and passing the type
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Registers the sensor by using registerListener passing the activity's context, a sensor and in what rate that sensor events should be delivered
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Method for setting up the list view and search field for companies.
     * Includes an OnItemClickListener to enable navigation from this activity to InfoActivity,
     * where the company specific information is displayed.
     */

    public void setupList(){

        searchView = (SearchView) findViewById(R.id.searchView) ;
        listView = (ListView) findViewById(R.id.company_list);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, companies);

        listView.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for (Company c : companies) {
                    if (c.name().contains(query)) {
                            adapter.getFilter().filter(query);
                    }
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });


        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    final View view,
                                    int position /*The position of the view in the adapter.*/,
                                    long id /* The row id of the item that was clicked */) {
                Log.d(LOG_TAG, "item clicked, pos:" + position + " id: " + id);

                Company comp = (Company)listView.getItemAtPosition(position);
                Session.getSession().put(comp.name(), comp);
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                Bundle extras = new Bundle();
                extras.putString("companyName", comp.name());
                Session.setCurrentCompanyName(comp.name());
                intent.putExtras(extras);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }


    /**
     * Resets the listview and checks if the session has a current company.
     * @param companies the list of companies
     */
    private void resetListView(List<Company> companies) {
        Log.d(LOG_TAG, "resetListView() " + companies.size());

        listView = (ListView) findViewById(R.id.company_list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, companies);
        listView.setAdapter(adapter);

        // Checks if Session has current company and if so sets the listview to that position.
        if(Session.currentCompanyName != null) {
            int position = companies.indexOf(Session.get(Session.currentCompanyName));
            Log.d(LOG_TAG, "Fetched company on index: " + position);
            listView.setSelection(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FairFetcher fetcher = new FairFetcher(this);
        fetcher.registerFairListener(new FairFetcher.FairListener() {
                     @Override
                     public void companiesUpdated(List<Company> companyList) {
                         Log.d(LOG_TAG, "companies: " + companyList);
                         if (companies.size() > 0 ){
                             companies.clear();
                         }
                         for(Company c : companyList) {
                             companies.add(c);
                             companyMap.put(c.caseNo(), c);
                             listView.requestLayout();
                         }
                     }

                     @Override
                     public void casesUpdated(Map<Integer, Integer[]> coordsMap) {
                         // Do nothing with cases in this Activity
                     }


                    @Override
                    public void eventsUpdated(List<Event> eventList) {
                        // Do nothing with events in this Activity
                    }


                     @Override
                     public void sponsorsUpdated(List<Sponsor> sponsorList){

                     }
                 });

                fetcher.getCompanies();
                resetListView(companies);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        setupList();
        resetListView(companies);
        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_companies);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MainActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(MainActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(MainActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });

        //Register the sensor when the application resumes
        senSensorManager.registerListener(this, senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

    }

    /**
     * Detects shake gesture, invoked every time the sensor detects a change (whenever the device is in motion).
     * If the device is shaken in a speed that reaches the value set for threshold this method calls
     * the method that generates a random company.
     * @param sensorEvent represents a sensor event including the type, timestamp, accuracy and data.
     *
     *
     */

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (companies.size() == 0) {
            return;
        }
        Sensor mySensor = sensorEvent.sensor;

        //Check that we get a reference to the correct sensor type, then ask the sensor event for its values (the devices position).
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //Restrict the data sampling to only fetching every 1000 ms
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 1000){
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                //Calculate the device's speed to detect if it has been shaken.
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                Log.d(LOG_TAG, "speed: " + speed);

                //If the device has been shaken in enough speed, get a random company from companies
                if (speed > SHAKE_THRESHOLD) {
                    Log.d(LOG_TAG, "the speed is enough: " + speed);
                    getRandomCompany();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    /**
     * This method is required for the SensorEventListener but is not used in this project.
     */

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    //Do nothing in this activity
    }


    /**
     * Unregister the sensor when the application is not active
     */
    @Override
    protected void onPause(){
        super.onPause();
        senSensorManager.unregisterListener(this);
    }


    /**
     * Dismisses active popup window if there are one.
     * Generates a random company from companies and calls a method for initiating a popup window
     * that displays info about that company.
     *
     */
    private void getRandomCompany(){

        if (pw != null) {
            pw.dismiss();
        }

        int numberOfElements = companies.size();

        Random randomCompany = new Random();
        int n = randomCompany.nextInt(numberOfElements);
        Company comp = companies.get(n);
        Log.d(LOG_TAG, "comp from rand: " + comp.name());
        initiatePopupWindow(comp.name(), comp.info());
        dimBehind(pw);
    }



    private PopupWindow pw;

    /**
     * Initiates a popup window for displaying company name and information and dismisses it if the
     * outside is touched.
     * @param name The name of the company
     * @param info The information about the company
     */
    private void initiatePopupWindow(String name, String info) {
        Log.d(LOG_TAG, "running initpopupwin");
        try {
            //Get the instance of the LayoutInflater, using the context of this activity
            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            assert inflater != null;
            View layout = inflater.inflate(R.layout.companypopup,
                    (ViewGroup) findViewById(R.id.companypopup));

            pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            //Display the popup in the center
            pw.setAnimationStyle(R.style.popup_window_animation);
            pw.setOutsideTouchable(true);
            pw.setFocusable(true);
            pw.setTouchable(true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Log.d(LOG_TAG, "before setting text to comp name.");


            /*Sets a text button called "More info" that directs the user to the info activity for
              the displayed company*/
            ((TextView)pw.getContentView().findViewById(R.id.textbutton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("companyName", currentCompanyName);
                    intent.putExtras(extras);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });

            //Text views for displaying the generated company's name and information
            ((TextView)pw.getContentView().findViewById(R.id.comppopupname)).setText(name);
            ((TextView)pw.getContentView().findViewById(R.id.comppopupinfo)).setText(info);





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
        assert wm != null;
        wm.updateViewLayout(container, p);
    }

}
