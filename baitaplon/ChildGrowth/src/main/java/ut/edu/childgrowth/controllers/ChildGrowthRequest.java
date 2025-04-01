package ut.edu.childgrowth.controllers;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ChildGrowthRequest {

    @Min(value = 1, message = "Cân nặng phải lớn hơn 0 kg")
    @Max(value = 200, message = "Cân nặng không hợp lệ")
    private double canNang;

    @Min(value = 30, message = "Chiều cao phải lớn hơn 30 cm")
    @Max(value = 250, message = "Chiều cao không hợp lệ")
    private double chieuCao;

    @Min(value = 10, message = "BMI phải lớn hơn 10")
    @Max(value = 40, message = "BMI không hợp lệ")
    private double bmi;

    @NotNull(message = "Thời điểm đo không được để trống")
    private LocalDate thoiDiem;

    // Constructor không đối số
    public ChildGrowthRequest() {}

    // Constructor có tham số
    public ChildGrowthRequest(double canNang, double chieuCao, double bmi, LocalDate thoiDiem) {
        this.canNang = canNang;
        this.chieuCao = chieuCao;
        this.bmi = bmi;
        this.thoiDiem = thoiDiem;
    }

    // Getter và Setter
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

    public LocalDate getThoiDiem() {
        return thoiDiem;
    }

    public void setThoiDiem(LocalDate thoiDiem) {
        this.thoiDiem = thoiDiem;
    }
}
