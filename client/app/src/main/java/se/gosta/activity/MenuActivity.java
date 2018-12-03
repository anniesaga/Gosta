/*package se.gosta.activity;

import se.gosta.R;
import se.gosta.storage.MenuOption;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


                if(menuOption.menuNavigation == R.id.navcontainer){
                    Intent mainIntent = new Intent(MenuActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                } else if(menuOption.menuNavigation == R.id.activity_map){
                    Intent mapIntent = new Intent(MenuActivity.this, MapActivity.class);
                    startActivity(mapIntent);
                } else if(menuOption.menuNavigation == R.id.readerLayout){
                    Intent readerIntent = new Intent(MenuActivity.this, ReaderActivity.class);
                    startActivity(readerIntent);
                }



            }
        });

    }

    private void createMenuOptionList(){
        menuOptions = new ArrayList<>();
        menuOptions.add(new MenuOption("Företag", R.id.navcontainer));
        menuOptions.add(new MenuOption("Mässkarta", R.id.activity_map));
        menuOptions.add(new MenuOption("Mässchema", R.id.navcontainer));
        menuOptions.add(new MenuOption("QR-läsare", R.id.readerLayout));

    }

}
*/