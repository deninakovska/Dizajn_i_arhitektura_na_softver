package com.example.demo.web;
import com.example.demo.entity.CompanyEntity;
import com.example.demo.services.AppService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/api")
@AllArgsConstructor
public class HomeController {
    private final AppService appService;

    @GetMapping("/homepage")
    public String showHomePage() {
        return "home-page";
    }
    @GetMapping("/data")
    public String showDataPage(@RequestParam(required = false) String error,
                               @RequestParam(name = "id", required = false) Long companyId, Model model) {
        if(companyId != null) {
            model.addAttribute("companyId", companyId);
            model.addAttribute("data", appService.findAllByCompanyId(companyId));
        } else {
            model.addAttribute("data", appService.listAllData());
        }
        model.addAttribute("companies", appService.listAll());
        return "data";
    }
    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }

}
