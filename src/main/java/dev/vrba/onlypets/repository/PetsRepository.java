package dev.vrba.onlypets.repository;

import dev.vrba.onlypets.entity.Pet;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetsRepository extends PagingAndSortingRepository<Pet, UUID> {
}
