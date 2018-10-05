package com.example.s215087038.wefixx.rsa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s215087038.wefixx.R;

import java.util.ArrayList;
import java.util.List;

public class PlumbingFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public PlumbingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myFragmentView = inflater.inflate(R.layout.fragment_carpentry, container, false);


        ((ManageByCategory)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) myFragmentView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) myFragmentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return myFragmentView;
    }

    private void setupViewPager(ViewPager viewPager) {
        PlumbingFragment.ViewPagerAdapter adapter = new PlumbingFragment.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PlumbingAssignFragment(), "ASSIGN");
        adapter.addFragment(new PlumbingCloseFragment(), "CLOSE");

        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

