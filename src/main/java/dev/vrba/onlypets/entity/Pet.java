package dev.vrba.onlypets.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
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

    @Nullable
    private String paypalLink;

    @ManyToOne(optional = false)
    private User owner;

    public Pet(@NotNull String name, @Nullable String paypalLink) {
        this.name = name;
        this.paypalLink = paypalLink;
    }
}