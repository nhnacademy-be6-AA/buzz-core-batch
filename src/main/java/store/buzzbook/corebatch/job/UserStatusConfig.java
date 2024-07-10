package store.buzzbook.corebatch.job;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.buzzbook.corebatch.entity.user.User;
import store.buzzbook.corebatch.repository.UserRepository;
import store.buzzbook.corebatch.service.user.UserService;



@Configuration
@Slf4j
@RequiredArgsConstructor
public class UserStatusConfig {
	private final JobRepository jobRepository;
	private final UserRepository userRepository;
	private final PlatformTransactionManager transactionManager;
	private final UserService userService;

	@Bean("userStatusChangeJob")
	public Job userStatusChangeJob(Step userStatusChangeStep) {
		return new JobBuilder("userStatusChangeJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(userStatusChangeStep)
			.build();
	}

	@JobScope
	@Bean("userStatusChangeStep")
	public Step userStatusChangeStep(ItemReader<User> userReader,
		ItemProcessor<User, User> userStatusProcessor,
		ItemWriter<User> userStatusWriter) {
		return new StepBuilder("userStatusStep", jobRepository)
			.<User, User>chunk(500, transactionManager)
			.reader(userReader)
			.processor(userStatusProcessor)
			.writer(userStatusWriter)
			.build();
	}

	@StepScope
	@Bean
	public ItemReader<User> userReader() {
		log.info("user reader started");

		return new ItemReader<User>() {
			private final List<User> userList = userService.getUserListByLastLoginAt();
			private int nextUserIndex = 0;

			@Override
			public User read() {
				User nextUser = null;
				if (nextUserIndex < userList.size()) {
					nextUser = userList.get(nextUserIndex);
					nextUserIndex++;
				}
				return nextUser;
			}
		};
	}

	@StepScope
	@Bean
	public ItemProcessor<User, User> userStatusProcessor() {
		return user ->{
			user.dormant();
			return user;
		};
	}

	@StepScope
	@Bean
	public RepositoryItemWriter<User> userStatusWriter() {
		return new RepositoryItemWriterBuilder<User>()
			.repository(userRepository)
			.methodName("save")
			.build();
	}

}
