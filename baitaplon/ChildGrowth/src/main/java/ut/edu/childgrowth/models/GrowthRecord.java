package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.time.LocalDate;

//CHỈ SỐ TĂNG TRƯỞNG

@Entity
@Table(name = "growth_records")
public class GrowthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(nullable = false)
    private LocalDate thoiDiem;

    @Column(nullable = false)
    private double canNang;

    @Column(nullable = false)
    private double chieuCao;

    @Column(nullable = false)
    private double bmi;

    // Constructor mặc định
    public GrowthRecord(Child child, LocalDate thoiDiem, double canNang, double chieuCao, double bmi) {
    }

    // Constructor với tham số
    public GrowthRecord(Child child, LocalDate thoiDiem, double canNang, double chieuCao) {
        this.child = child;
        this.thoiDiem = thoiDiem;
        this.canNang = canNang;
        this.chieuCao = chieuCao;
        this.bmi = calculateBmi(canNang, chieuCao);  // Tính toán BMI khi tạo bản ghi
    }

    public GrowthRecord() {

    }


    // Phương thức tính BMI
    private double calculateBmi(double canNang, double chieuCao) {
        if (chieuCao > 0) {
            return canNang / (chieuCao * chieuCao);
        }
        return 0;  // Trả về 0 nếu chiều cao không hợp lệ
    }

    // Getters và Setters
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

    public LocalDate getThoiDiem() {
        return thoiDiem;
    }

    public void setThoiDiem(LocalDate thoiDiem) {
        this.thoiDiem = thoiDiem;
    }

    public double getCanNang() {
        return canNang;
    }

    public void setCanNang(double canNang) {
        this.canNang = canNang;
    }

    public double getChieuCao() {
        return chieuCao;
    }

    public void setChieuCao(double chieuCao) {
        this.chieuCao = chieuCao;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}
