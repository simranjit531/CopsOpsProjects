package copops.com.copopsApp.pojo;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Ranjan Gupta
 */
public class AllLocationAndCityPojo implements Serializable{

    String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    ArrayList<City> cities;
    ArrayList<Data> data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }



    public class Data implements Serializable {
        String incident_description;

        public String getSub_category_name() {
            return sub_category_name;
        }

        public void setSub_category_name(String sub_category_name) {
            this.sub_category_name = sub_category_name;
        }

        String sub_category_name;
        String id;
        String ref_incident_category_id;
        String ref_incident_subcategory_id;
        String other_description;
        String reference;
        String qr_code;
        String latitude;
        String longitude;
        String address;
        String city;
        String updated_on;
        String created_by;
        String is_deleted;
        String updated_at;
        String created_at;
        String distance;

        public String getIncident_description() {
            return incident_description;
        }

        public void setIncident_description(String incident_description) {
            this.incident_description = incident_description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRef_incident_category_id() {
            return ref_incident_category_id;
        }

        public void setRef_incident_category_id(String ref_incident_category_id) {
            this.ref_incident_category_id = ref_incident_category_id;
        }

        public String getRef_incident_subcategory_id() {
            return ref_incident_subcategory_id;
        }

        public void setRef_incident_subcategory_id(String ref_incident_subcategory_id) {
            this.ref_incident_subcategory_id = ref_incident_subcategory_id;
        }

        public String getOther_description() {
            return other_description;
        }

        public void setOther_description(String other_description) {
            this.other_description = other_description;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getQr_code() {
            return qr_code;
        }

        public void setQr_code(String qr_code) {
            this.qr_code = qr_code;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(String is_deleted) {
            this.is_deleted = is_deleted;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }


    }

    public class City implements Serializable{
        String id;
        String city_name;
        String updated_on;
        String is_deleted;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public String getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(String is_deleted) {
            this.is_deleted = is_deleted;
        }


    }
}
