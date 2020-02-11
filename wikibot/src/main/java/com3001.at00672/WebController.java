package com3001.at00672;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("message", new Message());
        return "index";
    }

    @PostMapping("/message")
    public String submitMessage(@ModelAttribute Message message) {
        return "message";
    }
}
