package com.example.administrator.learning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import navigation.AHExtendedSlidingTabBar;
import navigation.AHViewPagerTabBar;
import navigation.ToastUtils;

public class testActivity extends AppCompatActivity {

    private AHExtendedSlidingTabBar bar;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        bar = (AHExtendedSlidingTabBar) findViewById(R.id.ah_nav_bar7);
        ImageView rightMenu = new ImageView(this);
        // rightMenu.setId(R.id.right_menu);
        rightMenu.setImageResource(R.drawable.icon_menu);
        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(testActivity.this,"自定义菜单");
                viewPager.setCurrentItem(2);
            }
        });
        bar.setRightExtensionView(rightMenu);
        AHViewPagerTabBar slidingTabBar = new AHViewPagerTabBar(this);
        bar.setSlidingTabBar(slidingTabBar);
        viewPager = (ViewPager) findViewById(R.id.ah_nav_view_pager);

        generateFragmentsFromConfig2(getSupportFragmentManager(), slidingTabBar, viewPager, 0);
        slidingTabBar.setViewPager(viewPager);
        viewPager.setCurrentItem(0);

        slidingTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
               // Fragment mFragment = ((BaseFragmentPagerAdapter) viewPager.getAdapter()).getFragments().get(position);
                ToastUtils.showMessage(testActivity.this, ((BaseFragmentPagerAdapter) viewPager.getAdapter()).getPageTitle(position).toString(),true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void generateFragmentsFromConfig2(FragmentManager fm, AHViewPagerTabBar tabs, ViewPager viewPager, int lastPosition) {
        List<String> titles = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            titles.add("title" + i);
        }

        final List<Fragment> fragments = new ArrayList<>();
     /*   for (String title : titles) {
            fragments.add(generateFragment(title));
        }*/

            Fragment fragment  = new testFragment1();
            Fragment fragment2 = new testFragment2();
            Fragment fragment3 = new testFragment3();
            Fragment fragment4 = new testFragment4();
            fragments.add(fragment);
            fragments.add(fragment2);
            fragments.add(fragment3);
            fragments.add(fragment4);


        BaseFragmentPagerAdapter adapter = (BaseFragmentPagerAdapter) viewPager.getAdapter();
        if (adapter == null) {
            adapter = new BaseFragmentPagerAdapter(fm, titles, fragments);
            viewPager.setAdapter(adapter);
        } else {
            CharSequence title = adapter.getPageTitle(lastPosition);
            adapter.setListFragments(titles, fragments);
            int p = position(title, titles);
            viewPager.setCurrentItem(p, false);
            tabs.getAdapter().notifyDataSetChanged();
        }
    }

    private int position(CharSequence title, List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).equals(title)) {
                return i;
            }
        }
        return 0;
    }

    private Fragment generateFragment(String title) {
        Fragment fragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", title);
        fragment.setArguments(bundle);
        return fragment;
    }

}