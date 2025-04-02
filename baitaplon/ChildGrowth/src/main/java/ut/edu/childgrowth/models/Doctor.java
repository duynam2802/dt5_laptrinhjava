package ut.edu.childgrowth.models;

import jakarta.persistence.*;

//BÁC SĨ

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String specialty;

    public Doctor() {
    }

    public Doctor(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

//    public String viewRequest(Long requestId) {
//        return "Đang xem yêu cầu ID: " + requestId;
//    }
//
//    public void respondToRequest(Long requestId, String advice) {
//        System.out.println("Phản hồi yêu cầu ID " + requestId + ": " + advice);
//    }
}