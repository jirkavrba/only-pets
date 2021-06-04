package dev.vrba.onlypets.entity.dto;

import dev.vrba.onlypets.entity.Pet;
import dev.vrba.onlypets.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PetDTO {
    @NotNull
    private final UUID id;

    @NotNull
    private final String name;

    @NotNull
    private final String avatar;

    @Nullable
    private final Date birthdate;

    @Nullable
    private final String paypalLink;

    public PetDTO(@NotNull Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.avatar = pet.getAvatar();
        this.birthdate = pet.getBirthdate();
        this.paypalLink = pet.getPaypalLink();
    }

    @Getter
    public static class WithOwner extends PetDTO {
        @NotNull
        private final UserDTO owner;

        public WithOwner(@NotNull Pet pet) {
            super(pet);
            this.owner = new UserDTO(pet.getOwner());
        }
    }
}
