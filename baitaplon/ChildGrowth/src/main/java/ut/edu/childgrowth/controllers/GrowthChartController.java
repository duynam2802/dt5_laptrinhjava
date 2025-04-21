package ut.edu.childgrowth.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ut.edu.childgrowth.jwt.JwtUtil;
import ut.edu.childgrowth.models.Child;
import ut.edu.childgrowth.models.GrowthRecord;
import ut.edu.childgrowth.models.User;
import ut.edu.childgrowth.services.ChildService;
import ut.edu.childgrowth.services.GrowthRecordService;
import ut.edu.childgrowth.models.GrowthChart;
import ut.edu.childgrowth.services.UserService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GrowthChartController {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;



    private final ChildService childService;
    private final GrowthRecordService growthRecordService;

    public GrowthChartController(ChildService childService, GrowthRecordService growthRecordService) {
        this.childService = childService;
        this.growthRecordService = growthRecordService;
    }

    @GetMapping("/growth-charts")
    public String showGrowthCharts(
                                   HttpSession session,
                                   @RequestParam(required = false) Long childId, Model model) {
        // Get all children for the list
//        List<Child> children = childService.getAllChildren();
        String token = (String) session.getAttribute("token");
//        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username);
        List<Child> children = childService.getChildrenByUser(user);
        model.addAttribute("children", children);

        int age = Period.between(children.getLast().getBirthday(), LocalDate.now()).getYears();
        model.addAttribute("age", age);
        model.addAttribute("fullname", children.getLast().getFullName());
        model.addAttribute("nickname", children.getLast().getNickname());
        model.addAttribute("birthday", children.getLast().getBirthday());
        model.addAttribute("gender", children.getLast().getGender());
        model.addAttribute("medicalHistory", children.getLast().getMedicalHistory());

        // If a child is selected, get their growth records
        if (childId != null) {
            Child selectedChild = childService.findById(childId);
            model.addAttribute("selectedChild", selectedChild);

            List<GrowthRecord> records = growthRecordService.getGrowthRecordsByChildId(childId);

            // Prepare data for Chart.js
            List<String> dates = records.stream()
                    .map(record -> record.getThoiDiem().toString())
                    .collect(Collectors.toList());

            List<Double> weights = records.stream()
                    .map(GrowthRecord::getCanNang)
                    .collect(Collectors.toList());

            List<Double> heights = records.stream()
                    .map(GrowthRecord::getChieuCao)
                    .collect(Collectors.toList());

            List<Double> bmis = records.stream()
                    .map(GrowthRecord::getBmi)
                    .collect(Collectors.toList());

            model.addAttribute("dates", dates);
            model.addAttribute("weights", weights);
            model.addAttribute("heights", heights);
            model.addAttribute("bmis", bmis);
        }

        return "growth-charts";
    }
}