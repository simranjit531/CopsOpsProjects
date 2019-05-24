package copops.com.copopsApp.services;

import copops.com.copopsApp.pojo.AllLocationAndCityPojo;
import copops.com.copopsApp.pojo.AssignmentListPojo;
import copops.com.copopsApp.pojo.CityWsieMapShowPojo;
import copops.com.copopsApp.pojo.CommanStatusPojo;
import copops.com.copopsApp.pojo.IncedentAcceptResponse;
import copops.com.copopsApp.pojo.IncidentSubPojo;
import copops.com.copopsApp.pojo.IncidentTypePojo;
import copops.com.copopsApp.pojo.OperatorShowAlInfo;
import copops.com.copopsApp.pojo.ProfilePojo;
import copops.com.copopsApp.pojo.RegistationPojo;
import copops.com.copopsApp.pojo.UserListPojo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by  Ranjan on 30/11/17.
 */

public interface Service {

    /**
     *  FOR USE CITIZEN AND OPERATOR REGISTATION
     * @param profile_image
     * @param id_card_front
     * @param id_card_back
     * @param business_card_front
     * @param business_card_back
     * @param data
     * @return
     */

    @Multipart
    //@POST("/copops/public/api/auth/register")
    @POST("api/auth/register")
    Call<RegistationPojo> registationWithuploadFile(@Part MultipartBody.Part profile_image, @Part MultipartBody.Part id_card_front, @Part MultipartBody.Part id_card_back, @Part MultipartBody.Part business_card_front, @Part MultipartBody.Part business_card_back, @Part("data") RequestBody data);

    /**
     * FOR USE CITIZEN AND OPERATOR LOGIN
     * @param data
     * @return
     */

    @Multipart
    //@POST("copops/public/api/auth/login")
    @POST("api/auth/login")
    Call<RegistationPojo> userLogin(@Part("data") RequestBody data);


    /**
     *   FOR USE CITIZEN AND OPERATOR RESET PASSWORD
     * @param data
     * @return
     */

    @Multipart
   // @POST("copops/public/api/auth/password/reset")
    @POST("api/auth/password/reset")
    Call<CommanStatusPojo> userReset(@Part("data") RequestBody data);


    /**
     *   FOR USE CITIZEN AND OPERATOR RESET PASSWORD FOR BOTH CITIZEN AND OPEROTOR
     * @param data
     * @return
     */


    @Multipart
    @POST("api/auth/validate/otp")
  //  @POST("copops/public/api/auth/validate/otp")
    Call<CommanStatusPojo> callOtp(@Part("data") RequestBody data);


    /**
     * FOR USE CITIZEN AND OPERATOR GETTING LOCATION
     * @param data
     * @return
     */

    @Multipart
    @POST("api/auth/profile/set/locations")
    //@POST("copops/public/api/auth/profile/set/locations")
    Call<CommanStatusPojo> getlocations(@Part("data") RequestBody data);


    /**
     * FOR USE CITIZEN AND OPERATOR CREATE INSIDENT AND UPDATE NOTIFICATION COUNT
     * @param data
     * @return
     */

    @Multipart
    @POST("api/auth/notification/update")
    //@POST("copops/public/api/auth/notification/update")
    Call<CommanStatusPojo> getupdate(@Part("data") RequestBody data);

    /**
     * FOR USE CITIZEN AND OPERATOR  INSIDENT SUBTYPE
     * @param data
     * @return
     */

//
    @Multipart
    @POST("api/auth/sub/incidents")
  //  @POST("copops/public/api/auth/sub/incidents")
    Call<IncidentSubPojo> getIncedentSubTypeData(@Part("data") RequestBody data);


    /**
     * FOR USE CITIZEN AND OPERATOR  INSIDENT TYPE
     * @param data
     * @return
     */

    @Multipart
    //@POST("copops/public/api/auth/incidents")
    @POST("api/auth/incidents")
    Call<IncidentTypePojo> incidentType(@Part("data") RequestBody data);

    /**
     * FOR USE CITIZEN AND OPERATOR  LIST OF CREATE INSIDENT
     * @param data
     * @return
     */

    @Multipart
    //@POST("copops/public/api/auth/incident/list")
    @POST("api/auth/incident/list")
    Call<AllLocationAndCityPojo> getMapList(@Part("data") RequestBody data);

    /**
     * for logout CITIZEN AND OPERATOR
     * @param data
     * @return
     */
    @Multipart
    //@POST("copops/public/api/auth/incident/list")
    @POST("api/auth/user/logout")
    Call<CommanStatusPojo> logout(@Part("data") RequestBody data);



 //   http://93.90.201.167/api/auth/user/update/medical

  //  parameter

//[user_id, medical_file]

    @Multipart
    //@POST("copops/public/api/auth/incident/list")
    @POST("api/auth/user/update/medical")
    Call<CommanStatusPojo> saveMedical(@Part("data") RequestBody data);

    /**
     *  FOR USE CITIZEN AND OPERATOR INSIDENT CREATE AND SHOW LIST
     * @param data
     * @return
     */


    @Multipart
    @POST("api/auth/copincident/status/list")
   // @POST("copops/public/api/auth/copincident/status/list")
    Call<AssignmentListPojo> getAssignmentList(@Part("data") RequestBody data);

    /**
     * FOR USE CITIZEN AND OPERATOR  SHOW LOACTION ON MAP ACCORDING TO CREATE INSIDENT
     * @param data
     * @return
     */


    @Multipart
   // @POST("copops/public/api/auth//profile/set/locations")
    @POST("api/auth//profile/set/locations")
    Call<RegistationPojo> setLocations(@Part("data") RequestBody data);



    /**
     * FOR USE OPERATOR  AVALIABIL OR NOT
     * @param data
     * @return
     */
    @Multipart
   // @POST("copops/public/api/auth/profile/set/availability")
    @POST("api/auth/profile/set/availability")
    Call<CommanStatusPojo> getAvailability(@Part("data") RequestBody data);


    /**
     * FOR USE CLOSE INSIDENT
     * @param data
     * @param signature
     * @return
     */
    @Multipart
    @POST("api/auth/registered/incident/close")
   // @POST("copops/public/api/auth/registered/incident/close")
    Call<IncedentAcceptResponse> close(@Part("data") RequestBody data,@Part MultipartBody.Part signature);


    /**
     * FOR USE ASSIGNED INSIDENT FOR OPERATOR
     * @param data
     * @return
     */

    @Multipart
  //  @POST("copops/public/api/auth/registered/incident/assigned")
    @POST("api/auth/registered/incident/assigned")
    Call<AssignmentListPojo> assignedData(@Part("data") RequestBody data);

    /**
     * FOR USE REJECTED INSIDENT TO  OPERATOR
     * @param data
     * @return
     */

    @Multipart
    //@POST("copops/public/api/auth/incident/rejected")
    @POST("api/auth/incident/rejected")
    Call<CommanStatusPojo> rejected(@Part("data") RequestBody data);


    /**
     * FOR USE INTERVEN INSIDENT TO  OPERATOR
     * @param data
     * @return
     */

    @Multipart
    @POST("api/auth/registered/incident/intervent")
   // @POST("copops/public/api/auth/registered/incident/intervent")
    Call<CommanStatusPojo> acceptInterven(@Part("data") RequestBody data);

   // http://13.233.74.84/api/auth/check/freeze

    /**
     * FOR USE OPERATOR FREEEZ
     * @param data
     * @return
     */
    @Multipart
   // @POST("copops/public/api/auth/check/freeze")
    @POST("api/auth/check/freeze")
    Call<CommanStatusPojo> freeze(@Part("data") RequestBody data);


    /**
     * FOr CHECK STATUS
     * @param data
     * @return
     */
    @Multipart
    //@POST("copops/public/api/auth/profile/attributes")
    @POST("api/auth/profile/attributes")
    Call<OperatorShowAlInfo> getOperotor(@Part("data") RequestBody data);

    /**
     * FOR USE INSIDENT CITY LIST
     * @param data
     * @return
     */
    @Multipart
   // @POST("copops/public/api/auth/incident/city/list")
    @POST("api/auth/incident/city/list")
    Call<CityWsieMapShowPojo> getMapListCity(@Part("data") RequestBody data);

  //  http://93.90.201.167/api/auth/user/profile
    @Multipart
    // @POST("copops/public/api/auth/incident/city/list")
    @POST("/api/auth/user/profile")
    Call<ProfilePojo> getProfile(@Part("data") RequestBody data);



    @Multipart
    // @POST("copops/public/api/auth/incident/city/list")
    @POST("/api/auth/user/update/profile")
    Call<RegistationPojo> updateProfile(@Part MultipartBody.Part profile_image,@Part("data") RequestBody data);



    @Multipart
    // @POST("copops/public/api/auth/incident/city/list")
    @POST("/api/auth/user/change/password")
    Call<CommanStatusPojo> changePassword(@Part("data") RequestBody data);


    /**
     *
     * @param incident_image
     * @param incident_video
     * @param data
     * @return
     */

    @Multipart
    //@POST("copops/public/api/auth/register/incident")
    @POST("api/auth/register/incident")
    Call<IncedentAcceptResponse> generateIncedent(@Part MultipartBody.Part incident_image,@Part MultipartBody.Part incident_video,@Part("data") RequestBody data);

    /**
     *
     * @param signature
     * @param handrail_image
     * @param handrail_video
     * @param data
     * @return
     */
    @Multipart
    //@POST("copops/public/api/auth/register/handrail")
    @POST("api/auth/register/handrail")
    Call<IncedentAcceptResponse> generateHandrailSignature(@Part MultipartBody.Part signature,@Part MultipartBody.Part handrail_image,@Part MultipartBody.Part handrail_video,@Part("data") RequestBody data);
    /**
     *
     * @param data
     * @return
     */

    @Multipart
    //@POST("chatdemo/public/api/auth/users")
    @POST("api/auth/users")
    Call<UserListPojo> userList(@Part("data") RequestBody data);

    /**
     *
     * @param upload_document
     * @return
     */

    @Multipart
   // @POST("copops/public/api/auth/upload/document")
    @POST("api/auth/upload/document")
    Call<CommanStatusPojo> uploadData(@Part MultipartBody.Part upload_document);


    /**
     *
     * @param data
     * @return
     */
     @Multipart
    //@POST("copops/public/api/auth/get/message")
     @POST("api/auth/get/message")
    Call<CommanStatusPojo> getMsgCount(@Part("data") RequestBody data);


}
