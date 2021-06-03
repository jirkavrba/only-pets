package dev.vrba.onlypets.entity.dto;

import dev.vrba.onlypets.entity.Pet;
import dev.vrba.onlypets.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDTO {

    private final UUID id;

    private final String discordId;

    private final String username;

    private final String avatar;

    public UserDTO(@NotNull User user) {
        this.id = user.getId();
        this.discordId = user.getDiscordId();
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
    }

    @Getter
    public static class WithPets extends UserDTO {

        private final List<Pet> pets;

        public WithPets(@NotNull User user) {
            super(user);
            this.pets = user.getPets();
        }
    }
}
