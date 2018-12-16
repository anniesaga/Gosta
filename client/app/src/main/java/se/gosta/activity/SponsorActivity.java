package se.gosta.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Map;

import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.FairFetcher;
import se.gosta.storage.Sponsor;

public class SponsorActivity extends AppCompatActivity {

    private static final String LOG_TAG = SponsorActivity.class.getSimpleName();

    private ArrayAdapter<Sponsor> adapter;
    private ListView listView;
    private List<Sponsor> sponsors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor);
        setupSponsorList();
        //resetListView(sponsors);


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
                                Intent intent = new Intent(SponsorActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(SponsorActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_schedule:
                                 intent = new Intent(SponsorActivity.this, ScheduleActivity.class);
                                 startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(SponsorActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;

                        }
                        return false;
                    }
                });
    }
   public void setupSponsorList(){

        sponsors = new ArrayList<>();

        listView = (ListView) findViewById(R.id.sponsor_list);

        adapter = new ArrayAdapter<Sponsor>(this, android.R.layout.simple_list_item_1,
                sponsors);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    final View view,
                                    int position /*The position of the view in the adapter.*/,
                                    long id /* The row id of the item that was clicked */) {
                Log.d(LOG_TAG, "item clicked, pos:" + position + " id: " + id);

                Sponsor sponsor = (Sponsor)listView.getItemAtPosition(position);

                // getEvents();

                initiatePopupWindow();
                ((TextView)pw.getContentView().findViewById(R.id.popupname)).setText(sponsor.sponsorName());
                ((TextView)pw.getContentView().findViewById(R.id.popuptime)).setText(sponsor.sponsorInfo());
                ((TextView)pw.getContentView().findViewById(R.id.popupinfo)).setText(sponsor.sponsorWebsite());

                dimBehind(pw);


            }
        });


    }

    private void resetListView(List<Sponsor> sponsors) {
        listView = (ListView) findViewById(R.id.sponsor_list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, sponsors);
        listView.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        FairFetcher fetcher = new FairFetcher(this);
        fetcher.registerFairListener(new FairFetcher.FairListener() {
            @Override
            public void sponsorsUpdated(List<Sponsor> sponsorList) {
                Log.d(LOG_TAG, "sponsors: " + sponsorList);
                for(Sponsor s : sponsorList) {
                    sponsors.add(s);
                }
            }
            @Override
            public void companiesUpdated(List<Company> companyList){

            }

            @Override
            public void casesUpdated(Map<Integer, Integer[]> coordsMap) {

            }

        });
        fetcher.getSponsors();
        resetListView(sponsors);
        //getCases();

    }

   /* @Override
    public void onStart() {
        super.onStart();
        getSponsors();
        for (Sponsor s : sponsors) {
            Log.d(LOG_TAG, s.toString());
        }
    }*/

  /*  private List<Sponsor> jsonToSponsor(JSONArray array) {
        List<Sponsor> sponsorList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject row = array.getJSONObject(i);
                String name = row.getString("name");
                String website = row.getString("website");
                String info = row.getString("info");
                String fileName = row.getString("fileName");
                Sponsor s = new Sponsor(name, website, info, fileName);
                Log.d(LOG_TAG, "jsonToSponsor(): " + s);
                sponsorList.add(s);


            } catch (JSONException ex) {
                ;
            }
        }
        return sponsorList;
    }
*/
  /*
    private void getSponsors() {
        Log.d(LOG_TAG, "getSponsors()");
        String url = "http://10.0.2.2:8080/sponsors";
        //  String url = "http://192.168.43.128:8080/sponsors";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        sponsors = jsonToSponsor(response);

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

    }*/

    private PopupWindow pw;
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) SponsorActivity.this
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

}
