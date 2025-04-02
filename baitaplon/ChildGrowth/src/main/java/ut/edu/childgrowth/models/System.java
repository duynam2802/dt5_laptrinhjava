package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

//HỆ THỐNG

@Entity
@Table(name = "systems")
public class System {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "systemId")
    private List<User> users = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "systemId")
    private List<Child> children = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "systemId")
    private List<Doctor> doctors = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "systemId")
    private List<MembershipPlan> plans = new ArrayList<>();

    public System() {
    }

    public System(List<User> users, List<Child> children, List<Doctor> doctors, List<MembershipPlan> plans) {
        this.users = (users != null) ? users : new ArrayList<>();
        this.children = (children != null) ? children : new ArrayList<>();
        this.doctors = (doctors != null) ? doctors : new ArrayList<>();
        this.plans = (plans != null) ? plans : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<MembershipPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<MembershipPlan> plans) {
        this.plans = plans;
    }

//    public void manageUsers() {
//
//    }
//
//    public void managePlans() {
//
//    }
//
//    public void trackGrowth() {
//
//    }
}