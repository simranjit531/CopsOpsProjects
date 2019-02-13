package copops.com.copopsApp.services;

import copops.com.copopsApp.pojo.AllLocationAndCityPojo;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.pojo.CityWsieMapShowPojo;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncedentAcceptResponse;
import copops.com.copopsApp.pojo.IncidentSubPojo;
import copops.com.copopsApp.pojo.IncidentTypePojo;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.pojo.RegistationPojo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by root on 30/11/17.
 */

public interface Service {


    @Multipart
    //@POST("/copops/public/api/auth/register")
    @POST("api/auth/register")
    Call<RegistationPojo> registationWithuploadFile(@Part MultipartBody.Part profile_image, @Part MultipartBody.Part id_card_front, @Part MultipartBody.Part id_card_back, @Part MultipartBody.Part business_card_front, @Part MultipartBody.Part business_card_back, @Part("data") RequestBody data);


    @Multipart
    //@POST("/copops/public/api/auth/login")
    @POST("api/auth/login")
    Call<RegistationPojo> userLogin(@Part("data") RequestBody data);
    @Multipart
    @POST("api/auth/password/reset")
    Call<CommanStatusPojo> userReset(@Part("data") RequestBody data);

    @Multipart
    @POST("api/auth/validate/otp")
    Call<CommanStatusPojo> callOtp(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth/profile/set/locations")
    Call<CommanStatusPojo> getlocations(@Part("data") RequestBody data);

  //  13.233.74.84/api/auth/notification/update
    @Multipart
    @POST("/api/auth/notification/update")
    Call<CommanStatusPojo> getupdate(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth/sub/incidents")
    Call<IncidentSubPojo> getIncedentSubTypeData(@Part("data") RequestBody data);
    @Multipart
    @POST("api/auth/incidents")
    Call<IncidentTypePojo> incidentType(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth/incident/list")
    Call<AllLocationAndCityPojo> getMapList(@Part("data") RequestBody data);




//    @Multipart
//    @POST("/api/auth/copincident/list")
//    Call<AssignmentListPojo> getAssignmentList(@Part("data") RequestBody data);

    @Multipart
    @POST("api/auth/copincident/status/list")
    Call<AssignmentListPojo> getAssignmentList(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth//profile/set/locations")
    Call<RegistationPojo> setLocations(@Part("data") RequestBody data);


    @Multipart
    @POST("public/api/auth/profile/set/availability")
    Call<CommanStatusPojo> getAvailability(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth/registered/incident/close")
    Call<IncedentAcceptResponse> close(@Part("data") RequestBody data,@Part MultipartBody.Part signature);


    @Multipart
    @POST("api/auth/registered/incident/assigned")
    Call<AssignmentListPojo> assignedData(@Part("data") RequestBody data);

    @Multipart
    @POST("/api/auth/incident/rejected")
    Call<CommanStatusPojo> rejected(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth/registered/incident/intervent")
    Call<CommanStatusPojo> acceptInterven(@Part("data") RequestBody data);

   // http://13.233.74.84/api/auth/check/freeze
    @Multipart
    @POST("api/auth/check/freeze")
    Call<CommanStatusPojo> freeze(@Part("data") RequestBody data);

    @Multipart
    @POST("api/auth/profile/attributes")
    Call<OperatorShowAlInfo> getOperotor(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth/incident/city/list")
    Call<CityWsieMapShowPojo> getMapListCity(@Part("data") RequestBody data);


    @Multipart
    @POST("api/auth/register/incident")
    Call<IncedentAcceptResponse> generateIncedent(@Part MultipartBody.Part incident_image,@Part MultipartBody.Part incident_video,@Part("data") RequestBody data);

    @Multipart
    @POST("api/auth/register/handrail")
    Call<IncedentAcceptResponse> generateHandrailSignature(@Part MultipartBody.Part signature,@Part MultipartBody.Part handrail_image,@Part MultipartBody.Part handrail_video,@Part("data") RequestBody data);

}
