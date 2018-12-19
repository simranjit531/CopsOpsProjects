package copops.com.copopsApp.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class IncidentSubPojo implements Serializable {

    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SubInsident> getData() {
        return data;
    }



    public void setData(ArrayList<SubInsident> data) {
        this.data = data;
    }

    ArrayList<SubInsident> data;

    public class SubInsident implements Serializable {
        String incident_id;
        String incident_name;

        String incident_description;
        String incident_img_url;
        String incident_parent_id;

        public String getIncident_id() {
            return incident_id;
        }

        public void setIncident_id(String incident_id) {
            this.incident_id = incident_id;
        }

        public String getIncident_name() {
            return incident_name;
        }

        public void setIncident_name(String incident_name) {
            this.incident_name = incident_name;
        }

        public String getIncident_description() {
            return incident_description;
        }

        public void setIncident_description(String incident_description) {
            this.incident_description = incident_description;
        }

        public String getIncident_img_url() {
            return incident_img_url;
        }

        public void setIncident_img_url(String incident_img_url) {
            this.incident_img_url = incident_img_url;
        }

        public String getIncident_parent_id() {
            return incident_parent_id;
        }

        public void setIncident_parent_id(String incident_parent_id) {
            this.incident_parent_id = incident_parent_id;
        }


    }
}
