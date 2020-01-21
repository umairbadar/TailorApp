package com.example.tailorapp.Mens;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MenPagerAdapter extends FragmentPagerAdapter {

    public MenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                AllStylesMenFragment allStylesMenFragment = new AllStylesMenFragment();
                return allStylesMenFragment;

            case 1:
                EthnicWearMenFragment ethnicWearMenFragment = new EthnicWearMenFragment();
                return  ethnicWearMenFragment;

            case 2:
                FormalWearMenFragment formalWearMenFragment = new FormalWearMenFragment();
                return formalWearMenFragment;

            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "ALL STYLES";

            case 1:
                return "ETHNIC";

            case 2:
                return "FORMAL";

            default:
                return null;
        }

    }
}
