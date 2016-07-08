package com.fujinbang.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fujinbang.R;
import com.fujinbang.ui.view.SwitchView;

/**
 * Created by Administrator on 2016/5/6.
 */
public class NeibourFragment extends Fragment {
    /**
     * 顶部侧滑选择按钮
     */
    private SwitchView switchView;

    private ImageView iv_order;

    private Fragment mMapFragment, mHelpListFragment;

    /**
     * 判断当前的页面显示的是否是地图 如果不是 则显示求助列表
     */
    //private boolean isMapFragment = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mMapFragment == null) mMapFragment = new MapFragment();
        if (mHelpListFragment == null) mHelpListFragment = new HelpListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neibour, container, false);

        Log.i("zy", "neibourFragment onCreateView");
        initContain();
        initImageView(view);
        initSwitchView(view);//初始化顶部选择按钮

        return view;
    }

    private final void initContain() {
        //if (!isMapFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.neibour_contain, mMapFragment);
        transaction.commit();
        //}
    }

    private final void initImageView(View view) {
        iv_order = (ImageView) view.findViewById(R.id.iv_neibour_order);
        iv_order.setVisibility(View.GONE);
    }

    private final void initSwitchView(View view) {
        switchView = (SwitchView) view.findViewById(R.id.switchView);
        switchView.setOnStatusChangeListener(new SwitchView.OnStatusChangeListener() {
            @Override
            public void onStatusChange(boolean isLeft) {
                FragmentTransaction transaction = NeibourFragment.this.getChildFragmentManager().beginTransaction();
                if (isLeft) {
                    //isMapFragment = true;
                    transaction.replace(R.id.neibour_contain, mMapFragment);
                    iv_order.setVisibility(View.GONE);
                } else {
                    //isMapFragment = false;
                    transaction.replace(R.id.neibour_contain, mHelpListFragment);
                    iv_order.setVisibility(View.VISIBLE);
                }
                transaction.commit();
            }
        });
    }
}
