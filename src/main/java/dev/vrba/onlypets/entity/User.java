package dev.vrba.onlypets.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
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
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    private String discordId;

    private String username;

    private String avatar;

    @CreatedDate
    private Instant registeredAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pet> pets;

    public User(@NotNull String discordId, @NotNull String username, @NotNull String avatar) {
        this.id = UUID.randomUUID();
        this.registeredAt = Instant.now();

        this.discordId = discordId;
        this.username = username;
        this.avatar = avatar;
    }
}
