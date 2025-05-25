package org.tg;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
public class AuthController {
    @Value("${telegram.bot_token}")
    private String token;

    private static final String ERROR_PATH = "error";
    private static final String PROFILE_PATH = "profile";

    public AuthController(EgoriottosBotService botService) {
        this.botService = botService;
    }

    private final EgoriottosBotService botService;

    @PostMapping("/auth/telegram")
    public String handleAuth(@RequestParam(value = "initData", required = false) String initData, HttpSession session) {
        try {
            if (!botService.validateTelegramData(initData, token)) {
                return "redirect:/" + ERROR_PATH;
            }
            Map<String, Object> userData = botService.parseInitData(initData);
            session.setAttribute("user", userData);

            return PROFILE_PATH;
        } catch (Exception e) {
            return "redirect:/" + ERROR_PATH;
        }
    }

    @GetMapping("/" + PROFILE_PATH)
    public String profile(HttpSession session, Model model) {
        Map<String, Object> user = (Map<String, Object>) session.getAttribute("user");
        if (user == null) {
            return "redirect:/" + ERROR_PATH;
        }
        model.addAttribute("user", user);
        return PROFILE_PATH;
    }

}
