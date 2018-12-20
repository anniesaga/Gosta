package se.gosta.activity;

import se.gosta.R;
import se.gosta.storage.MenuOption;
import se.gosta.storage.Session;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays more options that is not displayed
 * in the bottom navigation bar
 */

public class MenuActivity extends AppCompatActivity {

    private final String LOG_TAG = MenuActivity.class.getSimpleName();
    private ListAdapter adapter;
    private List<MenuOption>menuOptions;
    private ListView listView;

    /**
     * On creation of this activity it calls the method to create and setup the list of MenuOptions and
     * also sets up the navigation bar at the bottom.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        createMenuOptionList();
        setupMenuList();

        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_settings);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Session.setCurrentCompanyName(null);
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MenuActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(MenuActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:

                                return true;

                        }
                        return false;
                    }
                });

    }

    /**
     * On resuming this activity it calls the method to setup the list of MenuOptions and
     * also sets up the navigation bar at the bottom.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_menu);
        setupMenuList();
        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_settings);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MenuActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(MenuActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:

                                return true;

                        }
                        return false;
                    }
                });
    }

    /**
     * Method for loading and displaying the listview
     */
    private void setupMenuList(){
        ListView listView = (ListView) findViewById(R.id.menu_list);

        adapter =  new ArrayAdapter<MenuOption>(this,
                R.layout.layout_listview,
                menuOptions);

        listView.setAdapter(adapter);

        // Switch-statement to handle click-events and start corresponding activity
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    final View view,
                                    int position,
                                    long id) {
                Log.d(LOG_TAG, "item clicked, pos: " + position + "id: " + id);

                ListView listView = (ListView) findViewById(R.id.menu_list);
                MenuOption menuOption = (MenuOption) listView.getItemAtPosition(position);


                switch (menuOption.menuNavigation) {
                    case R.id.main:
                        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.activity_map:
                        intent = new Intent(MenuActivity.this, MapActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.activity_reader:
                        intent = new Intent(MenuActivity.this, ReaderActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.activity_schedule:
                        intent = new Intent(MenuActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                    case R.id.activity_sponsor:
                        intent = new Intent(MenuActivity.this, SponsorActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                }

            }
        });
    }

    /**
     * Method for creating MenuOption objects and store in ArrayList
     */
    private void createMenuOptionList(){
        menuOptions = new ArrayList<>();
        menuOptions.add(new MenuOption("Företag", R.id.main));
        menuOptions.add(new MenuOption("Mässkarta", R.id.activity_map));
        menuOptions.add(new MenuOption("Mässchema", R.id.activity_schedule));
        menuOptions.add(new MenuOption("QR-läsare", R.id.activity_reader));
        menuOptions.add(new MenuOption("Sponsorer", R.id.activity_sponsor));

    }

}

