package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.util.Map;

//BIỂU ĐỒ TĂNG TRƯỞNG

@Entity
@Table(name = "growth_charts")
public class GrowthChart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long childId;

    @ElementCollection
    @CollectionTable(name = "growth_data_points", joinColumns = @JoinColumn(name = "growthChartId"))
    @MapKeyColumn(name = "dataType")
    @Column(name = "value")
    private Map<String, Double> dataPoints;

    public GrowthChart() {
    }

    public GrowthChart(Long childId, Map<String, Double> dataPoints) {
        this.childId = childId;
        this.dataPoints = dataPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Map<String, Double> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Map<String, Double> dataPoints) {
        this.dataPoints = dataPoints;
    }
    //Các logic
//    public String generateChart(String type) {
//        if (dataPoints == null || !dataPoints.containsKey(type)) {
//            return "Không có dữ liệu cho " + type;
//        }
//        return "Biểu đồ được tạo cho " + type + " với dữ liệu: " + dataPoints.get(type);
//    }
//
//    public void shareChart(Long doctorId) {
//        System.out.println("Biểu đồ đã được chia sẻ với bác sĩ có ID: " + doctorId);
//    }

}