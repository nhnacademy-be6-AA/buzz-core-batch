package store.buzzbook.corebatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import store.buzzbook.corebatch.entity.user.Grade;
import store.buzzbook.corebatch.repository.GradeLogRepository;
import store.buzzbook.corebatch.repository.GradeRepository;
import store.buzzbook.corebatch.repository.UserRepository;


@Configuration
@RequiredArgsConstructor
public class GradeConfig {
	private final UserRepository userRepository;
	private final GradeRepository gradeRepository;
	private final GradeLogRepository gradeLogRepository;


}
