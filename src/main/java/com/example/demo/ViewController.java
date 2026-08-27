package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users-page")
    public String showUsers(
            @RequestParam(required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.isBlank()) {

            model.addAttribute(
                    "users",
                    userService.searchUsers(keyword));

        } else {

            model.addAttribute(
                    "users",
                    userService.findAll());

        }

        return "users";
    }

    @GetMapping("/user-form")
    public String userForm(Model model) {

        model.addAttribute(
                "userForm",
                new UserForm());

        return "user-form";
    }

    @PostMapping("/users-page/add")
    public String addUser(
            @Valid UserForm userForm,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user-form";
        }

        User user =
                new User(
                        null,
                        userForm.getName(),
                        userForm.getAge(),
                        userForm.getAddress());

        userService.createUser(user);

        return "redirect:/users-page";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        userService.deleteById(id);

        return "redirect:/users-page";
    }
    @GetMapping("/users/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            Model model) {

        User user =
                userService.findById(id);

        UserForm userForm =
                new UserForm();

        userForm.setName(user.getName());
        userForm.setAge(user.getAge());
        userForm.setAddress(user.getAddress());

        model.addAttribute(
                "userId",
                id);

        model.addAttribute(
                "userForm",
                userForm);

        return "user-edit";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(
            @PathVariable Long id,
            @Valid UserForm userForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("userId", id);

            return "user-edit";
        }

        User user =
                new User(
                        id,
                        userForm.getName(),
                        userForm.getAge(),
                        userForm.getAddress());

        userService.updateUser(user);

        return "redirect:/users-page";
    }
}