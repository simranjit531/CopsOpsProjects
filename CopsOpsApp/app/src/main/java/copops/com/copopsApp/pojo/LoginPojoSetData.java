package copops.com.copopsApp.pojo;

public class LoginPojoSetData {


    String email_id;
    String otp;

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

}
