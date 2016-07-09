package com.fujinbang.helplist;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 对求助列表进行排序
 */
public class HelpListSort {
    private HelpListSort() {
    }

    /**
     * 根据距离x，y坐标的远近排序
     */
    public static List<HelpMsg> sortByDistance(List<HelpMsg> list, final LatLng pt) {
        Collections.sort(list, new Comparator<HelpMsg>() {
            @Override
            public int compare(HelpMsg a, HelpMsg b) {
                LatLng aPt = new LatLng(a.getX(), a.getY());
                LatLng bPt = new LatLng(b.getX(), b.getY());
                int aDis = (int) DistanceUtil.getDistance(aPt, pt);
                int bDis = (int) DistanceUtil.getDistance(bPt, pt);
                return aDis - bDis;
            }
        });
        return list;
    }

    /**
     * 根据积分排序
     */
    public static List<HelpMsg> sortByIntegration(List<HelpMsg> list) {
        Collections.sort(list, new Comparator<HelpMsg>() {
            @Override
            public int compare(HelpMsg a, HelpMsg b) {
                return b.getIntegration() - a.getIntegration();
            }
        });
        return list;
    }

    public static List<HelpMsg> sortByTime(List<HelpMsg> list) {
//        Collections.sort(list, new Comparator<HelpMsg>() {
//            @Override
//            public int compare(HelpMsg a, HelpMsg b) {
//                return 0;
//            }
//        });
        return list;
    }
}
