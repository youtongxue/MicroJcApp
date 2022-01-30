package com.service.microjc.InterFace;

import com.service.microjc.stType.ExamInfo;
import com.service.microjc.stType.JwUserInfo;
import com.service.microjc.stType.LoginInfo;
import com.service.microjc.stType.PureInfo;
import com.service.microjc.stType.RequestRoomInfo;
import com.service.microjc.stType.RoomResultInfo;
import com.service.microjc.stType.ScoresInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JwApi {
    //访问登录页面
    @GET("/microjc/jwloginbefore")
    Call<LoginInfo> getLoginInfo();

    //登录
    @Headers({"Content-Type: application/json","Accept: application/json"})//添加header表明参数是json格式的
    @POST("/microjc/jwlogin")
    Call<JwUserInfo>  getLoginStatus(@Body LoginInfo loginInfo);

    //查询成绩
    @Headers({"Content-Type: application/json","Accept: application/json"})//添加header表明参数是json格式的
    @POST("/microjc/getscrodinfo")
    Call<ScoresInfo> getScoresInfo(@Body LoginInfo loginInfo);

    //考试安排查询
    @Headers({"Content-Type: application/json","Accept: application/json"})//添加header表明参数是json格式的
    @POST("/microjc/getexaminfo")
    Call<ExamInfo> getExamInfo(@Body LoginInfo loginInfo);

    //补考考试安排查询
    @Headers({"Content-Type: application/json","Accept: application/json"})//添加header表明参数是json格式的
    @POST("/microjc/getexamagaininfo")
    Call<ExamInfo> getSecondExamInfo(@Body LoginInfo loginInfo);

    //获取 课表 html
    @Headers({"Content-Type: application/json","Accept: application/json"})//添加header表明参数是json格式的
    @POST("/microjc/getkbhtml")
    Call<ResponseBody> getKbHtml(@Body LoginInfo loginInfo);

    //获取 空教室
    @Headers({"Content-Type: application/json","Accept: application/json"})//添加header表明参数是json格式的
    @POST("/microjc/getroomhtml")
    Call<RoomResultInfo> getRoom(@Body RequestRoomInfo requestRoomInfo);

}
