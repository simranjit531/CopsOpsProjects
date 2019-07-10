package copops.com.copopsApp.pojo;

import java.io.Serializable;
/**
 * Created by Ranjan Gupta
 */
public class IncedentAcceptResponse implements Serializable {
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    String message;
    String reference;
    String qrcode_url;

    public String getHelpline_number() {
        return helpline_number;
    }

    public void setHelpline_number(String helpline_number) {
        this.helpline_number = helpline_number;
    }

    String helpline_number;
}
