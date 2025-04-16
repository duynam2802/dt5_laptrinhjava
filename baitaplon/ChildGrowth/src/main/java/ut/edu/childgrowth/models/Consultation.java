package ut.edu.childgrowth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Entity
public class Consultation {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Child child;

    private String reason;
    private String attachmentPath;
    private LocalDateTime requestDate;
    private String status; // "PENDING", "ANSWERED", etc.

    private String submittedBy;  // Thêm trường submittedBy

    // Getter và Setter cho các trường
    public String getStatus() {
        return status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public String getReason() {
        return reason;
    }

    public Child getChild() {
        return child;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }
}

