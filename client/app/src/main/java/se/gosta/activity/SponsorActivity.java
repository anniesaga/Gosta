package se.gosta.activity;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import se.gosta.R;

import se.gosta.storage.Company;
import se.gosta.storage.Event;
import se.gosta.storage.FairFetcher;
import se.gosta.storage.Sponsor;

public class SponsorActivity extends AppCompatActivity {

    private static final String LOG_TAG = SponsorActivity.class.getSimpleName();

    private Map<String, Sponsor> sponsorMap = new HashMap<>();

    /**
     * On creation of this activity it initiates an image view for displaying the sponsors logos.
     * It also implements an onClickListener for initiation of a popup window displaying information
     * about the sponsors in the case of the logos being touched.
     * Finally it initiates the bottom navigation bar with navigation options handled by a switch.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Running onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor);
        BottomNavigationView navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);
        navigation.setSelectedItemId(R.id.action_settings);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_companies:
                                Intent intent = new Intent(SponsorActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_map:
                                intent = new Intent(SponsorActivity.this, MapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_schedule:
                                intent = new Intent(SponsorActivity.this, ScheduleActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;
                            case R.id.action_settings:
                                intent = new Intent(SponsorActivity.this, MenuActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                return true;

                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FairFetcher fetcher = new FairFetcher(this);
        fetcher.registerFairListener(new FairFetcher.FairListener() {
            @Override
            public void companiesUpdated(List<Company> companyList) {
                // Do nothing with companies in this activity
            }


            @Override
            public void casesUpdated(Map<Integer, Integer[]> coords) {
                // Do nothing with cases in this activity

            }

            @Override
            public void eventsUpdated(List<Event> eventList) {
                //Do nothing with events in this activity
            }


            @Override
            public void sponsorsUpdated(Map<String, Sponsor> sponsors){

                Log.d(LOG_TAG, "cases: " + sponsors);

                // Puts all sponsors in a temporary hashmap and then to regular map.
                Map tmp = new HashMap(sponsors);
                tmp.keySet().removeAll(sponsorMap.keySet());
                sponsorMap.putAll(tmp);
                setupSponsors();
            }

        });
        // Call method in FairFetcher to download cases from db
        fetcher.getSponsors();

    }

    private void setupSponsors(){
        ImageView iv1 = (ImageView) findViewById(R.id.sponsor1);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sponsorMap.get(getString(R.string.sponsor1)) != null) {
                    initiatePopupWindow();
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupname)).setText(sponsorMap.get(getString(R.string.sponsor1)).sponsorName());
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupinfo)).setText(sponsorMap.get(getString(R.string.sponsor1)).sponsorInfo());
                    dimBehind(pw);
                }
            }
        });
        ImageView iv2 = (ImageView) findViewById(R.id.sponsor2);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sponsorMap.get(getString(R.string.sponsor2)) != null) {
                    initiatePopupWindow();
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupname)).setText(sponsorMap.get(getString(R.string.sponsor2)).sponsorName());
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupinfo)).setText(sponsorMap.get(getString(R.string.sponsor2)).sponsorInfo());
                    dimBehind(pw);
                }
            }
        });
        ImageView iv3 = (ImageView) findViewById(R.id.sponsor3);
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sponsorMap.get(getString(R.string.sponsor3)) != null) {
                    initiatePopupWindow();
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupname)).setText(sponsorMap.get(getString(R.string.sponsor3)).sponsorName());
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupinfo)).setText(sponsorMap.get(getString(R.string.sponsor3)).sponsorInfo());
                    dimBehind(pw);
                }
            }
        });
        ImageView iv4 = (ImageView) findViewById(R.id.sponsor4);
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sponsorMap.get(getString(R.string.sponsor4)) != null) {
                    initiatePopupWindow();
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupname)).setText(sponsorMap.get(getString(R.string.sponsor4)).sponsorName());
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupinfo)).setText(sponsorMap.get(getString(R.string.sponsor4)).sponsorInfo());
                    dimBehind(pw);
                }
            }
        });
        ImageView iv5 = (ImageView) findViewById(R.id.sponsor5);
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sponsorMap.get(getString(R.string.sponsor5)) != null) {
                    initiatePopupWindow();
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupname)).setText(sponsorMap.get(getString(R.string.sponsor5)).sponsorName());
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupinfo)).setText(sponsorMap.get(getString(R.string.sponsor5)).sponsorInfo());
                    dimBehind(pw);
                }
            }
        });
        ImageView iv6 = (ImageView) findViewById(R.id.sponsor6);
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sponsorMap.get(getString(R.string.sponsor6)) != null) {
                    initiatePopupWindow();
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupname)).setText(sponsorMap.get(getString(R.string.sponsor6)).sponsorName());
                    ((TextView) pw.getContentView().findViewById(R.id.sponspopupinfo)).setText(sponsorMap.get(getString(R.string.sponsor6)).sponsorInfo());
                    dimBehind(pw);
                }
            }
        });
    }

    private PopupWindow pw;

    /**
     * Initiates a popup window for displaying sponsors name and information and dismisses it if the
     * outside is touched.
     */

    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) SponsorActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.sponsorpopup,
                    (ViewGroup) findViewById(R.id.sponsorpopup));

            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.setAnimationStyle(R.style.popup_window_animation);
            pw.setOutsideTouchable(true);
            pw.setFocusable(true);
            pw.setTouchable(true);
            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);




        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * Dims the outside of the popup window.
     * @param pw The popup window for which this settings apply to
     */
    public static void dimBehind(PopupWindow pw) {
        View container = pw.getContentView().getRootView();
        Context context = pw.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);

    }

}
