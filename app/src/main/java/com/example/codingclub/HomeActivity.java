package com.example.codingclub;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    Toolbar mTopToolbar;
    int numTabs = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTopToolbar = findViewById(R.id.my_toolbar);
        mTopToolbar.setTitle("Coding Club IITG");
        setSupportActionBar(mTopToolbar);

        //Initializing the tablayout
        //This is our tablayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        for(int i = 0; i < numTabs; i++){
            tabLayout.addTab(tabLayout.newTab());
        }


        //Initializing viewPager
        //This is our viewPager
        ViewPager viewPager = findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //Adding the tabs using addTab() method
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_white_48dp);
        tabLayout.getTabAt(0).setText("Home");
        tabLayout.getTabAt(1).setText("Events");
        tabLayout.getTabAt(2).setText("Projects");
        tabLayout.getTabAt(3).setText("Blogs");
        tabLayout.getTabAt(4).setText("About Us");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;

            case R.id.action_profile:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        setResult(RESULT_OK);
        finish();
    }
}