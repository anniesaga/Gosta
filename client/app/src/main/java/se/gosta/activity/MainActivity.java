package se.gosta.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Session;
import se.gosta.storage.Utils;


public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Company> adapter;
    private ListView listView;
    private List<Company> companies;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private static final int MENU_ENTRY_CONTACT = 0 ;
    private static final int MENU_ENTRY_INFO = 1 ;

    private static final String DEFAULT_URL = "http://10.0.2.2:8080/resources/logos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupList();
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
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent (MainActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent (MainActivity.this, MenuActivity.class);
                                startActivity(intent);
                                return true;

                        }
                        return false;
                    }
                });
    }

    public void setupList(){

        companies = new ArrayList<>();

        listView = (ListView) findViewById(R.id.company_list);

        adapter = new ArrayAdapter<Company>(this,android.R.layout.simple_list_item_1, companies);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);


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
            }
        });
        //Company company = Session.get(Session.currentCompanyName);

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

    private void resetListView() {
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
        getCompanies();
        for (Company c : companies) {
            Log.d(LOG_TAG, c.toString());
        }
    }

    private List<Company> jsonToCompany(JSONArray array) {
        List<Company> companyList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject row = array.getJSONObject(i);
                String name = row.getString("name");
                String email = row.getString("email");
                String info = row.getString("info");
                String fileName = row.getString("fileName");
                int caseNo = row.getInt("caseNo");

                Company c = new Company(name, email, info, fileName, caseNo);
                Log.d(LOG_TAG, "jsonToCompany(): " + c);
                companyList.add(c);
                Collections.sort(companyList);

            } catch (JSONException e) {
                ;
            }
        }
        return companyList;
    }

    private void getCompanies() {
        Log.d(LOG_TAG, "getCompanies()");
        String url = "http://10.0.2.2:8080/companies";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        companies = jsonToCompany(response);
                        Session.getSession().companies = companies;
                        resetListView();
                        for(Company c : companies) {
                            fetchLogo(c);
                        }
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
    public void fetchLogo(final Company company) {
        Log.d(LOG_TAG, "fetchLogos()");
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        Log.d(LOG_TAG, " URL: " + DEFAULT_URL + company.fileName());
        String url = DEFAULT_URL + company.fileName();

        if ( ( url == null) || url.equals("null") ) {
            // Add default url??
            return;
        }
        Log.d(LOG_TAG, "download URL: " + url);

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d(LOG_TAG, "onResponse ok: " + bitmap.toString());
                try {
                    // Create a file from the bitmap
                    File f = Utils.createImageFile(MainActivity.this, company, bitmap);
                    Log.d(LOG_TAG, " created file: " + f);
                } catch (IOException e) {
                    // Since we failed creating the file, we don't need to remove any
                    Log.d(LOG_TAG, " failed created file: " + e);
                    e.printStackTrace();
                    return;
                }
            }
        }, 500, 500, ImageView.ScaleType.CENTER,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, "onResponse fail");
                    }
                });
        queue.add(imageRequest);
    }

}
