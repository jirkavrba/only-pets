package dev.vrba.onlypets.controller;

import dev.vrba.onlypets.entity.Pet;
import dev.vrba.onlypets.entity.dto.PetDTO;
import dev.vrba.onlypets.repository.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/pets")
public class PetsController {

    private final PetsRepository repository;

    @Autowired
    public PetsController(PetsRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<PetDTO>> index() {
        return ResponseEntity.ok(
            StreamSupport.stream(this.repository.findAll().spliterator(), true)
                .map(PetDTO.WithOwner::new)
                .collect(Collectors.toList())
        );
    }
}
