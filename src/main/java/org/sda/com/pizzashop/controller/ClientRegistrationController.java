package org.sda.com.pizzashop.controller;

import jakarta.validation.Valid;
import org.sda.com.pizzashop.controller.dto.UserRegistrationDTO;
import org.sda.com.pizzashop.model.User;
import org.sda.com.pizzashop.model.enums.UserRole;
import org.sda.com.pizzashop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ClientRegistrationController {
    private final UserService userService;

    public ClientRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDTO userRegistrationDTO() {
        return new UserRegistrationDTO();
    }

    @GetMapping("/client-register")
    public String showClientRegisterPage() {
        return "client-register";
    }

    @PostMapping("/client-register")
    public String registerClientAccount(
            @ModelAttribute("user")
            @Valid UserRegistrationDTO userRegistrationDTO,
            BindingResult bindingResult
    )
    {
        Optional<User> userOptional = userService.findByEmail(userRegistrationDTO.getEmail());
        if(userOptional.isPresent()){
            bindingResult.rejectValue("email",null,"Email already exists");
        }
        if(bindingResult.hasErrors()){
            return "client-register";
        }
        userService.createUser(
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getPassword(),
                UserRole.CLIENT,
                userRegistrationDTO.getName()
        );
        return "redirect:/client-register?success";
    }
}
