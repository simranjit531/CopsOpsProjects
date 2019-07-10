package copops.com.copopsApp.utils;
/**
 * Created by Ranjan Gupta
 */
public class ChatHolder {
    private String message;
    String thumb;
    private String sender;
    private String receiver;
    private String message_type;


public ChatHolder(String message, String sender, String receiver, String message_type, String thumb){

    this.message=message;
    this.sender=sender;
    this.receiver=receiver;
    this.message_type=message_type;
    this.thumb=thumb;

}

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
    public String getMessage() {
        return message;
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }



    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }



}
