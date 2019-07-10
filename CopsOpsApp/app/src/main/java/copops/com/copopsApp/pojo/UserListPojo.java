package copops.com.copopsApp.pojo;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Ranjan Gupta
 */
public class UserListPojo implements Serializable {

    String status;

    public ArrayList<Response> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Response> response) {
        this.response = response;
    }

    ArrayList<Response> response;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



   public class Response {
       String first_name;

       public String getFirst_name() {
           return first_name;
       }

       public void setFirst_name(String first_name) {
           this.first_name = first_name;
       }

       public String getUser_id() {
           return user_id;
       }

       public void setUser_id(String user_id) {
           this.user_id = user_id;
       }

       public String getLast_name() {
           return last_name;
       }

       public void setLast_name(String last_name) {
           this.last_name = last_name;
       }

       String user_id;
       String last_name;

       public String getId() {
           return id;
       }

       public void setId(String id) {
           this.id = id;
       }

       String id;
    }
}
