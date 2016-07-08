package com.fujinbang.internet.IRequest;


import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/6/1.
 */
public interface IHelpListRequest {
    @GET("/help")
    Observable<HashMap<String, Object>> getHelpList(@Query("x") float x, @Query("y") float y, @Query("limit") int limit);

    @POST("/helpquery")
    Observable<HashMap<String, Object>> getUserHelpList(@Body phoneNum phonenum);

    @POST("/user/queryuserinfo")
    Observable<HashMap<String, Object>> getUserMsg(@Body ID id);

    @POST("/joinhelp")
    Observable<HashMap<String, Object>> joinHelp(@Body JH joinHelp);

    class phoneNum {
        public String phonenum;

        public phoneNum(String phonenum) {
            this.phonenum = phonenum;
        }
    }

    class ID {
        public int id;

        public ID(int id) {
            this.id = id;
        }
    }

    class JH {
        public String token;
        public int helpinfoid;

        public JH(String token, int helpinfoid) {
            this.token = token;
            this.helpinfoid = helpinfoid;
        }
    }
}
