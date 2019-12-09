package com.example.codingclub;

import android.util.EventLog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
//    private String[] tabTitles = new String[]{"Tab1", "Tab2", "Tab3"};

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitles[position];
//    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                HomeFragment tab0 = new HomeFragment();
                return tab0;
            case 1:
                EventsFragment tab1 = new EventsFragment();
                return tab1;
            case 2:
                ProjectsFragment tab2 = new ProjectsFragment();
                return tab2;
            case 3:
                BlogsFragment tab3 = new BlogsFragment();
                return tab3;
            case 4:
                AboutusFragment tab4 = new AboutusFragment();
                return tab4;
                default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
