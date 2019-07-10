package copops.com.copopsApp.pojo;

import java.io.Serializable;
/**
 * Created by Ranjan Gupta
 */
public class RegistationPojo implements Serializable {
    String status;
    String userid;
    String username;
    String email_id;
    String otp;
    String message;
    String verified;
    String profile_url;
    String level;
    String profile_percent;

    String id;
    String profile_qrcode;
    String total_reports;
    String completed_reports;
    String grade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }



    public String getProfile_qrcode() {
        return profile_qrcode;
    }

    public void setProfile_qrcode(String profile_qrcode) {
        this.profile_qrcode = profile_qrcode;
    }



    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getProfile_percent() {
        return profile_percent;
    }

    public void setProfile_percent(String profile_percent) {
        this.profile_percent = profile_percent;
    }

    public String getTotal_reports() {
        return total_reports;
    }

    public void setTotal_reports(String total_reports) {
        this.total_reports = total_reports;
    }

    public String getCompleted_reports() {
        return completed_reports;
    }

    public void setCompleted_reports(String completed_reports) {
        this.completed_reports = completed_reports;
    }



    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }



    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }







    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



 //   {"status":true,"userid":"5c012e95e5c1b","username":"kailash Karayat","otp":832379,"message":"Registration Successfull"}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
