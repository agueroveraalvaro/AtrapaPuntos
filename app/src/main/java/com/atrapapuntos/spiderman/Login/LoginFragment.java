package com.atrapapuntos.spiderman.Login;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atrapapuntos.spiderman.R;


public class LoginFragment extends Fragment
{

    private View v;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v=inflater.inflate(R.layout.login, container, false);
        iniciarTab();
        return v;
    }

    private void iniciarTab()
    {
        //Initializing the tablayout
        tabLayout = (TabLayout)v.findViewById(R.id.tabLayout);
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Ingresar"));
        tabLayout.addTab(tabLayout.newTab().setText("Registrarse"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing viewPager
        viewPager = (ViewPager)v.findViewById(R.id.pagerLogin);
        //Creating our pager adapter
        Pager adapter = new Pager(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Below lines of code shows you where to add that line
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static boolean validarDominio(String email)
    {
        String dominio=email.substring(email.indexOf('@')+1,email.length());
        System.out.println("33 DOMINIO "+dominio);
        String[] dominios=new String[5];
        dominios[0]="alumnos.duoc.cl";
        dominios[1]="gmail.com";
        dominios[2]="alumnos.uc.cl";
        dominios[3]="uchile.cl";
        dominios[4]="alumnis.sto.cl";
        for (int b=0;b<5;b++)
        {
            if (dominio.compareTo(dominios[b])==0)
            {
                return true;
            }
        }
        return false;
    }
}
