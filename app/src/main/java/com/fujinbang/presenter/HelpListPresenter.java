package com.fujinbang.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.fujinbang.mission.MissionModel;
import com.fujinbang.ui.activity.GroupHelpActivity;
import com.fujinbang.ui.activity.iactivity.IHelpListFragment;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.helplist.HelpMsg;
import com.fujinbang.internet.HelpListRequest;
import com.fujinbang.internet.InternetRequest;
import com.fujinbang.presenter.ipresenter.IHelpListPresenter;


import java.util.HashMap;
import java.util.HashSet;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/6/1.
 */
public class HelpListPresenter implements IHelpListPresenter {

    private IHelpListFragment view;
    private HelpListRequest request;
    private SimpleDataBase db;

    public HelpListPresenter(IHelpListFragment view) {
        this.view = view;
        request = new HelpListRequest(view.getFragmentContext());
        db = new SimpleDataBase(view.getFragmentContext());
    }

    /**
     * 接单：加入对应的求助任务
     *
     * @param helpId 求助信息的id
     */
    @Override
    public void joinHelpList(int helpId) {
        Log.i("zy", "HelpListPresenter joinHelpList");
        request.joinHelp(db.getToken(), helpId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HashMap<String, Object>>() {
                    @Override
                    public void onCompleted() {/*接单成功*/
                        new MissionModel(view.getFragmentContext()).addPickMission();
                        view.updateListContent(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToast("加入任务失败");
                        Log.e("zy", "join help:" + e);
                    }

                    @Override
                    public void onNext(HashMap<String, Object> hashMap) {

                    }
                });
    }

    /**
     * 获取坐标（x，y）附近的求助列表，数量为limited
     * * <p>
     * 1.获取x，y附近的所有求助信息 all_list（剔除重复）
     * 2.获取自己参与和自己发布的求助信息 attend_list 和 post_list
     * 3.取差集：list = all_list - attend_list - post_list
     * 4.获取用户的姓名 for id in list -> name
     * 5.获取用户的头像 for id in list -> avatar.png
     * 6.在主线程显示出来
     */
    @Override
    public void getHelpList(int num) {
        final float x = (float) db.getLocationX();
        final float y = (float) db.getLocationY();
        final int limit = num;
        Log.i("zy", "getHelpList:" + x + " , " + y);

        Observable<HelpMsg> helpMsgObservable = request.getUserHelpList(db.getPhoneNum())/* 获取用户参与的求助任务 */
                .flatMap(new Func1<HashSet<Integer>, Observable<HelpMsg>>() {
                    @Override
                    public Observable<HelpMsg> call(HashSet<Integer> hashSet) {/* 获取附近的求助任务 并 减去用户参与的 */
                        return request.getHelpList(x, y, limit, hashSet);
                    }
                })
                .flatMap(new Func1<HelpMsg, Observable<HelpMsg>>() {
                    @Override
                    public Observable<HelpMsg> call(HelpMsg helpMsg) {/* 通过求助任务获取发布人的用户名 */
                        return request.getUserName(helpMsg);
                    }
                });

        helpMsgObservable
                .observeOn(AndroidSchedulers.mainThread())/* 在主线程显示出来 */
                .subscribe(new Subscriber<HelpMsg>() {
                    /**
                     * 判断是否是Observer的第一个onNext
                     */
                    boolean isFirst = true;

                    @Override
                    public void onStart() {
                        isFirst = true;
                        view.updatePos(x, y);
                    }

                    @Override
                    public void onCompleted() {
                        view.hideProgressBar();
                        view.completeRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgressBar();
                        view.completeRefresh();
                        Log.e("zy", "getHelpList error:" + e.toString());
                    }

                    @Override
                    public void onNext(HelpMsg helpMsg) {
                        if (isFirst) {
                            view.clearList();
                            view.hideProgressBar();
                            isFirst = false;
                        }
                        view.addHelpMsg(helpMsg);
                    }
                });

        helpMsgObservable
                .observeOn(AndroidSchedulers.mainThread())/* 通过求助任务获取发布人的头像 */
                .subscribe(new Action1<HelpMsg>() {
                    @Override
                    public void call(final HelpMsg helpMsg) {
                        request.getAvatar(helpMsg.getNeederId(), new InternetRequest.OnResponseListener<Bitmap>() {
                            @Override
                            public void onSuccess(Bitmap avatar) {
                                view.updateAvatar(helpMsg.getPos(), avatar);
                            }

                            @Override
                            public void onError(String desc, int statusCode) {
                                Log.e("zy", desc);
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("zy", throwable.toString());
                    }
                });
    }

    @Override
    public void showDetail(HelpMsg msg) {
        String desc = msg.getDescription();
        String startTime = msg.getStartTime();
        String endTime = msg.getEndTime();
        int integration = msg.getIntegration();
        int needNum = msg.getNeedPeopleNum();
        String sex = " ";
        double x = msg.getX();
        double y = msg.getY();
        int id = msg.getId();

        Log.d("zy", "show detail: " + desc + " " + startTime + " " + endTime + " " + integration + " " + sex);
        GroupHelpActivity.startActivity(view.getFragment(), desc, startTime, endTime, integration, needNum, sex, x, y, id);
    }
}