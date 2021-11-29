package com.example.weatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.viewpagedemo.CircleIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ViewPagerWithCircleIndicatorView extends Fragment {
    FloatingActionButton btn_add;
    FloatingActionButton btn_delete;


    private MyFragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private Handler handler;
    private final int MESSAGE_WHAT_CHANGED = 100;

    private ArrayList<HashMap<String, Object>> mArrayList = null;
    private final String FRAGMENT = "fragment_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArrayList = new ArrayList<HashMap<String, Object>>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.viewpager_fragment, null);

        mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);
        btn_add = mView.findViewById(R.id.btn_add);
        btn_delete = mView.findViewById(R.id.btn_delete);
        mPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int pos) {
                        set(pos);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {

                    }
                });

        final CircleIndicatorView mCircleIndicatorView = (CircleIndicatorView) mView
                .findViewById(R.id.circleIndicatorView);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MESSAGE_WHAT_CHANGED:
                        mCircleIndicatorView.setCircleCount(mPagerAdapter
                                .getCount());
                        mCircleIndicatorView.setCircleSelectedPosition(mViewPager
                                .getCurrentItem());
                        mCircleIndicatorView.setSelectedCircleRadius(15);
                        mCircleIndicatorView.setCircleUnSelectedColor(Color.GRAY);
                        mCircleIndicatorView.drawCircleView();
//
//                        TestFragment tf = (TestFragment) mArrayList.get(mViewPager.getCurrentItem()).get(FRAGMENT);
//                        String title = tf.getTitle();


                        break;
                }
            }

            ;
        };


        // 初始化，在此，可选
        initialization();

        // 初始化选择第一项
        if (mPagerAdapter.getCount() > 0) {
            set(0);
        }
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionAdd();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArrayList.size() == 1) {
                    Toast.makeText(getContext(), "无法删除最后一个页面", Toast.LENGTH_SHORT).show();
                    return;
                }
                delete(mViewPager.getCurrentItem());
                set(mViewPager.getCurrentItem());
            }
        });
        return mView;
    }

    private void initialization() {
        // 在这里做初始化工作，如果有指定的Fragment，在此预装载

        // Fragment fragment;//创建一个Fragment
        // View view;//一个下方选项卡的View
        add(new HomeFragment());
        mPagerAdapter.notifyDataSetChanged();
    }

    private void add(Fragment fragment) {

        HashMap<String, Object> map = new HashMap<String, Object>();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        map.put(FRAGMENT, fragment);

        mArrayList.add(map);
    }


    private final void delete(int pos) {
        mArrayList.remove(pos);
        mPagerAdapter.notifyDataSetChanged();
    }

    private void set(int pos) {
        mViewPager.setCurrentItem(pos, true);
        handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return (Fragment) mArrayList.get(pos).get(FRAGMENT);
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentPagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            handler.sendEmptyMessage(MESSAGE_WHAT_CHANGED);
        }

    }

    private Fragment loadFragment() {
        return new TestFragment();
    }

    protected void onActionAdd() {
        add(loadFragment());
        mPagerAdapter.notifyDataSetChanged();
    }

    //动态新增的页面
    public static class TestFragment extends Fragment {

        private final String title = new Random().nextInt(20) + "";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            TextView tv = new TextView(getActivity());

            String str = "Fragment : " + title + "\n";
            for (int i = 0; i < 500; i++)
                str = str + i + "--";

            tv.setTextColor(Color.GRAY);
            tv.setText(str);

            return tv;
        }

        public String getTitle() {
            return title;
        }
    }
}