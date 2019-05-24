package copops.com.copopsApp.pojo;

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    private String created_at;
    private String updated_at;
    private String user_id;
    private String user;
    private String time;


    public RecentChatHolder(String id,String sender_id,String receiver_id,String message,String message_type,String is_read,String is_deleted,String created_at, String updated_at,String user_id,String user,String time,String unread){
     this.id=id;
        this.sender_id=sender_id;
        this.receiver_id=receiver_id;
        this.message=message;
        this.message_type=message_type;
        this.is_read=is_read;
        this.is_deleted=is_deleted;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.user_id=user_id;
        this.user=user;
        this.time=time;
        this.unread=unread;



    }



}
