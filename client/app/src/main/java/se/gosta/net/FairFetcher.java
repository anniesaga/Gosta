package se.gosta.net;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.gosta.storage.Company;
import se.gosta.storage.Event;
import se.gosta.utils.Session;
import se.gosta.storage.Sponsor;
import se.gosta.utils.Utils;


/**
 * Class for fetching data from the server
 */
public class FairFetcher {


    private static final String DEFAULT_URL = "http://94.46.49.221:8080";

    // URL for debugging on phone
    // private static final String DEFAULT_URL = "http://192.168.43.128:8080";

    private static final String LOG_TAG = FairFetcher.class.getSimpleName();

    private List<FairListener> fairListenerList = new ArrayList<>();

    private Context context;

    public FairFetcher(Context context) {
        this.context = context;
    }

    /**
     * Method used to register as listener for FairFetcher
     * @param listener listener to be added
     */
    public void registerFairListener(FairListener listener) {
        fairListenerList.add(listener);
    }

    /**
     * Method used to get a list of companies and their logos from the server with Volleyrequest
     */
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

    /**
     * Method used to get a list of cases from the server with Volleyrequest
     */
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

    /**
     * Method used to get a list of events from the server with Volleyrequest
     */
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

    /**
     * Methods used to parse JSONArray and download to a map of sponsors.
     */
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

                        Map<String, Sponsor> sponsors = jsonToSponsor(response);

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

    /**
     * Method used to parse JSONArray to a list of companies
     * @param array JSONArray fetched from the server
     * @return List of companies
     */
    private List<Company> jsonToCompany(JSONArray array) {
        List<Company> companyList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            String name, contact = null, email = null, info = null, fileName = null, website = null;
            int recruiting = 0, partTime = 0, thesis = 0, caseNo = 0;
            try {
                JSONObject row = array.getJSONObject(i);
                name = row.getString("name");
                if (!row.isNull("contact_name")) {
                    contact = row.getString("contact_name");
                }
                if (!row.isNull("contact_email")) {
                    email = row.getString("contact_email");
                }
                if (!row.isNull("info")) {
                    info = row.getString("info");
                }
                if (!row.isNull("recruiting")) {
                    recruiting = row.getInt("recruiting");
                }
                if (!row.isNull("part_time")) {
                    partTime = row.getInt("part_time");
                }
                if (!row.isNull("thesis")) {
                    thesis = row.getInt("thesis");
                }
                if (!row.isNull("fileName")) {
                    fileName = row.getString("fileName");
                }
                if (!row.isNull("caseNo")) {
                    caseNo = row.getInt("caseNo");
                }
                if (!row.isNull("website")) {
                    website = row.getString("website");
                }

                Company c = new Company(name, contact, email, info, recruiting, partTime, thesis, fileName, caseNo, website);
                Log.d(LOG_TAG, "jsonToCompany(): " + c);
                companyList.add(c);


            } catch (JSONException e) {
                Log.d(LOG_TAG, "Error parsing JSON: " + e.getMessage());
            }
        }
        Collections.sort(companyList);
        return companyList;
    }

    /**
     * Method used to parse JSONArray to a map of cases
     * @param array JSONArray fetched from the server
     * @return Map of Cases and ID's
     */

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

    /**
     * Method used to parse JSONArray to a list of events
     * @param array JSONArray fetched from the server
     * @return List of events
     */
    private List<Event> jsonToEvent(JSONArray array) {
        List<Event> eventList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject row = array.getJSONObject(i);
                String start_time = row.getString("start_time");
                String name = row.getString("name");
                String info = null;
                if (!row.isNull("info")) {
                    info = row.getString("info");
                }
                Event e = new Event(start_time, name, info);
                Log.d(LOG_TAG, "jsonToEvent(): " + e);
                eventList.add(e);


            } catch (JSONException ex) {
                Log.d(LOG_TAG, "Error parsing JSON: " +ex.getMessage());
            }
        }
        Collections.sort(eventList);
        return eventList;
    }

    private Map<String, Sponsor> jsonToSponsor(JSONArray array) {
        Map<String, Sponsor> sponsorMap = new HashMap<>();
        for (int i = 0; i < array.length(); i++) {
            String website = null, info = null, fileName = null;
            try {
                JSONObject row = array.getJSONObject(i);
                String name = row.getString("name");
                if (!row.isNull("website")) {
                    website = row.getString("website");
                }
                if (!row.isNull("info")) {
                    info = row.getString("info");
                }
                if (!row.isNull("fileName")) {
                    fileName = row.getString("fileName");
                }

                Sponsor s = new Sponsor(name, website, info, fileName);
                Log.d(LOG_TAG, "jsonToSponsor(): " + s);
                sponsorMap.put(name, s);


            } catch (JSONException ex) {
                Log.d(LOG_TAG, "Error parsing JSON: " + ex.getMessage());
            }
        }
        return sponsorMap;
    }

    /**
     * Method used to fetch a logo for a specific company from the server
     * @param company The company for what logo to fetch
     */
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


    public interface FairListener {

        void companiesUpdated(List<Company> companyList);

        void casesUpdated(Map<Integer, Integer[]> coords);


        void eventsUpdated(List<Event> eventList);

        void sponsorsUpdated(Map<String, Sponsor> sponsors);

    }

}
