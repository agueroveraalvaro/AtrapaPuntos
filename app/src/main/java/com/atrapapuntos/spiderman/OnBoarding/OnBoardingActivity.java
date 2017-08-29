package com.atrapapuntos.spiderman.OnBoarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.atrapapuntos.spiderman.Login.LoginFragment;
import com.atrapapuntos.spiderman.Main.MainActivity;
import com.atrapapuntos.spiderman.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Spiderman on 8/28/2017.
 */

public class OnBoardingActivity extends FragmentActivity
{
    private ViewPager pager;
    private SmartTabLayout indicator;
    private Button skip;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (SmartTabLayout)findViewById(R.id.indicator);
        skip = (Button)findViewById(R.id.skip);
        next = (Button)findViewById(R.id.next);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return new OnBoardingFragment1();
                    case 1 : return new OnBoardingFragment2();
                    case 2 : return new OnBoardingFragment3();
                    case 3 : return new LoginFragment();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        skip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(pager.getCurrentItem() == 3)
                { // The last screen
                    pager.setCurrentItem(
                            pager.getCurrentItem() - 1,
                            true
                    );
                }
                else
                {
                    pager.setCurrentItem(3,true);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pager.getCurrentItem() == 3) { // The last screen
                    pager.setCurrentItem(
                            pager.getCurrentItem() - 1,
                            true
                    );
                } else {
                    pager.setCurrentItem(
                            pager.getCurrentItem() + 1,
                            true
                    );
                }
            }
        });
        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position)
            {
                if(position == 3)
                {
                    skip.setText("ATRÁS");
                    skip.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f));
                    next.setVisibility(View.GONE);
                }
                else
                {
                    skip.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,0.5f));
                    skip.setText("SKIP");
                    next.setVisibility(View.VISIBLE);
                    next.setText("SIGUIENTE");
                }
            }
        });
    }

    private void finishOnboarding() {
        // Get the shared preferences
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);

        // Set onboarding_complete to true
        preferences.edit()
                .putBoolean("onboarding_complete",true).apply();

        // Launch the main Activity, called MainActivity
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

        // Close the OnboardingActivity
        finish();
    }
}
