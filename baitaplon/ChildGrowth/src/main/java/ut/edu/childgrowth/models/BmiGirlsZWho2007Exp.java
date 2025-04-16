package ut.edu.childgrowth.models;

import jakarta.persistence.*;

@Entity
@Table(name = "bmi_girls_z_who_2007_exp")
public class BmiGirlsZWho2007Exp {

    @Id
    @Column(name = "Month")
    private Integer month;

    @Column(name = "L")
    private Double l;

    @Column(name = "M")
    private Double m;

    @Column(name = "S")
    private Double s;

    @Column(name = "SDneg4")
    private Double sdneg4;

    @Column(name = "SDneg3")
    private Double sdneg3;

    @Column(name = "SDneg2")
    private Double sdneg2;

    @Column(name = "SDneg1")
    private Double sdneg1;

    @Column(name = "SD0")
    private Double sd0;

    @Column(name = "SD1")
    private Double sd1;

    @Column(name = "SD2")
    private Double sd2;

    @Column(name = "SD3")
    private Double sd3;

    @Column(name = "SD4")
    private Double sd4;

    // Constructors (no-arg and with arguments)
    public BmiGirlsZWho2007Exp() {
    }

    public BmiGirlsZWho2007Exp(Integer month, Double l, Double m, Double s, Double sdneg4, Double sdneg3, Double sdneg2, Double sdneg1, Double sd0, Double sd1, Double sd2, Double sd3, Double sd4) {
        this.month = month;
        this.l = l;
        this.m = m;
        this.s = s;
        this.sdneg4 = sdneg4;
        this.sdneg3 = sdneg3;
        this.sdneg2 = sdneg2;
        this.sdneg1 = sdneg1;
        this.sd0 = sd0;
        this.sd1 = sd1;
        this.sd2 = sd2;
        this.sd3 = sd3;
        this.sd4 = sd4;
    }

    // Getters and Setters
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getL() {
        return l;
    }

    public void setL(Double l) {
        this.l = l;
    }

    public Double getM() {
        return m;
    }

    public void setM(Double m) {
        this.m = m;
    }

    public Double getS() {
        return s;
    }

    public void setS(Double s) {
        this.s = s;
    }

    public Double getSdneg4() {
        return sdneg4;
    }

    public void setSdneg4(Double sdneg4) {
        this.sdneg4 = sdneg4;
    }

    public Double getSdneg3() {
        return sdneg3;
    }

    public void setSdneg3(Double sdneg3) {
        this.sdneg3 = sdneg3;
    }

    public Double getSdneg2() {
        return sdneg2;
    }

    public void setSdneg2(Double sdneg2) {
        this.sdneg2 = sdneg2;
    }

    public Double getSdneg1() {
        return sdneg1;
    }

    public void setSdneg1(Double sdneg1) {
        this.sdneg1 = sdneg1;
    }

    public Double getSd0() {
        return sd0;
    }

    public void setSd0(Double sd0) {
        this.sd0 = sd0;
    }

    public Double getSd1() {
        return sd1;
    }

    public void setSd1(Double sd1) {
        this.sd1 = sd1;
    }

    public Double getSd2() {
        return sd2;
    }

    public void setSd2(Double sd2) {
        this.sd2 = sd2;
    }

    public Double getSd3() {
        return sd3;
    }

    public void setSd3(Double sd3) {
        this.sd3 = sd3;
    }

    public Double getSd4() {
        return sd4;
    }

    public void setSd4(Double sd4) {
        this.sd4 = sd4;
    }

    // ... (Getters and Setters cho các thuộc tính khác) ...
}