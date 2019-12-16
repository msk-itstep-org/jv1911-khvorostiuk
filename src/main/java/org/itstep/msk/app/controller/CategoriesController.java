package org.itstep.msk.app.controller;

import org.itstep.msk.app.repository.CategoryRepository;
import org.itstep.msk.app.service.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoriesController {
    @Autowired
    private CategoryRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    private String index(Model model){
        model.addAttribute("root", repository.getRootCategories());
        return "category_tree";
    }
}
