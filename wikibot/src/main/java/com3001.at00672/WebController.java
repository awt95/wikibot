package com3001.at00672;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final ChatRepository chatRepository;

    @Autowired
    public WebController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("message", new Message());
        model.addAttribute("chat", chatRepository.findAll());
        return "index";
    }

    @PostMapping("/")
    public String submitMessage(@ModelAttribute Message message, BindingResult result, Model model) {
        chatRepository.save(message);
        model.addAttribute("chat", chatRepository.findAll());
        return "index";
    }
}
