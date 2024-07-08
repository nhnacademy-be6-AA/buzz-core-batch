package store.buzzbook.corebatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.corebatch.entity.user.GradeLog;

public interface GradeLogRepository extends JpaRepository<GradeLog, Long> {
}
