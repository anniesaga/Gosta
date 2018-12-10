package se.gosta.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.gosta.R;
import se.gosta.storage.Event;

public class ScheduleActivity extends AppCompatActivity {

    private static final String LOG_TAG = ScheduleActivity.class.getSimpleName();

    private ArrayAdapter<Event> adapter;
    private ListView listView;
    private List<Event> events;


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
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_map:
                                 intent = new Intent(ScheduleActivity.this, MapActivity.class);
                                 startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_schedule:
                                // intent = new Intent(StartActivity.this, ScheduleActivity.class);
                                // startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(ScheduleActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;

                        }
                        return false;
                    }
                });


    }
    public void setupEventList(){

        events = new ArrayList<>();

        listView = (ListView) findViewById(R.id.schedule_list);

        adapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1,
                events);

        listView.setAdapter(adapter);

     //   registerForContextMenu(listView);


        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    final View view,
                                    int position /*The position of the view in the adapter.*/,
                                    long id /* The row id of the item that was clicked */) {
                Log.d(LOG_TAG, "item clicked, pos:" + position + " id: " + id);

                Event event = (Event)listView.getItemAtPosition(position);

               // getEvents();

                initiatePopupWindow();
                ((TextView)pw.getContentView().findViewById(R.id.popupname)).setText(event.eventName());
                ((TextView)pw.getContentView().findViewById(R.id.popuptime)).setText(event.startTime());
                ((TextView)pw.getContentView().findViewById(R.id.popupinfo)).setText(event.eventInfo());

                dimBehind(pw);

          /*      TextView tv = (TextView) findViewById(R.id.popup);
                tv.setText(event.eventName());
                Log.d(LOG_TAG, "Title set: " + event.eventName());
*/

               /* TextView tv = (TextView) findViewById(R.id.popup);
                tv.setText(event.eventInfo());
*/
      /*          Intent intent = new Intent(ScheduleActivity.this, InfoActivity.class);
                Bundle extras = new Bundle();
                //extras.putString("startTime", event.startTime());
                extras.putString("eventName", event.eventName());

                intent.putExtras(extras);
                startActivity(intent);
*/


              }
        });


    }


    private void resetListView() {
        listView = (ListView) findViewById(R.id.schedule_list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, events);
        listView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getEvents();
        for (Event e : events) {
            Log.d(LOG_TAG, e.toString());
        }
    }

    private List<Event> jsonToEvent(JSONArray array) {
        List<Event> eventList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject row = array.getJSONObject(i);
                String start_time = row.getString("start_time");
                String name = row.getString("name");
                String info = row.getString("info");
                Event e = new Event(start_time, name, info);
                Log.d(LOG_TAG, "jsonToEvent(): " + e);
                eventList.add(e);


            } catch (JSONException ex) {
                ;
            }
        }
        return eventList;
    }

    private void getEvents() {
        Log.d(LOG_TAG, "getEvents()");
        String url = "http://10.0.2.2:8080/schedule";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        events = jsonToEvent(response);
                     //   Session.getSession().companies = companies;
                        resetListView();

                        Log.d(LOG_TAG, "onResponse ok");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());

            }
        });
        queue.add(jsonArrayRequest);

    }

    private PopupWindow pw;
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) ScheduleActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
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


          //  pw.update();


            //    ((TextView)pw.getContentView().findViewById(R.id.popup)).setText("TEST");



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
        wm.updateViewLayout(container, p);

    }






    /*private void initiatePopupWindow(View v) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }*/

}
