package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.util.List;

//GÓI THÀNH VIÊN

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @ElementCollection
    @CollectionTable(name = "plan_features", joinColumns = @JoinColumn(name = "planId"))
    @Column(name = "feature")
    private List<String> features;

    public MembershipPlan() {
    }

    public MembershipPlan(String name, Double price, List<String> features) {
        this.name = name;
        this.price = price;
        this.features = features;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
   // các logic
//    public String getPlanDetails() {
//        return "Gói: " + name + ", Giá: " + price + " VND/month, Tính năng: " + features;
//    }
//
//    public void applyPlan(Long userId) {
//        System.out.println("Gói " + name + " đã được ID " + userId + " mua.");
//    }
}