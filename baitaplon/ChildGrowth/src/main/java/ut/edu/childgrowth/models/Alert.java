package ut.edu.childgrowth.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(name = "alertType", nullable = false)
    private String alertType;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private boolean resolved;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


    public Alert() {
    }

    public Alert(Child child, String alertType, String message) {
        this.child = child;
        this.alertType = alertType;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isResolved() {
        return resolved;
    }


}
