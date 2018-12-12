package se.gosta.storage;

import android.content.Context;
import android.util.Log;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FairFetcher {

    private static final String DEFAULT_URL = "http://10.0.2.2:8080";

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
                            // TODO: fetchLogo(c);
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

    public interface FairListener {

        void companiesUpdated(List<Company> companyList);

        void casesUpdated(Map<Integer, Integer[]> coords);
    }

}
