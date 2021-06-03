package dev.vrba.onlypets.controller;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String index(@Nullable Principal principal) {
        // User is not authenticated
        if (principal == null) {
            return "redirect:/oauth2/authorization/discord";
        }

        return "redirect:/app";
    }
}
