package com.example.alexandremguay.DDAfinalproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.astuetz.PagerSlidingTabStrip;
import com.example.alexandremguay.DDAfinalproject.fragements.MemoViewerFragment;
import com.example.alexandremguay.DDAfinalproject.fragements.RecordFragment;

/**
 * Created by fergu on 2017-12-3.
 *
 * REFERENCES:
 * https://github.com/dkim0419/SoundRecorder
 * https://github.com/codepath/android-audio-video-demo
 */


public class MemoRecorderActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "MemoRecorderActivity";
    private PagerSlidingTabStrip tabs;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_recorder);

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabs = findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("D.D.A");

        }
    }

    public class MyAdapter extends FragmentPagerAdapter {

        private String[] titles = { getString(R.string.tab_title_record),
                getString(R.string.tab_title_saved_recordings) };

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:{
                    return RecordFragment.newInstance(position);
                }
                case 1:{
                    return MemoViewerFragment.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public MemoRecorderActivity() {
    }



    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In OnStart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In OnResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In OnPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In OnStop()");
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In OnDestroy()");
    }

}
