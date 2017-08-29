package com.atrapapuntos.spiderman.OnBoarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atrapapuntos.spiderman.R;

/**
 * Created by Spiderman on 8/28/2017.
 */

public class OnBoardingFragment3 extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle s)
    {
        return inflater.inflate(
                R.layout.onboarding_screen3,
                container,
                false
        );
    }
}
