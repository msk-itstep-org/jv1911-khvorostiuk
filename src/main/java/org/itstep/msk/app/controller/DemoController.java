package org.itstep.msk.app.controller;

import org.itstep.msk.app.repository.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoController {
    @Autowired
    private GoodRepository goodRepository;

    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "") String name) {
        model.addAttribute("name", name);

        return "demo";
    }
}
