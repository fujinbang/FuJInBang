package com.fujinbang.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.fujinbang.R;
import com.fujinbang.helplist.HelpListSort;
import com.fujinbang.ui.activity.GroupHelpActivity;
import com.fujinbang.ui.activity.iactivity.IHelpListFragment;
import com.fujinbang.helplist.HelpMsg;
import com.fujinbang.baidumap.LocationManager;
import com.fujinbang.presenter.HelpListPresenter;
import com.fujinbang.presenter.ipresenter.IHelpListPresenter;
import com.fujinbang.ui.view.CircleImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.functions.Action1;

public class HelpListFragment extends Fragment implements IHelpListFragment {
    public final static String TAG = "HelpListFragment";
    /**
     * 求助列表
     */
    private PullToRefreshListView mHelpList;
    private HelpListAdapter mAdapter;
    private int helpMsgNum = 10;//期待的求助的列表的item数目
    private int visableNum = 0;//加载后显示的列表项的位置
    /**
     * 进度条动画
     */
    private RelativeLayout rl_progress;
    /**
     * 定位器
     */
    private LocationManager.OnLocationListener mLocationListener;
    private LocationManager mLocationManager;

    private IHelpListPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_list, container, false);

        initPresenter();
        initMenu();
        initProgressBar(view);
        initLocation();
        initListView(view);//必须在initProgressBar后调用
        return view;
    }

    private final void initPresenter() {
        presenter = new HelpListPresenter(this);
    }

    private final void initMenu() {
        ((NeibourFragment) getParentFragment()).getMenuObservable()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        List<HelpMsg> list = mAdapter.getList();
                        switch (s) {
                            case "最新发布":
                                list = HelpListSort.sortByTime(list);
                                mAdapter.updateList(list);
                                mAdapter.notifyDataSetChanged();
                                break;
                            case "离我最近":
                                list = HelpListSort.sortByDistance(list, mAdapter.getPos());
                                mAdapter.updateList(list);
                                mAdapter.notifyDataSetChanged();
                                break;
                            case "悬赏金额最高":
                                list = HelpListSort.sortByIntegration(list);
                                mAdapter.updateList(list);
                                mAdapter.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("zy", "HelpListFragment Menu error:" + throwable.toString());
                    }
                });
    }

    private final void initLocation() {
        mLocationManager = LocationManager.getInstance(getContext());
        mLocationListener = new LocationManager.OnLocationListener() {
            @Override
            public void onRecieveLocation(BDLocation location) {
                mLocationManager.removeOnLocationListener(this);
                //updateListContent(false);
            }
        };
    }

    private final void initProgressBar(View view) {
        rl_progress = (RelativeLayout) view.findViewById(R.id.rl_helplist_progress);
        showProgressBar();
    }

    private final void initListView(View view) {
        mAdapter = new HelpListAdapter(getContext(), presenter);
        mHelpList = (PullToRefreshListView) view.findViewById(R.id.lv_list_for_help);
        mHelpList.setMode(PullToRefreshBase.Mode.BOTH);
        mHelpList.setAdapter(mAdapter);
        mHelpList.requestFocus();
        mHelpList.setOnItemClickListener(mAdapter);

        mHelpList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                Log.i("zy", "HelpListFragment onPullDown");
                helpMsgNum = 10;
                visableNum = 0;
                presenter.getHelpList(helpMsgNum);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                Log.i("zy", "HelpListFragment onPullUp");
                helpMsgNum += 10;
                visableNum = mHelpList.getRefreshableView().getCount();
                mHelpList.getRefreshableView().getSelectedItemPosition();
                presenter.getHelpList(helpMsgNum);
            }
        });
        /**刷新列表的内容 默认初始的item数目为10*/
        helpMsgNum = 10;
        updateListContent(true);
    }

    @Override
    public Context getFragmentContext() {
        return getContext();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationManager.addOnLocationListener(mLocationListener);
        mLocationManager.requestRightNow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setHelpList(List<HelpMsg> list) {
        mAdapter.updateList(list);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void hideProgressBar() {
        rl_progress.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        rl_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateListContent(boolean isShowProgress) {
        if (isShowProgress) {
            showProgressBar();
        }
        presenter.getHelpList(helpMsgNum);
    }

    @Override
    public void clearList() {
        mAdapter.removeAll();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateAvatar(int id, Bitmap avatar) {
        mAdapter.updateAvatar(id, avatar);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePos(double x, double y) {
        mAdapter.updatePos(x, y);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showToast(String str) {
        if (getContext() != null)
            Toast.makeText(getContext().getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addHelpMsg(HelpMsg msg) {
        mAdapter.addHelpMsg(msg);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void completeRefresh() {
        mHelpList.onRefreshComplete();
        mHelpList.getRefreshableView().setSelection(visableNum);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GroupHelpActivity.isJoin(resultCode, data)) {
            if (GroupHelpActivity.getHelpId(data) != -1) {
                presenter.getHelpList(helpMsgNum);
                presenter.joinHelpList(GroupHelpActivity.getHelpId(data));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class HelpListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private List<HelpMsg> list;
        private Context mContext;
        /**
         * 用户当前位置
         */
        private LatLng myPos;

        private IHelpListPresenter presenter;

        public HelpListAdapter(Context context, List<HelpMsg> list, LatLng pos, IHelpListPresenter presenter) {
            this.list = list;
            this.mContext = context;
            this.myPos = pos;
        }

        public HelpListAdapter(Context context, IHelpListPresenter presenter) {
            list = new ArrayList<>();
            mContext = context;
            this.presenter = presenter;
        }

        public void updateList(List<HelpMsg> list) {
            this.list = list;
        }

        public List<HelpMsg> getList() {
            return list;
        }

        @Nullable
        public LatLng getPos() {
            return myPos;
        }

        public void updatePos(double x, double y) {
            myPos = new LatLng(x, y);
        }

        public void updateAvatar(int id, Bitmap Avatar) {
            if (id < list.size())
                list.get(id).setAvatar(Avatar);
        }

        public void removeAll() {
            list.clear();
        }

        public void addHelpMsg(HelpMsg msg) {
            list.add(msg);
        }

        @Override
        public int getCount() {
            if (list != null)
                return list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (list != null)
                return list.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder = new ViewHolder();

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.listview_item_helplist, null);

                holder.tv_name = (TextView) view.findViewById(R.id.tv_helplist_name);
                holder.tv_desc = (TextView) view.findViewById(R.id.tv_helplist_description);
                holder.tv_distance = (TextView) view.findViewById(R.id.tv_helplist_distance);
                holder.tv_intergration = (TextView) view.findViewById(R.id.tv_helplist_integration);
                holder.tv_needNum = (TextView) view.findViewById(R.id.tv_helplist_needNum);
                holder.tv_time = (TextView) view.findViewById(R.id.tv_helplist_time);
                holder.civ_avatar = (CircleImageView) view.findViewById(R.id.civ_helplist_avatar);
                holder.btn_accept = (TextView) view.findViewById(R.id.btn_helplist_accept);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (list != null) {
                final HelpMsg helpMsg = list.get(position);

                if (helpMsg.getAvatar() != null) {
                    holder.civ_avatar.setImageBitmap(helpMsg.getAvatar());
                } else {
                    holder.civ_avatar.setImageResource(R.drawable.bb);
                }
                holder.tv_name.setText(helpMsg.getUserName());
                holder.tv_time.setText(helpMsg.getStartTime() + " 至 " + helpMsg.getEndTime());
                holder.tv_intergration.setText(helpMsg.getIntegration() + "积分");
                holder.tv_distance.setText((int) DistanceUtil.getDistance(myPos, new LatLng(helpMsg.getX(), helpMsg.getY())) + "米");
                holder.tv_needNum.setText("还差： " + helpMsg.getNeedPeopleNum() + "人");
                holder.tv_desc.setText(helpMsg.getDescription());
            }
            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("zy", "HelpList onItemClick");
            final HelpMsg helpMsg = list.get(position - 1);
            presenter.showDetail(helpMsg);
        }

        class ViewHolder {
            CircleImageView civ_avatar;
            TextView tv_name, tv_distance, tv_needNum, tv_time, tv_desc, tv_intergration;
            TextView btn_accept;
        }
    }
}
