package se.gosta.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;

import se.gosta.R;
import se.gosta.storage.Company;
import se.gosta.storage.Session;
import se.gosta.storage.Utils;

public class InfoActivity extends AppCompatActivity {

    private static final String LOG_TAG = InfoActivity.class.getSimpleName();

    private static final String DEFAULT_URL = "http://10.0.2.2:8080/resources/logos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        String compName;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                compName = null;
            } else {
                compName = extras.getString("companyName");
                // or: Session.getSession().currentCompanyName; ...
            }
        } else {
            compName= (String) savedInstanceState.getSerializable("companyName");
        }
        Company currentCompany = Session.get(compName);
        Session.setCurrentCompanyName(currentCompany.name());
        fetchLogo(currentCompany);
        Log.d(LOG_TAG, "Displaying company: " + currentCompany);



        ImageView iv = (ImageView) findViewById(R.id.logo);
        Bitmap bm = Utils.avatarBitmap(InfoActivity.this, currentCompany);
        iv.setImageBitmap(bm);
        Log.d(LOG_TAG, "Fetched logo of company: " + currentCompany.name());

        TextView tvTitle = (TextView) findViewById(R.id.companyTitle);
        tvTitle.setText(currentCompany.name());
        Log.d(LOG_TAG, "Title set: " + currentCompany.name());

        TextView tv = (TextView)findViewById(R.id.companyText);
        tv.setText(currentCompany.info());
        Log.d(LOG_TAG, "Description set: " + currentCompany.info());

        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_companies);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Session.setCurrentCompanyName(null);
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(InfoActivity.this, MapActivity.class);
                                startActivity(intent);
                               // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.action_schedule:
                                // intent = new Intent(StartActivity.this, ScheduleActivity.class);
                                // startActivity(intent);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(InfoActivity.this, MenuActivity.class);
                                startActivity(intent);
                                return true;

                        }
                        return false;
                    }
                });
    }

    public void fetchLogo(final Company company) {
        Log.d(LOG_TAG, "fetchLogos()");
        RequestQueue queue = Volley.newRequestQueue(InfoActivity.this);
        Log.d(LOG_TAG, " URL: " + DEFAULT_URL + company.fileName());
        String url = DEFAULT_URL + company.fileName();

        if ( ( url == null) || url.equals("null") ) {
            // Add default url??
            return;
        }
        Log.d(LOG_TAG, "download URL: " + url);

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d(LOG_TAG, "onResponse ok: " + bitmap.toString());
                try {
                    // Create a file from the bitmap
                    File f = Utils.createImageFile(InfoActivity.this, company, bitmap);
                    Log.d(LOG_TAG, " created file: " + f);
                } catch (IOException e) {
                    // Since we failed creating the file, we don't need to remove any
                    Log.d(LOG_TAG, " failed created file: " + e);
                    e.printStackTrace();
                    return;
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

}
