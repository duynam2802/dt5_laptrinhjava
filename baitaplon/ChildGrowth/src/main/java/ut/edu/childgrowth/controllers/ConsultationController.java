package ut.edu.childgrowth.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.Consultation;
import ut.edu.childgrowth.repositories.UserRepository;
import ut.edu.childgrowth.services.ChildService;
import ut.edu.childgrowth.services.ConsultationService;

import java.util.List;

@Controller
@RequestMapping("/consultation")
public class ConsultationController {

    private final ChildService childService;
    private final ConsultationService consultationService;
    private final UserRepository userRepository;

    public ConsultationController(ChildService childService,
                                  ConsultationService consultationService,
                                  UserRepository userRepository) {
        this.childService = childService;
        this.consultationService = consultationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showConsultationForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Child> children = childService.getChildrenByUser(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"))
        );

        model.addAttribute("children", children); // ❗ cần có dòng này
        return "consultation";                    // ❗ cần trả về tên view
    }

    @PostMapping
    public String submitConsultationRequest(
            @RequestParam("childId") Long childId,
            @RequestParam("consultReason") String consultReason,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            String username = authentication.getName();
            Child child = childService.findById(childId);

            if (child == null) {
                redirectAttributes.addFlashAttribute("error", "Child not found");
                return "redirect:/consultation";
            }

            consultationService.createConsultation(
                    child,
                    username,
                    consultReason,
                    attachment
            );

            redirectAttributes.addFlashAttribute("success", "Consultation request submitted successfully");
            return "redirect:/consultation-success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting consultation: " + e.getMessage());
            return "redirect:/consultation";
        }
    }
}