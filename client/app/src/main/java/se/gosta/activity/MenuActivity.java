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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;



public class MenuActivity extends AppCompatActivity {

    private final String LOG_TAG = MenuActivity.class.getSimpleName();
    private ListAdapter adapter;
    private List<MenuOption>menuOptions;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        createMenuOptionList();

        ListView listView = (ListView) findViewById(R.id.menu_list);

        adapter =  new ArrayAdapter<MenuOption>(this,
                android.R.layout.simple_list_item_1,
                menuOptions);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    final View view,
                                    int position,
                                    long id) {
                Log.d(LOG_TAG, "item clicked, pos: " + position + "id: " + id);

                ListView listView = (ListView) findViewById(R.id.menu_list);
                MenuOption menuOption = (MenuOption) listView.getItemAtPosition(position);


                if(menuOption.menuNavigation == R.id.main){
                    Intent mainIntent = new Intent(MenuActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                } else if(menuOption.menuNavigation == R.id.activity_map){
                    Intent mapIntent = new Intent(MenuActivity.this, MapActivity.class);
                    startActivity(mapIntent);
                } else if(menuOption.menuNavigation == R.id.readerLayout){
                    Intent readerIntent = new Intent(MenuActivity.this, ReaderActivity.class);
                    startActivity(readerIntent);
                }else if(menuOption.menuNavigation == R.id.activity_schedule) {
                    Intent scheduleIntent = new Intent(MenuActivity.this, ScheduleActivity.class);
                    startActivity(scheduleIntent);
                }else if(menuOption.menuNavigation == R.id.activity_sponsor) {
                    Intent sponsorIntent = new Intent(MenuActivity.this, SponsorActivity.class);
                    startActivity(sponsorIntent);
                }


            }
        });
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
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MenuActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                 intent = new Intent(MenuActivity.this, ScheduleActivity.class);
                                 startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_settings:

                                return true;

                        }
                        return false;
                    }
                });

    }
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_menu);
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
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(MenuActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(MenuActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.action_settings:

                                return true;

                        }
                        return false;
                    }
                });
    }

    private void createMenuOptionList(){
        menuOptions = new ArrayList<>();
        menuOptions.add(new MenuOption("Företag", R.id.main));
        menuOptions.add(new MenuOption("Mässkarta", R.id.activity_map));
        menuOptions.add(new MenuOption("Mässchema", R.id.activity_schedule));
        menuOptions.add(new MenuOption("QR-läsare", R.id.readerLayout));
        menuOptions.add(new MenuOption("Sponsorer", R.id.activity_sponsor));

    }

}

