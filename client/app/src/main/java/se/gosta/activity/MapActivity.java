package se.gosta.activity;


import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Event;
import se.gosta.storage.FairFetcher;
import se.gosta.storage.Session;
import se.gosta.storage.Sponsor;
import uk.co.senab.photoview.PhotoViewAttacher;

import static se.gosta.storage.Session.currentCompanyName;

/**
 * Activity that displays a map over the fair
 */

public class MapActivity extends AppCompatActivity implements OnClickableAreaClickedListener {

    private final String LOG_TAG = MapActivity.class.getSimpleName();

    @SuppressLint("UseSparseArrays")
    private Map<Integer, Integer[]> coordsMap = new HashMap<>();

    ClickableAreasImage clickableAreasImage;

    private Context context;

    /**
     * On creation of this activity it calls the method for displaying the map and
     * sets up the navigation bar at the bottom.
     * @param savedInstanceState Bundle containing the activity's previously frozen state, if there
     *      *                    was one.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_map);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MapActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(MapActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(MapActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });
    }

    /**
     * Method that reloads the map and registers as a listener for FairFetcher.
     * Calls a method for loading all coordinates for the map and creates touchable
     * areas for those coordinates.
     */
    @Override
    public void onStart() {
        super.onStart();
        FairFetcher fetcher = new FairFetcher(this);
        ImageView img = (ImageView) findViewById(R.id.map);
        img.setImageResource(R.drawable.gostamap);
        fetcher.registerFairListener(new FairFetcher.FairListener() {
            @Override
            public void companiesUpdated(List<Company> companyList) {
                // Do nothing with companies in this activity
            }


            @Override
            public void casesUpdated(Map<Integer, Integer[]> coords) {

                Log.d(LOG_TAG, "cases: " + coords);

                //Tried coords.forEach(coordsMap::putIfAbsent) but required API level 24..

                // Puts all coordinates in hashmap in a temporary hashmap and then to regular map.
                Map tmp = new HashMap(coords);
                tmp.keySet().removeAll(coordsMap.keySet());
                coordsMap.putAll(tmp);
                //Call to create ClickableAreas from coordinates.
                setClickableAreas();

            }

            @Override
            public void eventsUpdated(List<Event> eventList) {
                //Do nothing with events in this activity
            }


            @Override
            public void sponsorsUpdated(Map<String, Sponsor> sponsors) {
                // Do nothing with sponsors in this activity
            }

        });
        // Call method in FairFetcher to download cases from db
        fetcher.getCases();

    }

    /**
     * On resuming this activity, the bottom navigation bar is reloaded.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_map);
        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_map);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MapActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(MapActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(MapActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });
    }

    /**
     * Method for handling click events on ClickableArea
     * @param item The Company associated with the ClickableArea
     */
    @Override
    public void onClickableAreaTouched(Object item){
        if (item instanceof Company) {
            Company company = ((Company) item);
            Log.d(LOG_TAG, "Clicked on ClickableArea");

            // Set current company to company clicked
            Session.setCurrentCompanyName(company.name());
            Session.getSession().put(company.name(), company);
            initiatePopupWindow();
            ((TextView)pw.getContentView().findViewById(R.id.textbutton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, InfoActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("companyName", currentCompanyName);
                    intent.putExtras(extras);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
            // Find text for current company and display in textviews
            ((TextView)pw.getContentView().findViewById(R.id.comppopupname)).setText(company.name());
            ((TextView)pw.getContentView().findViewById(R.id.comppopupinfo)).setText(company.info());
            dimBehind(pw);
        }

    }



    private PopupWindow pw;

    /**
     * Initiates a popup window for displaying more information about a specific company.
     * Dismisses it if the outside is touched.
     */
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) MapActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Inflate the view from a predefined XML layout
            assert inflater != null;
            View layout = inflater.inflate(R.layout.companypopup,
                    (ViewGroup) findViewById(R.id.companypopup));

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
        assert wm != null;
        wm.updateViewLayout(container, p);

    }

    /**
     * Method for setting up all ClickableAreas on the map based on the Map
     * with coordinates and company associated with them.
     */
    private void setClickableAreas() {

        ImageView img = (ImageView) findViewById(R.id.map);
        img.setImageResource(R.drawable.gostamap);

        // Cast the Image to a ClickableAreasImage
        clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(img), this);
        List<ClickableArea> clickableAreas = new ArrayList<>();

        // Loop through all coordinates in Map and create ClickableArea for each keyset and add area
        // to list of clickableareas.
        for(int caseNo : coordsMap.keySet()) {

            try {
                if (MainActivity.companyMap.get(caseNo) != null) {
                    clickableAreas.add(new ClickableArea(coordsMap.get(caseNo)[0],
                            coordsMap.get(caseNo)[1],
                            coordsMap.get(caseNo)[2],
                            coordsMap.get(caseNo)[3],
                            MainActivity.companyMap.get(caseNo)));
                }

            }catch (NullPointerException npe){
                Log.d(LOG_TAG, "Error when fetching from coordsMap" + npe.getMessage());
            }

        }
        // Add ClickableAreas to the Image.
        clickableAreasImage.setClickableAreas(clickableAreas);
    }

}