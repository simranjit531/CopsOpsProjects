package copops.com.copopsApp.pojo;

import java.io.Serializable;
import java.util.ArrayList;

    public class IncidentTypePojo implements Serializable {

        private String flag;
        ArrayList<Data> data;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public ArrayList<Data> getData() {
            return data;
        }

        public void setData(ArrayList<Data> data) {
            this.data = data;
        }




        public class Data implements Serializable {

            private String incident_description;

            private String incident_img_url;

            private String incident_id;

            private String incident_name;

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



        }
    }


