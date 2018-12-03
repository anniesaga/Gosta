package se.gosta.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import se.gosta.R;


public class MapActivity extends AppCompatActivity{

    private final String TAG = MapActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        ImageView iv = (ImageView) findViewById(R.id.map);
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

