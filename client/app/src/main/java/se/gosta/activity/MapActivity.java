package se.gosta.activity;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

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
import uk.co.senab.photoview.PhotoViewAttacher;


public class MapActivity extends AppCompatActivity implements OnClickableAreaClickedListener {

    private final String LOG_TAG = MapActivity.class.getSimpleName();

    private Map<Integer, Integer[]> coordsMap = new HashMap<>();

    ClickableAreasImage clickableAreasImage;

    private Context context;

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
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MapActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                 intent = new Intent(MapActivity.this, ScheduleActivity.class);
                                 startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(MapActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;

                        }
                        return false;
                    }
                });
    }

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
                     Map tmp = new HashMap(coords);
                     tmp.keySet().removeAll(coordsMap.keySet());
                     coordsMap.putAll(tmp);
                     setClickableAreas();
                 }

            @Override
            public void eventsUpdated(List<Event> eventList) {
                //Do nothing with events in this activity
            }
        });
        fetcher.getCases();

    }
    @Override
    public void onClickableAreaTouched(Object item){
        if (item instanceof Company) {
            Company company = ((Company) item);
            String text = company.name();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Clicked on ClickableArea");


        }

    }

    private void setClickableAreas() {

        ImageView img = (ImageView) findViewById(R.id.map);
        img.setImageResource(R.drawable.gostamap);
        clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(img), this);
        List<ClickableArea> clickableAreas = new ArrayList<>();
        for(int caseNo : coordsMap.keySet()) {
              if (MainActivity.companyMap.get(caseNo) != null) {
                clickableAreas.add(new ClickableArea(coordsMap.get(caseNo)[0],
                        coordsMap.get(caseNo)[1],
                        coordsMap.get(caseNo)[2],
                        coordsMap.get(caseNo)[3],
                        MainActivity.companyMap.get(caseNo)));
         }
        }
        clickableAreasImage.setClickableAreas(clickableAreas);


        //TODO: Write code to populate clickableareas from maps and update gui
    }

}

