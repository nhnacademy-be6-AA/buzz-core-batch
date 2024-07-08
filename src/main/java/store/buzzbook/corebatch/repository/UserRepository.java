package store.buzzbook.corebatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.corebatch.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
