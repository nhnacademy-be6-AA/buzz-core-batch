package store.buzzbook.corebatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.corebatch.entity.user.Grade;

public interface GradeRepository extends JpaRepository<Grade, Integer> {

}
