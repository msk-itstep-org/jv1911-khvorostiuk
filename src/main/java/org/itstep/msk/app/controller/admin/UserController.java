package org.itstep.msk.app.controller.admin;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.enums.Role;
import org.itstep.msk.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(Model model, @PageableDefault(value = 100, sort = "username") Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        model.addAttribute("users", users.getContent());
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("pages", users.getTotalPages());
        model.addAttribute("pagesRange", IntStream.range(0, users.getTotalPages()).toArray());

        return "userList";
    }

    @GetMapping("/edit/{id}")
    public String userRedactor(@PathVariable(name = "id") User user, Model model) {

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PostMapping("/save/{id}")
    public String save(
            @PathVariable(name = "id") User user,
            @ModelAttribute User editedUser
    ) {
        if (editedUser.getUsername().equals("admin")) {
            editedUser.getRoles().add(Role.ROLE_ADMIN);
        }

        user.setUsername(editedUser.getUsername());
        user.getRoles().clear();
        for (Role role : editedUser.getRoles()) {
            user.getRoles().add(role);
        }

        userRepository.save(user);
        userRepository.flush();

        return "redirect:user/edit/" + user.getId();
    }


}
