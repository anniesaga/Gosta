package se.gosta.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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

import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Event;
import se.gosta.storage.FairFetcher;
import se.gosta.storage.Session;
import se.gosta.storage.Sponsor;
import se.gosta.storage.Utils;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ArrayAdapter<Company> adapter;
    private ListView listView;
    private SearchView searchView;
    private List<Company> companies = new ArrayList<>();;

    public static Map<Integer, Company> companyMap = new HashMap<>();

    private static final int MENU_ENTRY_CONTACT = 0 ;
    private static final int MENU_ENTRY_INFO = 1 ;

    private static final String DEFAULT_URL = "http://10.0.2.2:8080";

 //   private static final String DEFAULT_URL = "http://192.168.43.128:8080";

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
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent (MainActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent (MainActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;

                        }
                        return false;
                    }
                });
    }

    public void setupList(){


        searchView = (SearchView) findViewById(R.id.searchView) ;
        listView = (ListView) findViewById(R.id.company_list);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, companies);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);

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
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        ListView listview = (ListView) v;
        AdapterView.AdapterContextMenuInfo acMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Company company = (Company)listview.getItemAtPosition(acMenuInfo.position);
        menu.setHeaderTitle(company.name());
        menu.add(Menu.NONE, MENU_ENTRY_CONTACT, Menu.NONE, company.email());
        Session.getSession().put(company.name(), company);
        Session.getSession().currentCompanyName = company.name();
        menu.add(Menu.NONE,MENU_ENTRY_INFO, Menu.NONE, "More info");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.d(LOG_TAG, "itemId" + item.getItemId());
        Log.d(LOG_TAG, "title: " +item.getTitle());

        if (item.getItemId() == MENU_ENTRY_CONTACT) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Session.get(Session.currentCompanyName).email()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
            intent.putExtra(Intent.EXTRA_TEXT, "mail body");
            startActivity(Intent.createChooser(intent, ""));

        }

        if (item.getItemId() == MENU_ENTRY_INFO ) {
            //Intent intent = new Intent(this, InfoActivity.class);
           // Bundle extras = new Bundle();
            //extras.putString("companyName", Session.getSession().currentCompanyName);

           // intent.putExtras(extras);
            //startActivity(intent);
        }

        return true;
    }

    private void resetListView(List<Company> companies) {
        Log.d(LOG_TAG, "resetListView() " + companies.size());

        listView = (ListView) findViewById(R.id.company_list);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, companies);
        listView.setAdapter(adapter);
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

        //getCases();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        setupList();
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
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MainActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(MainActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(MainActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;

                        }
                        return false;
                    }
                });
    }


}
