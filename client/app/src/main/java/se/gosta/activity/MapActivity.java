package se.gosta.activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.lukle.clickableareasimage.ClickableArea;
import at.lukle.clickableareasimage.ClickableAreasImage;
import at.lukle.clickableareasimage.OnClickableAreaClickedListener;
import at.lukle.clickableareasimage.PixelPosition;
import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Session;
import uk.co.senab.photoview.PhotoViewAttacher;


public class MapActivity extends AppCompatActivity implements OnClickableAreaClickedListener {

    private final String LOG_TAG = MapActivity.class.getSimpleName();

    ClickableAreasImage clickableAreasImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        ImageView img = (ImageView) findViewById(R.id.map);
        img.setImageResource(R.drawable.gostamap);
        clickableAreasImage = new ClickableAreasImage(new PhotoViewAttacher(img), this);

        List<ClickableArea> clickableAreas = new ArrayList<>();
        clickableAreas.add(new ClickableArea(370,473,130,87, Session.get("Acando")));
        Log.d(LOG_TAG, "Added company: " + Session.get("Acando") + "as clickable");
        clickableAreasImage.setClickableAreas(clickableAreas);



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
                                return true;
                            case R.id.action_map:
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                // intent = new Intent(StartActivity.this, ScheduleActivity.class);
                                // startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(MapActivity.this, MenuActivity.class);
                                startActivity(intent);
                                return true;

                        }
                        return false;
                    }
                });
    }


    @Override
    public void onClickableAreaTouched(Object item){
        if (item instanceof Company) {
        String text = ((Company) item).name();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

        }
        Log.d(LOG_TAG, "Clicked on ClickableArea");
    }

}

