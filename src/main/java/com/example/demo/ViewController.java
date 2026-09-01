package com.example.demo;

import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private static final int PAGE_SIZE = 20;

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users-page")
    public String showUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model) {

        if (page < 0) {
            page = 0;
        }

        Pageable pageable =
                PageRequest.of(
                        page,
                        PAGE_SIZE,
                        Sort.by(Sort.Direction.ASC, "id"));

        Page<User> userPage;

        if (keyword != null && !keyword.isBlank()) {
            userPage = userService.searchUsers(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            userPage = userService.findAll(pageable);
            model.addAttribute("keyword", "");
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("hasPrevious", userPage.hasPrevious());
        model.addAttribute("hasNext", userPage.hasNext());
        model.addAttribute("totalElements", userPage.getTotalElements());

        return "users";
    }

    @GetMapping("/users-page/export")
    public ResponseEntity<byte[]> exportUsers(
            @RequestParam(required = false) String keyword) {

        List<User> users;

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE,
                        Sort.by(Sort.Direction.ASC, "id"));

        if (keyword != null && !keyword.isBlank()) {
            users = userService.searchUsers(keyword, pageable).getContent();
        } else {
            users = userService.findAll(pageable).getContent();
        }

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("ID,名前,年齢,住所\r\n");

        for (User user : users) {
            csv.append(user.getId())
                    .append(',')
                    .append(escapeCsv(user.getName()))
                    .append(',')
                    .append(user.getAge())
                    .append(',')
                    .append(escapeCsv(user.getAddress()))
                    .append("\r\n");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "text",
                        "csv",
                        StandardCharsets.UTF_8));
        headers.set(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"users.csv\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv.toString()
                        .getBytes(StandardCharsets.UTF_8));
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\r")
                || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
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
                        null,
                        userForm.getName(),
                        userForm.getAddress(),
                        userForm.getBirthday());

        try {
            userService.createUser(user);
        } catch (DuplicateUserException e) {
            bindingResult.reject("duplicateUser", e.getMessage());
            return "user-form";
        }

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
        userForm.setAddress(user.getAddress());
        userForm.setBirthday(user.getBirthday());
        userForm.setVersion(user.getVersion());

        model.addAttribute("userId", id);
        model.addAttribute("userForm", userForm);

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
                        userForm.getVersion(),
                        userForm.getName(),
                        userForm.getAddress(),
                        userForm.getBirthday());

        try {
            userService.updateUser(user);
        } catch (ObjectOptimisticLockingFailureException e) {
            bindingResult.reject(
                    "optimisticLocking",
                    "他のユーザーによって更新されました。最新のデータを確認してください。");
            model.addAttribute("userId", id);
            return "user-edit";
        }

        return "redirect:/users-page";
    }
}