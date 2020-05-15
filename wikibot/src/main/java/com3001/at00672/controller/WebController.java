package com3001.at00672.controller;

import com3001.at00672.model.MessageRepository;
import com3001.at00672.model.Message;
import com3001.at00672.model.Sender;
import com3001.at00672.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class WebController {

    private final MessageRepository messageRepository;

    ChatbotService chatbotService = new ChatbotService();

    @Autowired
    public WebController(MessageRepository messageRepository) throws IOException {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session){
        System.out.println("Session: " + session.getId());
        Message message = new Message();
        message.setSession(session.getId());
        model.addAttribute("message", message);
        model.addAttribute("chat", messageRepository.findBySessionEquals(session.getId()));
        return "index";
    }

    @PostMapping("/")
    public String submitMessage(@ModelAttribute Message message, BindingResult result, Model model, HttpSession session) {
        message.setSender(Sender.USER);
        message.setSession(session.getId());
        messageRepository.save(message);
        // get response
        Message response = chatbotService.chatbotRequest(message);
        response.setSession(session.getId());
        messageRepository.save(response);
        model.addAttribute("chat", messageRepository.findBySessionEquals(session.getId()));
        return "index";
    }

    // Clear chat
    @PostMapping("/clear")
    public String clear(Model model) {
        messageRepository.deleteAll();
        return "redirect:/";
    }
}
