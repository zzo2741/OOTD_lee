package com.example.ootd.ootd_1.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.ootd.ootd_1.Fragment.BookmarkFragment;
import com.example.ootd.ootd_1.Fragment.GridFragment;
import com.example.ootd.ootd_1.Fragment.ScrollFragment;

import java.util.ArrayList;
import java.util.List;


public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;
    public int mCurPage;
    public String[] resultURLArray = null;

    GridFragment gridFragment = new GridFragment();
    ScrollFragment scrollFragment = new ScrollFragment();
    BookmarkFragment bookmarkFragment = new BookmarkFragment();

    //널값일 때
    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.d("널값일때", "잘돌아가?");
    }

    //값이 있을떄
    public FragmentPagerAdapter(FragmentManager fm, String[] result) {
        super(fm);
        //mCurPage = 0;
        //this.mPageCount = pageCount;
        Log.d("값있을때", "잘돌아가냐");
        this.resultURLArray = result;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("result", resultURLArray);
        switch (position) {
            case 0:
                gridFragment.setArguments(bundle);
                Log.d("FPA", position + "번째 받음");
                return gridFragment;
            case 1:
                scrollFragment.setArguments(bundle);
                Log.d("FPA", position + "번째 받음");
                return scrollFragment;
            case 2:
                bookmarkFragment.setArguments(bundle);
                Log.d("FPA", position + "번째 받음");
                return bookmarkFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}


/*@Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("result", resultURLArray);
        mFragmentList.get(position).setArguments(bundle);
        Log.d("FPA", position + "번째 받음");
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}*/




    /*private int mPageCount;
    String[] resultURLArray = null;
    private int mCurPage;
    GridFragment tab1 = new GridFragment();
    ScrollFragment tab2 = new ScrollFragment();
    public FragmentPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    public FragmentPagerAdapter(FragmentManager fm, int pageCount, String[] result) {
        super(fm);
        this.mPageCount = pageCount;
        this.resultURLArray = result;
        for(int i=0;i<resultURLArray.length;i++){
            Log.d("love", resultURLArray[i]);
        }
    }

    public void setResultArray(String[] arr) {
        this.resultURLArray = arr;
        tab1.changeDataSet(resultURLArray);
        tab2.changeDataSet(resultURLArray);

    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(resultURLArray != null){
                    System.out.println("1112");
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("result", resultURLArray);
                    Log.d("bundle", String.valueOf(bundle));
                    tab1.setArguments(bundle);
                    for(int i=0;i<resultURLArray.length;i++){
                        Log.d("what", resultURLArray[i]);
                    }

                    mCurPage = position;
                    System.out.println("1112");
                    return tab1;
                }else{
                    GridFragment tab1 = new GridFragment();
                    System.out.println("1111");
                    return tab1;
                }

            case 1:
                if(resultURLArray != null){
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("result", resultURLArray);
                    tab2.changeDataSet(resultURLArray);
                    mCurPage = position;
                    System.out.println("1113");
                    return tab2 ;
                }else{
                    ScrollFragment tab2 = new ScrollFragment();
                    System.out.println("2222");

                    return tab2 ;
                }

            case 2:
                BookmarkFragment tab3 = new BookmarkFragment();
                mCurPage = position;
                System.out.println("1114");

                return tab3;

            default:
                return null;
        }

    }

    @Override

    public int getCount() {
        return mPageCount;
    }
}*/
