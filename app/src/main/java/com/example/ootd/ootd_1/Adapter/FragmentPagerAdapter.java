package com.example.ootd.ootd_1.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.ScrollView;

import com.example.ootd.ootd_1.Fragment.BookmarkFragment;
import com.example.ootd.ootd_1.Fragment.GridFragment;
import com.example.ootd.ootd_1.Fragment.ScrollFragment;


public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;
    public FragmentPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GridFragment tab1 = new GridFragment();
                return tab1;

            case 1:
                ScrollFragment tab2 = new ScrollFragment();
                return tab2 ;

            case 2:
                BookmarkFragment tab3 = new BookmarkFragment();
                return tab3;

            default:
                return null;
        }
    }

    @Override

    public int getCount() {
        return mPageCount;
    }
}
