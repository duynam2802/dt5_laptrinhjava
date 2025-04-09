package ut.edu.childgrowth.models;

import jakarta.persistence.*;

import java.util.Map;

//YÊU CẦU TƯ VẤN

@Entity
@Table(name = "consultation_requests")
public class ConsultationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long requestId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "childId", nullable = false)
    private Long childId;

    @Column(name = "doctorId", nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private String status;

    private String response;

    public ConsultationRequest() {
    }

    public ConsultationRequest(Long userId, Long childId, Long doctorId, String status) {
        this.userId = userId;
        this.childId = childId;
        this.doctorId = doctorId;
        this.status = status;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response != null ? response : "Chưa có phản hồi nào!";
    }

    public void setResponse(String response) {
        this.response = response;
    }

}