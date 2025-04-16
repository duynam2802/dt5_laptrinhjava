package ut.edu.childgrowth.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.Consultation;
import ut.edu.childgrowth.repositories.ConsultationRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    public Consultation createConsultation(Child child, String username, String reason, MultipartFile attachment) throws IOException {
        Consultation consultation = new Consultation();
        consultation.setChild(child);
        consultation.setReason(reason);
        consultation.setRequestDate(LocalDateTime.now());
        consultation.setStatus("PENDING");

        // Xử lý file nếu có
        if (attachment != null && !attachment.isEmpty()) {
            // Thư mục lưu trữ file (bạn có thể thay đổi đường dẫn này)
            String uploadDir = "uploads/consultations/";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // Tạo tên file duy nhất để tránh đụng nhau
            String uniqueFileName = UUID.randomUUID() + "_" + attachment.getOriginalFilename();
            String fullPath = uploadDir + uniqueFileName;

            // Lưu file vào thư mục
            attachment.transferTo(new File(fullPath));

            // Gán đường dẫn file cho đối tượng consultation
            consultation.setAttachmentPath(fullPath);
        }

        // Lưu vào DB
        return consultationRepository.save(consultation);
    }
}
