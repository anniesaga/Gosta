package se.gosta.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.gosta.activity.MainActivity;


public class FairFetcher {

    private static final String DEFAULT_URL = "http://10.0.2.2:8080";

  //  private static final String DEFAULT_URL = "http://192.168.43.128:8080";


    private static final String LOG_TAG = FairFetcher.class.getSimpleName();

    private List<FairListener> fairListenerList = new ArrayList<>();

    private Context context;

    public FairFetcher(Context context) {
        this.context = context;
    }

    public void registerFairListener(FairListener listener) {
        fairListenerList.add(listener);
    }

    public void getCompanies() {

        Log.d(LOG_TAG, "getCompanies()");
        String url = DEFAULT_URL + "/companies";


        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Map<Integer, Company> companyMap = new HashMap<>();
                        List<Company> companies = new ArrayList<>();

                        companies = jsonToCompany(response);
                        Session.getSession().companies = companies;
                        for(Company c : companies) {
                            companyMap.put(c.caseNo(), c);
                            fetchLogo(c);
                        }
                        for(FairListener listener : fairListenerList) {
                            listener.companiesUpdated(companies);
                        }
                        Log.d(LOG_TAG, "onResponse ok");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, " cause 2: " + error.getCause().getMessage());

            }
        });
        queue.add(jsonArrayRequest);

    }

    public void getCases() {

        Log.d(LOG_TAG, "getCases()");

        String url = DEFAULT_URL + "/cases";


        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Map<Integer, Integer[]> coordsMap = jsonToCase(response);
                        Log.d(LOG_TAG, "onResponse ok");
                        for(FairListener l : fairListenerList) {
                            l.casesUpdated(coordsMap);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Log.d(LOG_TAG, " cause 1: " + url);
                Log.d(LOG_TAG, " cause 1: " + error.getCause().getMessage());
                error.printStackTrace();

            }
        });
        queue.add(jsonArrayRequest);

    }

    public void getEvents() {

        Log.d(LOG_TAG, "getEvents()");
        String url =  DEFAULT_URL + "/schedule";

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Event> events = new ArrayList<>();
                        events = jsonToEvent(response);
                        Log.d(LOG_TAG, "onResponse ok");
                        for(FairListener l : fairListenerList) {
                            l.eventsUpdated(events);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());

            }
        });
        queue.add(jsonArrayRequest);

    }

    private List<Company> jsonToCompany(JSONArray array) {
        List<Company> companyList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject row = array.getJSONObject(i);
                String name = row.getString("name");
                String contact = row.getString("contact_name");
                String email = row.getString("contact_email");
                String info = row.getString("info");
                int recruiting = row.getInt("recruiting");
                int partTime = row.getInt("part_time");
                int thesis = row.getInt("thesis");
                String fileName = row.getString("fileName");
                int caseNo = row.getInt("caseNo");
                String website = "website";

                Company c = new Company(name, contact, email, info, recruiting, partTime, thesis, fileName, caseNo, website);
                Log.d(LOG_TAG, "jsonToCompany(): " + c);
                companyList.add(c);
                Collections.sort(companyList);

            } catch (JSONException e) {
                Log.d(LOG_TAG, "Error parsing JSON: " + e.getMessage());
            }
        }
        return companyList;
    }

    private Map<Integer,Integer[]> jsonToCase(JSONArray array) {


        Map<Integer, Integer[]> coordsMap = new HashMap<>();

        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                int id = row.getInt("caseNo");
                JSONArray arr = row.getJSONArray("coords");
                Integer[] coords = new Integer[]{
                        (Integer) arr.get(0),
                        (Integer) arr.get(1),
                        (Integer) arr.get(2),
                        (Integer) arr.get(3)
                };
                coordsMap.put(id, coords);

            }

        } catch (JSONException jsone) {
            Log.d(LOG_TAG, "Error parsing JSON: " + jsone.getMessage());
        }
        return coordsMap;
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

    private void fetchLogo(final Company company) {
        Log.d(LOG_TAG, "fetchLogo()");
        RequestQueue queue = Volley.newRequestQueue(context);
        Log.d(LOG_TAG, " URL: " + DEFAULT_URL + "/resources/logos/" + company.fileName());
        String url = DEFAULT_URL + "/resources/logos/" + company.fileName();

        if (company.fileName() == null) {
            return;
        }



        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d(LOG_TAG, "onResponse ok: " + bitmap.toString());
                if (!Utils.avatarExists(context, company)) {
                try {
                    // Create a file from the bitmap
                        File f = Utils.createImageFile(context, company, bitmap);
                        Log.d(LOG_TAG, " created file: " + f);
                    } catch(IOException e){
                        // Since we failed creating the file, we don't need to remove any
                        Log.d(LOG_TAG, " failed created file: " + e);
                        e.printStackTrace();
                        return;
                    }
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


    public void getSponsors() {

        Log.d(LOG_TAG, "getSponsors()");
        String url = DEFAULT_URL + "/sponsors";


        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Sponsor> sponsors = new ArrayList<>();

                        sponsors = jsonToSponsor(response);


                        for(FairListener listener : fairListenerList) {
                            listener.sponsorsUpdated(sponsors);
                        }
                        Log.d(LOG_TAG, "onResponse ok");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, " cause 2: " + error.getCause().getMessage());

            }
        });
        queue.add(jsonArrayRequest);

    }

    private List<Sponsor> jsonToSponsor(JSONArray array) {
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

    public interface FairListener {

        void companiesUpdated(List<Company> companyList);

        void casesUpdated(Map<Integer, Integer[]> coords);


        void eventsUpdated(List<Event> eventList);

        void sponsorsUpdated(List<Sponsor> sponsorList);

    }

}
