package copops.com.copopsApp.pojo;
/**
 * Created by Ranjan Gupta
 */
public class RecentChatHolder {
    private String id;
    private String sender_id;
    private String receiver_id;
    private String message;
    private String message_type;
    private String is_read;
    private String is_deleted;

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }

    private String unread;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    private String created_on;
    private String updated_on;
    private String user_id;
    private String user;
    private String time;


    public RecentChatHolder(String id,String sender_id,String receiver_id,String message,String message_type,String is_read,String is_deleted,String created_on, String updated_on,String user_id,String user,String time,String unread){
     this.id=id;
        this.sender_id=sender_id;
        this.receiver_id=receiver_id;
        this.message=message;
        this.message_type=message_type;
        this.is_read=is_read;
        this.is_deleted=is_deleted;
        this.created_on=created_on;
        this.updated_on=updated_on;
        this.user_id=user_id;
        this.user=user;
        this.time=time;
        this.unread=unread;



    }



}
