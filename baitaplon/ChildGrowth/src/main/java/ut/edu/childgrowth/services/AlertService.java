package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.models.Alert;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.repositories.AlertRepository;

import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public void checkAndHandleAlert(Child child, double heightCm, double weightKg) {
        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);

        // Kiểm tra nếu BMI bất thường
        if (bmi < 15 || bmi > 25) {
            String alertType = bmi < 15 ? "BMI_LOW" : "BMI_HIGH";
            String message = bmi < 15 ?
                    "Chỉ số BMI thấp hơn mức bình thường!" :
                    "Chỉ số BMI vượt ngưỡng bình thường!";

            // Kiểm tra xem đã có cảnh báo chưa
            boolean alreadyAlerted = alertRepository.existsByChildAndAlertTypeAndResolvedFalse(child, alertType);

            if (!alreadyAlerted) {
                Alert alert = new Alert(child, alertType, message);
                alertRepository.save(alert);
            }

        } else {
            // BMI bình thường -> đánh dấu alert trước là đã giải quyết, và thêm thông báo
            List<Alert> unresolvedAlerts = alertRepository.findByChildAndResolvedFalse(child);

            if (!unresolvedAlerts.isEmpty()) {
                for (Alert alert : unresolvedAlerts) {
                    alert.setResolved(true);
                    alertRepository.save(alert);
                }

                Alert recoveryAlert = new Alert(child, "BMI_NORMAL", "Chỉ số BMI đã trở lại bình thường.");
                alertRepository.save(recoveryAlert);
            }
        }
    }
}
