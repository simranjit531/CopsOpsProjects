package copops.com.copopsApp.pojo;

import java.io.Serializable;
/**
 * Created by Ranjan Gupta
 */
public class IncidentSetPojo implements Serializable {

    String incident_id;
    String device_language;

    public String getIncident_id() {
        return incident_id;
    }

    public void setIncident_id(String incident_id) {
        this.incident_id = incident_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    String device_id;

    public void setdevice_language(String device_language) {
        this.device_language = device_language;
    }

    public String getdevice_language() {
        return device_language;
    }
}
