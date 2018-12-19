package se.gosta.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
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

import static se.gosta.storage.Session.currentCompanyName;

public class InfoActivity extends AppCompatActivity {

    private static final String LOG_TAG = InfoActivity.class.getSimpleName();

    private static final String DEFAULT_URL = "http://10.0.2.2:8080/resources/logos/";

 //   private static final String DEFAULT_URL = "http://192.168.43.128:8080/resources/logos/";

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

        Session.setCurrentCompanyName(compName);

        setupInfo();
        Log.d(LOG_TAG, "Displaying company: " + Session.get(currentCompanyName));

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
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(InfoActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(InfoActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(InfoActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });
    }

    private void setupInfo() {
        ImageView iv = (ImageView) findViewById(R.id.logo);
        if(Utils.avatarExists(InfoActivity.this, Session.get(currentCompanyName))) {
            iv.setImageBitmap(Utils.avatarBitmap(InfoActivity.this, Session.get(currentCompanyName)));
            Log.d(LOG_TAG, "Fetched logo of company: " + Session.get(currentCompanyName).name());
        }

        TextView tvTitle = (TextView) findViewById(R.id.companyTitle);
        tvTitle.setText(Session.get(currentCompanyName).name());
        Log.d(LOG_TAG, "Title set: " + Session.get(currentCompanyName).name());

        TextView tvCase = (TextView) findViewById(R.id.companyCaseNo);
        tvCase.setText("Monter: " + Session.get(currentCompanyName).caseNo());
        Log.d(LOG_TAG, "Case set: " + Session.get(currentCompanyName).caseNo());

        TextView tvContact = (TextView) findViewById(R.id.companyContact);
        tvContact.setText(Session.get(currentCompanyName).contact());
        Log.d(LOG_TAG, "Contact set: " + Session.get(currentCompanyName).contact());

        ImageView ivMail = (ImageView) findViewById(R.id.mailimg);
        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Session.get(Session.currentCompanyName).email()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        ImageView ivWeb = (ImageView) findViewById(R.id.webimg);
        ivWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.google.com"));
                //TODO: Sätt varje företags URL istället för google.com
                startActivity(intent);
            }
        });

        TextView tv = (TextView)findViewById(R.id.companyText);
        tv.setText(Session.get(currentCompanyName).info());
        Log.d(LOG_TAG, "Description set: " + Session.get(currentCompanyName).info());

        TextView recruit = (TextView) findViewById(R.id.recruit);
        if(Session.get(currentCompanyName).isRecruiting()){
            recruit.setTextColor(getResources().getColor(R.color.red));
        } else {
            recruit.setTextColor(getResources().getColor(R.color.grey));
        }
        TextView partTime = (TextView) findViewById(R.id.parttime);
        if(Session.get(currentCompanyName).hasPartTime()){
            partTime.setTextColor(getResources().getColor(R.color.red));
        } else {
            partTime.setTextColor(getResources().getColor(R.color.grey));
        }
        TextView thesis = (TextView) findViewById(R.id.thesis);
        if(Session.get(currentCompanyName).hasThesis()){
            thesis.setTextColor(getResources().getColor(R.color.red));
        } else {
            thesis.setTextColor(getResources().getColor(R.color.grey));
        }
    }

}
