package se.gosta.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Event;
import se.gosta.storage.FairFetcher;
import se.gosta.storage.Sponsor;

//Moved this code to main, keeping the class if we change our mind
public class ShakeActivity extends AppCompatActivity implements SensorEventListener {

    private static final String LOG_TAG = ShakeActivity.class.getSimpleName();
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y,last_z;
    private static final int SHAKE_THRESHOLD = 5;
    final List<Company> companies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FairFetcher fetcher = new FairFetcher(this);
        fetcher.registerFairListener(new FairFetcher.FairListener() {
            @Override
            public void companiesUpdated(List<Company> companyList) {
                Log.d(LOG_TAG, "companies: " + companyList);
                for(Company c : companyList) {
                    companies.add(c);
                    //companyMap.put(c.caseNo(), c);
                    //listView.requestLayout();
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shaker);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (companies.size() == 0) {
            return;
        }
    Sensor mySensor = sensorEvent.sensor;
    if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 1000){
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

            Log.d(LOG_TAG, "speed: " + speed);
            if (speed > SHAKE_THRESHOLD) {
                Log.d(LOG_TAG, "grodanboll: " + speed);
                getRandomCompany();
            }

            last_x = x;
            last_y = y;
            last_z = z;
            }
         }
      }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause(){
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void getRandomCompany(){


        int numberOfElements = companies.size();

        Random randomCompany = new Random();
        int n = randomCompany.nextInt(numberOfElements);
        Company comp = companies.get(n);
        Log.d(LOG_TAG, "comp from rand: " + comp.name());
        initiatePopupWindow(comp.name());
        dimBehind(pw);

    }

    private PopupWindow pw;
    private void initiatePopupWindow(String name) {
        Log.d(LOG_TAG, "running initpopupwin");
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) ShakeActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            assert inflater != null;
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup));


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
            Log.d(LOG_TAG, "before setting text to comp name.");
            ((TextView)pw.getContentView().findViewById(R.id.popupname)).setText(name);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
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
