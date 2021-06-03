package dev.vrba.onlypets.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "discordId"),
    indexes = {
        @Index(name = "discord_id_index", columnList = "discordId", unique = true)
    }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private UUID id;

    private String discordId;

    private String username;

    private String avatar;

    @CreatedDate
    private Instant registeredAt;

    public User(@NotNull String discordId, @NotNull String username, @NotNull String avatar) {
        this.id = UUID.randomUUID();
        this.registeredAt = Instant.now();

        this.discordId = discordId;
        this.username = username;
        this.avatar = avatar;
    }
}
