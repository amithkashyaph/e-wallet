package com.project.UserService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.UserService.dto.CreateUserRequestDTO;
import com.project.UserService.model.User;
import com.project.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) throws JsonProcessingException {
        return userService.createUser(createUserRequestDTO);
    }

    @GetMapping("/details/me")
    public User getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return userService.loadUserByUsername(user.getContact());
    }

    @GetMapping("/details")
    public User getUserDeatilsByContact(@RequestParam("contact") String contact) {
        return userService.loadUserByUsername(contact);
    }

}
