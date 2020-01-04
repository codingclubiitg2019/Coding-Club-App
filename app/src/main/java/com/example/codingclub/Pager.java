package com.example.codingclub;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Pager extends FragmentStatePagerAdapter {

    private HomeFragment tab0 = null;
    private EventsFragment tab1 = null;
    private ProjectsFragment tab2 = null;
    private BlogsFragment tab3 = null;
    private AboutusFragment tab4 = null;

    //integer to count number of tabs
    int tabCount;
//    private String[] tabTitles = new String[]{"Tab1", "Tab2", "Tab3"};

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                if(tab0 == null){
                    tab0 = new HomeFragment();
                }
                return tab0;
            case 1:
                if(tab1 == null){
                    tab1 = new EventsFragment();
                }
                return tab1;
            case 2:
                if(tab2 == null){
                    tab2 = new ProjectsFragment();
                }
                return tab2;
            case 3:
                if(tab3 == null){
                    tab3 = new BlogsFragment();
                }
                return tab3;
            case 4:
                if(tab4 == null){
                    tab4 = new AboutusFragment();
                }
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
