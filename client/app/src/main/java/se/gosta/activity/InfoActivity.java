package se.gosta.activity;

import android.content.Intent;
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

import se.gosta.R;
import se.gosta.storage.Session;
import se.gosta.storage.Utils;

import static se.gosta.storage.Session.currentCompanyName;

/**
 * Activity that displays detailed information about a specific Company
 */
public class InfoActivity extends AppCompatActivity {

    private static final String LOG_TAG = InfoActivity.class.getSimpleName();

    private static final String DEFAULT_URL = "http://10.0.2.2:8080/resources/logos/";

    //   private static final String DEFAULT_URL = "http://192.168.43.128:8080/resources/logos/";

    /**
     * On creation of this activity it calls the method for setting up all detailed information
     * about the company and initiates the bottom navigation bar with navigation options
     * handled by a switch.
     * @param savedInstanceState Bundle containing the activity's previously frozen state, if there
     *      *                    was one.
     */
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

        //Sets up the navigation bar at the bottom of the screen
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

    /**
     * Method for fetching data from current companys variables and passing
     * the information to the designated view. Also registers onclick-listeners
     * for certain views.
     */
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

        //Sets website image clickable if company has URL
        if (!(Session.get(Session.currentCompanyName).website().equals("URL saknas"))) {
            Log.d(LOG_TAG, "Created clickable imgview: " + Session.get(Session.currentCompanyName).website());
            ImageView ivWeb = (ImageView) findViewById(R.id.webimg);
            ivWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Session.get(Session.currentCompanyName).website()));
                    Log.d(LOG_TAG, "Clicked website: " + Session.get(Session.currentCompanyName).website());
                    startActivity(intent);
                }
            });
        }

        TextView tv = (TextView)findViewById(R.id.companyText);
        tv.setText(Session.get(currentCompanyName).info());
        Log.d(LOG_TAG, "Description set: " + Session.get(currentCompanyName).info());

        // If-statements to check if company is recruiting etc and highlights the text if true
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
