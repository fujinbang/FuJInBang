package com.fujinbang.share;

import android.content.Context;

import com.fujinbang.R;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ShareManager {
    private ShareManager() {

    }

    public static final int qq = 1;
    public static final int weixin = 2;
    public static final int sinaWeibo = 3;
    public static final int sms = 4;

    public static String getPlatformName(int platform) {
        switch (platform) {
            case qq:
                return QQ.NAME;
            case weixin:
                return Wechat.NAME;
            case sinaWeibo:
                return SinaWeibo.NAME;
            case sms:
                return ShortMessage.NAME;
            default:
                return null;
        }
    }

    public static void showShare(Context context, int platform) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        //oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("邀请好友");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //oks.setImageUrl(url);
        //url仅在微信（包括好友和朋友圈）中使用
        oks.setTitleUrl("www.baidu.com");
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        oks.setPlatform(getPlatformName(platform));
        // 启动分享GUI
        oks.show(context);
    }
}
