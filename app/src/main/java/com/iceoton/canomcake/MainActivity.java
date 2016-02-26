package com.iceoton.canomcake;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.iceoton.canomcake.adapter.PagerAdapter;
import com.iceoton.canomcake.ui.AccountFragment;
import com.iceoton.canomcake.ui.ProductFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    private final static String TAG = "MainActivity";
    private TextView titleBar;
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private PagerAdapter mPagerAdapter;
    // used to store app title
    private String[] titleList;

    /**
     * Maintains extrinsic info of a tab's construct
     */
    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }

    /**
     * A simple factory that returns dummy views to the Tabhost
     */
    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        /**
         * (non-Javadoc)
         *
         * @see TabHost.TabContentFactory#createTabContent(String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setCustomView(R.layout.custom_actionbar);
        mActionBar.setDisplayShowCustomEnabled(true);
        titleBar = (TextView)mActionBar.getCustomView().findViewById(R.id.text_title);

        titleList = getResources().getStringArray(R.array.menu_title);
        titleBar.setText(titleList[0]);

        // Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            //set the tab as per the saved state
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        // Initialise ViewPager
        this.initialiseViewPager();
    }

    /**
     * (non-Javadoc)
     *
     * @see FragmentActivity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab selected
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mTabHost.setCurrentTabByTag(savedInstanceState
                    .getString("tab")); // set the tab as per the saved state
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Initialise ViewPager
     */
    private void initialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, ProductFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, AccountFragment.class.getName()));
        this.mPagerAdapter = new PagerAdapter(
                super.getSupportFragmentManager(), fragments);
        this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager_content);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mTabHost.setCurrentTab(position);
        titleBar.setText(titleList[position]);
        Log.d(TAG, "current fragment id = " + mViewPager.getCurrentItem());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo;
        mTabHost.getTabWidget().setBackgroundResource(R.drawable.tab_indicator);
        AddTab(this, this.mTabHost, this.mTabHost
                        .newTabSpec("Product").setIndicator("", ContextCompat.getDrawable(this, R.drawable.product)),
                (tabInfo = new TabInfo("Product", ProductFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        AddTab(this, this.mTabHost, this.mTabHost
                        .newTabSpec("Account").setIndicator("", ContextCompat.getDrawable(this, R.drawable.account)),
                (tabInfo = new TabInfo("Account", AccountFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        mTabHost.setOnTabChangedListener(this);

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
    private void AddTab(MainActivity activity,
                        TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {

        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tag) {
        // TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    // Get back press work only at second press and notify user to press again
    // to exit.
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed(); // Exit
        } else {
            Toast.makeText(getBaseContext(), R.string.press_one_again,
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
