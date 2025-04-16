package ut.edu.childgrowth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ut.edu.childgrowth.models.Alert;
import ut.edu.childgrowth.models.BmiBoysZWho2007Exp;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.BmiGirlsZWho2007Exp;
import ut.edu.childgrowth.repositories.AlertRepository;
import ut.edu.childgrowth.repositories.BmiBoysZRepository;
import ut.edu.childgrowth.repositories.BmiGirlsZRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private BmiGirlsZRepository bmiGirlsZRepository;

    @Autowired
    BmiBoysZRepository bmiBoysZRepository;



    public void checkAndHandleAlert(Child child, double heightCm, double weightKg) {
        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);

        // Tính tháng tuổi
        LocalDate dob = child.getBirthday();
        int months = Period.between(dob, LocalDate.now()).getYears() * 12 +
                Period.between(dob, LocalDate.now()).getMonths();

        // Khởi tạo các tham số L, M, S
        double l, m, s;

        // Kiểm tra giới tính và lấy dữ liệu tham chiếu
        if (child.getGender().equals("MALE")) {
            BmiBoysZWho2007Exp ref = bmiBoysZRepository.findByMonth(months).orElse(null);
            if (ref == null) return;
            l = ref.getL();
            m = ref.getM();
            s = ref.getS();
        } else {
            BmiGirlsZWho2007Exp ref = bmiGirlsZRepository.findByMonth(months).orElse(null);
            if (ref == null) return;
            l = ref.getL();
            m = ref.getM();
            s = ref.getS();
        }

        // Tính Z-score
        double z;
        if (l == 0) {
            z = Math.log(bmi / m) / s;
        } else {
            z = (Math.pow((bmi / m), l) - 1) / (l * s);
        }

        String alertType = null;
        String message = null;

        // Phân mức theo Z-score
        if (z < -3) {
            alertType = "BMI_SEVERELY_LOW";
            message = String.format("Chỉ số BMI rất thấp (Z = %.2f), thấp hơn chuẩn %.2f độ lệch chuẩn.", z, Math.abs(z));
        } else if (z < -2) {
            alertType = "BMI_MODERATELY_LOW";
            message = String.format("Chỉ số BMI thấp (Z = %.2f), thấp hơn chuẩn %.2f độ lệch chuẩn.", z, Math.abs(z));
        } else if (z < -1) {
            alertType = "BMI_MILDLY_LOW";
            message = String.format("Chỉ số BMI hơi thấp (Z = %.2f), thấp hơn chuẩn %.2f độ lệch chuẩn.", z, Math.abs(z));
        } else if (z > 3) {
            alertType = "BMI_SEVERELY_HIGH";
            message = String.format("Chỉ số BMI rất cao (Z = %.2f), cao hơn chuẩn %.2f độ lệch chuẩn.", z, Math.abs(z));
        } else if (z > 2) {
            alertType = "BMI_MODERATELY_HIGH";
            message = String.format("Chỉ số BMI cao (Z = %.2f), cao hơn chuẩn %.2f độ lệch chuẩn.", z, Math.abs(z));
        } else if (z > 1) {
            alertType = "BMI_MILDLY_HIGH";
            message = String.format("Chỉ số BMI hơi cao (Z = %.2f), cao hơn chuẩn %.2f độ lệch chuẩn.", z, Math.abs(z));
        } else {
            // Luôn đánh dấu các alert trước đó là đã resolved (nếu có)
            List<Alert> unresolved = alertRepository.findByChildAndResolvedFalse(child);
            for (Alert alert : unresolved) {
                alert.setResolved(true);
                alertRepository.save(alert);
            }

            // Tạo cảnh báo BMI bình thường
            String normalMsg = String.format("Chỉ số BMI trong giới hạn bình thường (Z = %.2f).", z);
            String detail = String.format("BMI = %.2f | Z = %.2f | L = %.3f, M = %.3f, S = %.3f", bmi, z, l, m, s);
            Alert normalAlert = new Alert(child, "BMI_NORMAL", normalMsg, detail);
            alertRepository.save(normalAlert);

            return;
        }

        // Nếu bất thường: kiểm tra và lưu cảnh báo nếu chưa có
        boolean alreadyAlerted = alertRepository.existsByChildAndAlertTypeAndResolvedFalse(child, alertType);

        if (!alreadyAlerted) {
            String detailMessage = String.format("BMI = %.2f | Z = %.2f | L = %.3f, M = %.3f, S = %.3f", bmi, z, l, m, s);
            Alert alert = new Alert(child, alertType, message, detailMessage);
            alertRepository.save(alert);
        }
    }

    public List<Alert> getUnresolvedAlertsForChild(Child child) {
        return alertRepository.findByChildAndResolvedFalse(child);
    }

}
