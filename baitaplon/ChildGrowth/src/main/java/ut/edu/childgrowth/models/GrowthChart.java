package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import java.util.Map;
import java.util.Set;

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

    // Các logic thêm vào
    public double calculateBmi() {
        Double height = dataPoints.get("height");
        Double weight = dataPoints.get("weight");

        if (height != null && weight != null && height > 0) {
            double heightInMeters = height / 100;
            return weight / (heightInMeters * heightInMeters);
        }
        return 0;
    }

    public void addDataPoint(String dataType, Double value) {
        if (dataType != null && value != null) {
            dataPoints.put(dataType, value);
        }
    }

    public double calculateGrowthChange(String dataType) {
        Double initialValue = dataPoints.get(dataType);
        if (initialValue == null) {
            return 0;
        }
        Double latestValue = dataPoints.get(dataType);
        return latestValue - initialValue;
    }

    public String generateChart(String dataType) {
        if (dataPoints == null || !dataPoints.containsKey(dataType)) {
            return "Không có dữ liệu cho " + dataType;
        }
        Double value = dataPoints.get(dataType);
        return "Biểu đồ được tạo cho " + dataType + " với giá trị: " + value;
    }

    public void shareChart(Long doctorId) {
//        System.out.println("Biểu đồ đã được chia sẻ với bác sĩ có ID: " + doctorId);
    }

    public Set<String> getDataTypes() {
        return dataPoints.keySet();
    }

    public boolean isValid() {
        for (Map.Entry<String, Double> entry : dataPoints.entrySet()) {
            if (entry.getValue() == null || entry.getValue() <= 0) {
                return false;
            }
        }
        return true;
    }
}
