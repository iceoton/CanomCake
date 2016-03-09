package com.iceoton.canomcake.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.iceoton.canomcake.R;
import com.iceoton.canomcake.activity.CartActivity;
import com.iceoton.canomcake.activity.MainActivity;
import com.iceoton.canomcake.adapter.PagerAdapter;
import com.iceoton.canomcake.util.CartManagement;

import java.util.List;
import java.util.Vector;

public class MainFragment extends Fragment {
    private final static String TAG = "DEBUG";
    private TextView titleBar, txtCountInCart;
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    // used to store app title
    private String[] titleList;

    /**
     * A simple factory that returns dummy views to the Tabhost
     */
    class TabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initialView(rootView, savedInstanceState);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        CartManagement cartManagement = new CartManagement(getActivity());
        cartManagement.loadCountInto(txtCountInCart);
    }

    private void initialView(View rootView, Bundle savedInstanceState) {
        ActionBar mActionBar = ((MainActivity)getActivity()).getSupportActionBar();
        View customView = getLayoutInflater(savedInstanceState).inflate(R.layout.custom_actionbar, null);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0,0);

        titleBar = (TextView)customView.findViewById(R.id.text_title);
        txtCountInCart = (TextView) customView.findViewById(R.id.text_count);
        FrameLayout containerCart = (FrameLayout) customView.findViewById(R.id.container_cart);
        containerCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                getActivity().startActivity(intent);
            }
        });

        titleList = getResources().getStringArray(R.array.menu_title);
        titleBar.setText(titleList[0]);

        this.initialiseTabHost(rootView, savedInstanceState);
        if (savedInstanceState != null) {
            //set the tab as per the saved state
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        this.initialiseViewPager(rootView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab selected
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.mTabHost.setCurrentTabByTag(savedInstanceState
                    .getString("tab")); // set the tab as per the saved state
        }
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(View rootView, Bundle args) {
        mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabHost.getTabWidget().setBackgroundResource(R.drawable.tab_indicator);

        addTab(this.mTabHost.newTabSpec("Category")
                .setIndicator("", ContextCompat.getDrawable(getContext(), R.drawable.product)));

        addTab(this.mTabHost.newTabSpec("Account")
                .setIndicator("", ContextCompat.getDrawable(getContext(), R.drawable.account)));

        mTabHost.setOnTabChangedListener(tabChangeListener);

        TabWidget widget = mTabHost.getTabWidget();
        for (int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            Log.d(TAG, "Child count of TabWidget = " + widget.getChildCount());
            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView) v.findViewById(android.R.id.title);
            if (tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_indicator);
        }
    }

    /**
     * Add Tab content to the TabHost
     */
    private void addTab(TabHost.TabSpec tabSpec) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(new TabFactory(getActivity()));
        mTabHost.addTab(tabSpec);
    }

    TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            // TabInfo newTab = this.mapTabInfo.get(tag);
            int pos = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(pos);
        }
    };


    /**
     * Initialise ViewPager
     */
    private void initialiseViewPager(View rootView) {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(CategoryFragment.newInstance());
        fragments.add(AccountFragment.newInstance());
        this.mPagerAdapter = new PagerAdapter(
                getChildFragmentManager(), fragments);
        this.mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager_content);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mTabHost.setCurrentTab(position);
            titleBar.setText(titleList[position]);
            Log.d(TAG, "current fragment id = " + mViewPager.getCurrentItem());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
