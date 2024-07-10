package store.buzzbook.corebatch.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.corebatch.entity.user.User;
import store.buzzbook.corebatch.entity.user.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findAllByLastLoginAtBeforeAndStatus(LocalDateTime localDateTime, UserStatus status);
}
