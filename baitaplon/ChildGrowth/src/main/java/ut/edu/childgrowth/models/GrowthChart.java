package ut.edu.childgrowth.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Map;

@Data
@Entity
@Table(name = "growth_charts")
public class GrowthChart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ElementCollection
    @CollectionTable(name = "growth_data_points", joinColumns = @JoinColumn(name = "growth_chart_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "data_type")
    @Column(name = "value")
    private Map<GrowthMetric, Double> dataPoints;

    public enum GrowthMetric {
        WEIGHT, HEIGHT, BMI
    }
}
