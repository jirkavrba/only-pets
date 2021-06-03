package dev.vrba.onlypets.repository;

import dev.vrba.onlypets.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends CrudRepository<User, UUID> {
}
