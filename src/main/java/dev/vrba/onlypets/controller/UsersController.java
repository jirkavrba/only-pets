package dev.vrba.onlypets.controller;

import dev.vrba.onlypets.entity.User;
import dev.vrba.onlypets.entity.dto.UserDTO;
import dev.vrba.onlypets.exception.EntityNotFoundException;
import dev.vrba.onlypets.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersRepository repository;

    @Autowired
    public UsersController(UsersRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> profile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.WithPets> user(@PathVariable("id") UUID id) {
        User user = this.repository.findById(id)
                        .orElseThrow(EntityNotFoundException::new);

        return ResponseEntity.ok(new UserDTO.WithPets(user));
    }
}
