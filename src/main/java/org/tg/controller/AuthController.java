package org.tg.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tg.service.EgoriottosBotService;
import org.tg.service.UserService;
import java.util.Map;

@Controller
public class AuthController {

    private static final String ERROR_PATH = "error";
    private static final String PROFILE_PATH = "profile";

    public AuthController(EgoriottosBotService botService, UserService userService) {
        this.botService = botService;
        this.userService = userService;
    }

    private final EgoriottosBotService botService;
    private final UserService userService;

    @PostMapping("/auth/telegram")
    public String handleAuth(@RequestParam(value = "initData", required = false) String initData, HttpSession session) {
        try {
            if (!botService.validateTelegramData(initData)) {
                return "redirect:/" + ERROR_PATH;
            }
            Map<String, Object> userData = botService.parseInitData(initData);
            session.setAttribute("user", userData);
            userService.saveFromInitData(initData,botService);

            return PROFILE_PATH;
        } catch (Exception e) {
            return "redirect:/" + ERROR_PATH;
        }
    }

    @GetMapping("/" + PROFILE_PATH)
    public String profile(HttpSession session, Model model) {
        Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");
        model.addAttribute("user", user);
        return PROFILE_PATH;
    }

}
