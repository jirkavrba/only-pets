package dev.vrba.onlypets.controller;

import dev.vrba.onlypets.entity.Pet;
import dev.vrba.onlypets.entity.User;
import dev.vrba.onlypets.entity.dto.PetDTO;
import dev.vrba.onlypets.exception.EntityNotFoundException;
import dev.vrba.onlypets.repository.PetsRepository;
import dev.vrba.onlypets.repository.UsersRepository;
import dev.vrba.onlypets.service.ImageUploadingService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/pets")
public class PetsController {

    private final PetsRepository petsRepository;

    private final UsersRepository usersRepository;

    private final ImageUploadingService imageUploadingService;

    @Autowired
    public PetsController(
            PetsRepository petsRepository,
            UsersRepository usersRepository,
            ImageUploadingService imageUploadingService
    ) {
        this.petsRepository = petsRepository;
        this.usersRepository = usersRepository;
        this.imageUploadingService = imageUploadingService;
    }

    @GetMapping
    public ResponseEntity<List<PetDTO>> index() {
        return ResponseEntity.ok(
                StreamSupport.stream(this.petsRepository.findAll().spliterator(), true)
                        .map(PetDTO.WithOwner::new)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO.WithOwner> pet(@PathVariable UUID id) {
        return this.petsRepository.findById(id)
                .map(PetDTO.WithOwner::new)
                .map(ResponseEntity::ok)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Getter
    @AllArgsConstructor
    private static class RegisterPetRequest {
        @NotEmpty
        @Length(min = 3, max = 32)
        private final String name;

        @NotNull
        private final MultipartFile avatar;

        @Nullable
        private final Date birthdate;

        @Nullable
        private final String paypalLink;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute RegisterPetRequest request
    ) {
        UUID id = UUID.randomUUID();
        String avatar = this.imageUploadingService.uploadAvatar(request.getAvatar(), id);

        Pet pet = this.petsRepository.save(new Pet(
                request.getName(),
                avatar,
                request.getBirthdate(),
                request.getPaypalLink()
        ));

        user.getPets().add(pet);
        this.usersRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new PetDTO(pet));
    }
}
