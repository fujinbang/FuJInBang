package com.fujinbang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.fujinbang.R;
import com.fujinbang.global.SimpleDataBase;
import com.fujinbang.global.StatusBarCompat;
import com.fujinbang.internet.HttpConnRequest;
import com.fujinbang.internet.UrlConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class ContactActivity extends FragmentActivity {
    protected static Map<String, EaseUser> userMap = new Hashtable<>();
    protected EaseContactListFragment contactListFragment = new EaseContactListFragment();
    SimpleDataBase simpleDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        StatusBarCompat.compat(this);
        simpleDataBase = new SimpleDataBase(ContactActivity.this);
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                //跳转帮友信息界面
                UserActivity.startActivity(ContactActivity.this, user.getUserid(), true);
            }
        });
        contactListFragment.initRightTxt(R.drawable.add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactActivity.startActivity(ContactActivity.this);
            }
        });
        contactListFragment.setContactsMap(userMap);
        getSupportFragmentManager().beginTransaction().add(R.id.contact_contain, contactListFragment).commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initUserMap(simpleDataBase.getToken());
    }

    private void initUserMap(String token) {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try {
                    JSONObject object = new JSONObject();
                    object.put("token", params[0]);
                    result = HttpConnRequest.request(UrlConstant.queryFriend, "POST", object);
                } catch (Exception e){e.printStackTrace();}
                return result;
            }
            @Override
            protected void onPostExecute(String result){
                if (result!=null){
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.has("data")){
                            JSONArray data = object.getJSONArray("data");
                            for (int i = 0; i<data.length();i++){
                                JSONObject friend = data.getJSONObject(i);
                                if (userMap.containsKey(friend.getString("phonenum"))){
                                    continue;
                                }
                                EaseUser user = new EaseUser(friend.getString("phonenum"));
                                user.setNick(friend.getString("nickname"));
                                user.setUserid(friend.getInt("id"));
                                String avatar = "http://o73gf55zi.bkt.clouddn.com/"+friend.getInt("id")+".png";
                                user.setAvatar(avatar);
                                EaseCommonUtils.setUserInitialLetter(user);
                                userMap.put(friend.getString("phonenum"),user);
                            }
                            contactListFragment.refresh();
                        }
                    } catch (Exception e){e.printStackTrace();
                    }
                }
            }
        }.execute(token);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ContactActivity.class);
        context.startActivity(intent);
    }
}
