package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.util.Map;

//CẢNH BÁO

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long childId;

    @Column(name = "alertType", nullable = false)
    private String alertType;

    @Column(nullable = false)
    private String message;

    public Alert() {
    }

    public Alert(Long childId, String alertType, String message) {
        this.childId = childId;
        this.alertType = alertType;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public boolean checkGrowthAnomaly(Map<String, Double> data) {
//        return data.get("bmi") != null && (data.get("bmi") < 15 || data.get("bmi") > 25);
//    }
//
//    public void sendAlert(Long userId) {
//        System.out.println("Cảnh báo đã gửi đến ID: " + userId + " - " + message);
//    }
}