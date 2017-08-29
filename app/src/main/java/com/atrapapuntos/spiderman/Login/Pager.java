package com.atrapapuntos.spiderman.Login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Spiderman on 8/1/2017.
 */

public class Pager extends FragmentStatePagerAdapter
{
    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount)
    {
        super(fm);
        //Initializing tab count
        this.tabCount=tabCount;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                IngresarTabFragment tab1=new IngresarTabFragment();
                tabCount=1;
                return tab1;
            case 1:
                RegistrarTabFragment tab2 = new RegistrarTabFragment();
                tabCount=2;
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
