package dev.vrba.onlypets.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "pets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    @Id
    private UUID id;

    private String name;

    private String avatar;

    @Nullable
    private Date birthdate;

    @Nullable
    private String paypalLink;

    @ManyToOne(optional = false)
    private User owner;

    public Pet(@NotNull String name, @NotNull String avatar, @Nullable Date birthdate, @Nullable String paypalLink) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.avatar = avatar;
        this.birthdate = birthdate;
        this.paypalLink = paypalLink;
    }
}
