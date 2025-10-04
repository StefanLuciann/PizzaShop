package org.sda.com.pizzashop.controller;

import jakarta.validation.Valid;
import org.sda.com.pizzashop.model.ContactMessage;
import org.sda.com.pizzashop.repository.ContactMessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContactController {

    private final ContactMessageRepository messageRepository;

    public ContactController(ContactMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping("/send-message")
    public String sendMessage(
            @Valid ContactMessage newMessage,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", " Please check your inputs.");
            return "contact";
        }

        messageRepository.save(newMessage);

        model.addAttribute("successMessage",
                "Thank you, " + newMessage.getName() + "! Your message has been sent. We will contact you shortly.");

        return "contact";
    }


    @GetMapping("/admin/messages")
    public String viewMessages(Model model) {
        List<ContactMessage> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        return "admin-messages";
    }


    @GetMapping("/admin/messages/{id}")
    public String viewMessage(@PathVariable Long id, Model model) {
        ContactMessage message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id " + id));
        model.addAttribute("message", message);
        return "admin-message-view";
    }


    @GetMapping("/admin/messages/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageRepository.deleteById(id);
        return "redirect:/admin/messages";
    }
}
