package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.util.Map;

//BẢNG ĐIỀU KHIỂN

@Entity
@Table(name = "dashboards")
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "user_stats", joinColumns = @JoinColumn(name = "dashboardId"))
    @MapKeyColumn(name = "statType")
    @Column(name = "value")
    private Map<String, Integer> userStats;

    @Column(nullable = false)
    private Double revenue;

    @ElementCollection
    @CollectionTable(name = "alert_summary", joinColumns = @JoinColumn(name = "dashboardId"))
    @MapKeyColumn(name = "alertType")
    @Column(name = "count")
    private Map<String, Integer> alertSummary;

    public Dashboard() {
    }

    public Dashboard(Map<String, Integer> userStats, Double revenue, Map<String, Integer> alertSummary) {
        this.userStats = userStats;
        this.revenue = revenue;
        this.alertSummary = alertSummary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Integer> getUserStats() {
        return userStats;
    }

    public void setUserStats(Map<String, Integer> userStats) {
        this.userStats = userStats;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Map<String, Integer> getAlertSummary() {
        return alertSummary;
    }

    public void setAlertSummary(Map<String, Integer> alertSummary) {
        this.alertSummary = alertSummary;
    }

//    public String generateReport() {
//        return "User Stats: " + userStats + ", Revenue: " + revenue + ", Alerts: " + alertSummary;
//    }
//
//    public void displayStats() {
//
//    }
}