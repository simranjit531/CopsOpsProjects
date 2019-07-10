package copops.com.copopsApp.pojo;

import java.io.Serializable;
/**
 * Created by Ranjan Gupta
 */
public class RegistationPjoSetData implements Serializable {

    String gender;
    String first_name;
    String last_name;
    String date_of_birth;
    String device_id;
    String ref_user_type_id;
    String place_of_birth;
    String phone_number;
    String email_id;
    String user_password;
    String reg_latitude;
    String reg_longitude;
    String device_language;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String userid;

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    String fcm_token;
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }



    public String getRef_user_type_id() {
        return ref_user_type_id;
    }

    public void setRef_user_type_id(String ref_user_type_id) {
        this.ref_user_type_id = ref_user_type_id;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public void setPlace_of_birth(String place_of_birth) {
        this.place_of_birth = place_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }



    public String getReg_latitude() {
        return reg_latitude;
    }

    public void setReg_latitude(String reg_latitude) {
        this.reg_latitude = reg_latitude;
    }

    public String getReg_longitude() {
        return reg_longitude;
    }

    public void setReg_longitude(String reg_longitude) {
        this.reg_longitude = reg_longitude;
    }


    public String getdevice_language() {
        return device_language;
    }

    public void setdevice_language(String device_language) {
        this.device_language = device_language;
    }

//    'gender':'Male',
//            'first_name:'kailash',
//            'last_name:'Karayat',
//            'date_of_birth:'2018-01-01',
//            'place_of_birth:'Noida',
//            'phone_number':'1234567890',
//            'email_id':'test@test.com',
//            'user_password':'password'
}
