package com.securefileshare.controller;

import com.securefileshare.model.User;
import com.securefileshare.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Plain text for now
            if (user.getPassword().equals(password)) {

                // ✅ SINGLE SOURCE OF TRUTH
                session.setAttribute("loggedInUser", user);

                return "redirect:/dashboard";
            }
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";
    }
}
