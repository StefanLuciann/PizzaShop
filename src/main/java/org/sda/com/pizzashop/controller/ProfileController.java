package org.sda.com.pizzashop.controller;

import org.sda.com.pizzashop.model.User;
import org.sda.com.pizzashop.model.UserProfile;
import org.sda.com.pizzashop.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserProfile profile = user.getUserProfile();

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String address,
            @RequestParam String phone
    ) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserProfile profile = user.getUserProfile();

        if (profile == null) {
            profile = new UserProfile();
        }
        profile.setFirsName(firstName);
        profile.setLastName(lastName);
        profile.setAddress(address);
        profile.setPhoneNumber(phone);

        user.setUserProfile(profile);
        userService.save(user);

        return "redirect:/profile?updated=true";
    }

}
