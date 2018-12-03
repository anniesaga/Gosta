package se.gosta.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import se.gosta.R;


public class MapActivity extends AppCompatActivity{

    private final String TAG = MapActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        ImageView iv = (ImageView) findViewById(R.id.map);
        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_companies);
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
     /*   iv.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                int X =(int) event.getX();
                int Y = (int) event.getY();
                int eventaction = event.getAction();
                Context context = getApplicationContext();
                Rect r = new Rect(1030,1371,1109,1492);

                switch(eventaction){
                    case MotionEvent.ACTION_DOWN:
                        if(r.contains(X,Y)){
                            Toast.makeText(context, "Pressed!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "ACTION_DOWN AT COORDS " + "X: " + X + "Y: " + Y, Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        Toast.makeText(context, "ACTION_MOVE "+ "X: " + X + "Y: " + Y, Toast.LENGTH_SHORT).show();
                        return true;

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context, "ACTION_UP "+ "X: " + X + "Y: " + Y, Toast.LENGTH_SHORT).show();
                        return true;


                }

                return false;


            }
        });*/


    }

}

