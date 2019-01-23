package copops.com.copopsApp.pojo;

import java.io.Serializable;

public class OperatorShowAlInfo implements Serializable {

    String grade;
    String available;
    String level;
    String profile_percent;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    String report;

    public String getNew_reports() {
        return new_reports;
    }

    public void setNew_reports(String new_reports) {
        this.new_reports = new_reports;
    }

    String new_reports;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getProfile_percent() {
        return profile_percent;
    }

    public void setProfile_percent(String profile_percent) {
        this.profile_percent = profile_percent;
    }

    public String getTotal_reports() {
        return total_reports;
    }

    public void setTotal_reports(String total_reports) {
        this.total_reports = total_reports;
    }

    public String getCompleted_reports() {
        return completed_reports;
    }

    public void setCompleted_reports(String completed_reports) {
        this.completed_reports = completed_reports;
    }


    String total_reports;
    String completed_reports;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
}
