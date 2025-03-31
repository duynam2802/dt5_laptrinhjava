package ut.edu.childgrowth.models;
import jakarta.persistence.*;
import java.time.LocalDate;

import java.sql.Struct;
import java.util.*;
import ut.edu.childgrowth.models.Child;

@Entity
@Table (name = "childs")
public class Child {
    @Id //Khoachinh
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false, unique = true)
    private String fullName;

    private enum Gender {
        MALE, FEMALE, OTHER
    }
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate thoiDiem;

    @Column(nullable = false)
    private double canNang;

    @Column(nullable = false)
    private double chieuCao;

    @Column(nullable = false)
    private double bmi;

    public Child() {
    }

    public Child(String fullName, LocalDate birthday, Gender gender, LocalDate thoiDiem, double chieuCao, double bmi ) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.thoiDiem = thoiDiem;
        this.chieuCao = chieuCao;
        this.bmi = bmi;
    }

    // Getters and setters

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getFullName() {
        return fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getThoiDiem() {
        return thoiDiem;
    }

    public double getCanNang() {
        return canNang;
    }

    public double getChieuCao() {
        return chieuCao;
    }

    public double getBmi() {
        return bmi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setThoiDiem(LocalDate thoiDiem) {
        this.thoiDiem = thoiDiem;
    }

    public void setCanNang(double canNang) {
        this.canNang = canNang;
    }

    public void setChieuCao(double chieuCao) {
        this.chieuCao = chieuCao;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}

