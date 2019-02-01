package copops.com.copopsApp.pojo;

import java.io.Serializable;

public class CommanStatusPojo implements Serializable {
    String status;

    public String getIsfreeze() {
        return isfreeze;
    }

    public void setIsfreeze(String isfreeze) {
        this.isfreeze = isfreeze;
    }

    String isfreeze;

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    String available;

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

    String message;
}
