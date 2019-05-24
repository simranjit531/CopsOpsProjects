package copops.com.copopsApp.pojo;

import java.io.Serializable;

public class ProfilePojo implements Serializable {

    private  String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    Message message;

   public  class Message implements Serializable {
    String id;

       String user_id;
       String first_name;
       String ref_user_type_id;
       String last_name;
       String date_of_birth;
       String place_of_birth;
       String gender;

       public String getMedical_file() {
           return medical_file;
       }

       public void setMedical_file(String medical_file) {
           this.medical_file = medical_file;
       }

       String medical_file;

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }

       public String getUser_id() {
           return user_id;
       }

       public void setUser_id(String user_id) {
           this.user_id = user_id;
       }

       public String getFirst_name() {
           return first_name;
       }

       public void setFirst_name(String first_name) {
           this.first_name = first_name;
       }

       public String getRef_user_type_id() {
           return ref_user_type_id;
       }

       public void setRef_user_type_id(String ref_user_type_id) {
           this.ref_user_type_id = ref_user_type_id;
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

       public String getGender() {
           return gender;
       }

       public void setGender(String gender) {
           this.gender = gender;
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

       public String getDevice_lang() {
           return device_lang;
       }

       public void setDevice_lang(String device_lang) {
           this.device_lang = device_lang;
       }

       public String getProfile_image() {
           return profile_image;
       }

       public void setProfile_image(String profile_image) {
           this.profile_image = profile_image;
       }

       public String getProfile_qrcode() {
           return profile_qrcode;
       }

       public void setProfile_qrcode(String profile_qrcode) {
           this.profile_qrcode = profile_qrcode;
       }

       public String getStatus() {
           return status;
       }

       public void setStatus(String status) {
           this.status = status;
       }

       String phone_number;
       String email_id;
       String device_lang;
       String profile_image;
       String profile_qrcode;
       String status;



   }
}
