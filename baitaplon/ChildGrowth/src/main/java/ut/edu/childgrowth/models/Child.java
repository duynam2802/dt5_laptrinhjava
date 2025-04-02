package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//TRẺ

@Entity
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fullName; // Họ và tên của trẻ

    @Column(nullable = false)
    private LocalDate birthday; // Ngày sinh

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // Giới tính của trẻ

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Liên kết với User (người dùng, có thể là phụ huynh)

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrowthRecord> growthRecords = new ArrayList<>(); // Lịch sử phát triển của trẻ

    // Constructor mặc định
    public Child() {
    }

    // Constructor với thông tin cơ bản
    public Child(String fullName, LocalDate birthday, Gender gender, User user) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.user = user;
    }

    public Child(String fullName, LocalDate birthday, Gender gender) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
    }

    // Phương thức thêm bản ghi phát triển
    public void addGrowthRecord(LocalDate thoiDiem, double canNang, double chieuCao) {
        GrowthRecord record = new GrowthRecord(this, thoiDiem, canNang, chieuCao);
        this.growthRecords.add(record);
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<GrowthRecord> getGrowthRecords() {
        return growthRecords;
    }

    public void setGrowthRecords(List<GrowthRecord> growthRecords) {
        this.growthRecords = growthRecords;
    }

    // Enum cho giới tính
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
