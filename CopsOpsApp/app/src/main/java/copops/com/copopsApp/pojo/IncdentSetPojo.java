package copops.com.copopsApp.pojo;

import java.io.Serializable;

public class IncdentSetPojo implements Serializable {

    String ref_incident_category_id;
    String ref_incident_subcategory_id;
    String description;
    String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIncident_id() {
        return incident_id;
    }

    public void setIncident_id(String incident_id) {
        this.incident_id = incident_id;
    }

    String incident_id;

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    String available;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    String city_id;

    public String getIncident_lat() {
        return incident_lat;
    }

    public void setIncident_lat(String incident_lat) {
        this.incident_lat = incident_lat;
    }

    public String getIncident_lng() {
        return incident_lng;
    }

    public void setIncident_lng(String incident_lng) {
        this.incident_lng = incident_lng;
    }

    public String getObjects() {
        return objects;
    }

    public void setObjects(String objects) {
        this.objects = objects;
    }

    String incident_lat;
    String incident_lng;
    String latitude;
    String longitude;

    public String getHandrail_lat() {
        return handrail_lat;
    }

    public void setHandrail_lat(String handrail_lat) {
        this.handrail_lat = handrail_lat;
    }

    public String getHandrail_lng() {
        return handrail_lng;
    }

    public void setHandrail_lng(String handrail_lng) {
        this.handrail_lng = handrail_lng;
    }

    String handrail_lat;
    String handrail_lng;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    String device_id;

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

    public String getIncident_description() {
        return incident_description;
    }

    public void setIncident_description(String incident_description) {
        this.incident_description = incident_description;
    }

    public String getOther_description() {
        return other_description;
    }

    public void setOther_description(String other_description) {
        this.other_description = other_description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
    public String getobjects() {
        return objects;
    }

    public void setobjects(String objects) {
        this.objects = objects;
    }

    String incident_description;
    String other_description;
    String created_by;
    String objects;
    String device_language;

    public void setdevice_language(String device_language) {
        this.device_language = device_language;
    }
    public String getdevice_language() {
        return device_language;
    }
}
