package copops.com.copopsApp.pojo;

import java.io.Serializable;

public class LoginPojoSetData implements Serializable {


    String email_id;
    String otp;
    String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String    latitude;
    String  longitude;
    public String getIncident_lat() {
        return incident_lat;
    }

    public void setIncident_lat(String incident_lat) {
        this.incident_lat = incident_lat;
    }

    public String getIncident_lng() {
        return incident_lng;
    }

    public void setIncident_lng(String incident_lng) {
        this.incident_lng = incident_lng;
    }

    String incident_lat;
    String incident_lng;

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    String fcm_token;

    public String getRef_user_type_id() {
        return ref_user_type_id;
    }

    public void setRef_user_type_id(String ref_user_type_id) {
        this.ref_user_type_id = ref_user_type_id;
    }

    String ref_user_type_id;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }



    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    String device_id;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String user_password;
    String type;
    String device_language;

    public void setdevice_language(String device_language) {
        this.device_language = device_language;
    }
    public String getdevice_language() {
        return device_language;
    }
}
