package com.example.administrator.learning;

/**
 * Created by Administrator on 2016/12/29.
 */

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;



/**
 * ViewPager中使用的FragmentPagerAdapter
 *
 * @date:2014-3-14
 */
public class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitles;
    private List<? extends Fragment> mListFragments;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<String> titles, List<? extends Fragment> listFragments) {
        super(fm);
        this.mTitles = titles;
        this.mListFragments = listFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    public void setListFragments(List<? extends Fragment> mListFragments) {
        this.mListFragments = mListFragments;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub
        super.restoreState(arg0, arg1);
    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }


    public void setTitles(List<String> mTitles) {
        this.mTitles = mTitles;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public List<String> getTitles() {
        return mTitles;
    }

    public List<? extends Fragment> getFragments() {
        return mListFragments;
    }

    public void setListFragments(List<String> titles, List<? extends Fragment> fragments) {
        if (mTitles != null && mListFragments != null) {
            mTitles.clear();
            mTitles = null;
            mTitles = titles;

            mListFragments.clear();
            mListFragments = null;
            mListFragments = fragments;
            notifyDataSetChanged();
        }
    }

}

