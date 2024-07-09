package store.buzzbook.corebatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.corebatch.entity.user.Grade;
import store.buzzbook.corebatch.entity.user.GradeName;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
	Optional<Grade> findByName(GradeName name);
	List<Grade> findAll();
}
