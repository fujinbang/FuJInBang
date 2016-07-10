package com.fujinbang.internet;

/**
 * Created by VITO on 2016/6/12.
 * Url常量
 */
public class UrlConstant {
    public static final String baseUrl = "http://120.24.240.199:6789";

    public static final String login = "http://120.24.240.199:6789/user/authentication"; //登录
    public static final String register = "http://120.24.240.199:6789/user"; //注册
    public static final String verificationCode = "http://120.24.240.199:6789/verification"; //验证码
    public static final String invitationCode = "http://120.24.240.199:6789/invitation"; //邀请码验证
    public static final String resetPassword = "http://120.24.240.199:6789/user/reset"; //忘记密码时重置密码

    public static final String queryUserInfo = "http://120.24.240.199:6789/user/queryuserinfo"; //搜索用户信息
    public static final String modifyUserInfo = "http://120.24.240.199:6789/user/modifyuserinfo";//修改用户信息
    public static final String queryFriend = "http://120.24.240.199:6789/user/friend/query"; //搜索帮友
    public static final String addFriend = "http://120.24.240.199:6789/user/friend/add"; //添加帮友

    public static final String queryHelp = "http://120.24.240.199:6789/helpquery"; //搜索用户参与的任务
    public static final String queryAttenders = "http://120.24.240.199:6789/help/queryattenders?helpid="; //获取任务的参与者
    public static final String finishMission = "http://120.24.240.199:6789/help/finish"; //完成任务
    public static final String dropMission = "http://120.24.240.199:6789/help/drop"; //放弃任务
    public static final String transferBonus = "http://120.24.240.199:6789/user/transfer"; //追加积分奖励
    public static final String urgeAttender = "http://120.24.240.199:6789/help/urge"; //催促接单者
    public static final String joinMission = "http://120.24.240.199:6789/joinhelp"; //接单、加入任务群组
    public static final String help = "http://120.24.240.199:6789/help"; //发布求助任务
    public static final String token = "http://120.24.240.199:6789/token"; //获取上传音频文件/图像文件的token

    public static final String sign = "http://120.24.240.199:6789/user/sign";//签到
    public static final String getSign = "http://120.24.240.199:6789/user/sign/get";//获取目前的签到状态等信息

    public static final String mall = "http://120.24.240.199:6789/user/autologinurl";//获取商城链接

    public static final String label = "http://120.24.240.199:6789/user/label";//与标签相关的操作

    public static final String qiniu = "http://o73gf55zi.bkt.clouddn.com/";//七牛存储相关
    public static final String qiniuUpload = "http://up.qiniu.com";//七牛上传

    public static final String broadcastSystemMsg = "http://120.24.240.199:6789/msg/broadcast";
    public static final String transMission = "http://120.24.240.199:6789/msg/transmission";
}
