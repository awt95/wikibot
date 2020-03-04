package com3001.at00672.controller;

import com3001.at00672.model.ChatRepository;
import com3001.at00672.model.Message;
import com3001.at00672.model.Sender;
import com3001.at00672.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final ChatRepository chatRepository;

    @Autowired
    ChatbotService chatbotService;

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
        message.setSender(Sender.USER);
        chatRepository.save(message);
        // get response
        //Message response = new Message("Response to: " + message.getContent(), Sender.BOT);
        Message response = chatbotService.chatbotRequest(message);
        chatRepository.save(response);
        model.addAttribute("chat", chatRepository.findAll());
        return "index";
    }

    // Clear chat
    @PostMapping("/clear")
    public String clear(Model model) {
        chatRepository.deleteAll();
        return "redirect:/";
    }
}
