package store.buzzbook.corebatch.common.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {
	private final JobLauncher jobLauncher;
	private final Job gradeChangeJob;
	private final Job userStatusChangeJob;
	private final Job orderDetailChangeJob;

	@Scheduled(cron = "${schedule.user.grading.cron}")
	public void userGradingJob() throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("user-grading-job-execution-time", System.currentTimeMillis())
			.toJobParameters();

		jobLauncher.run(gradeChangeJob, jobParameters);
	}

	@Scheduled(cron = "${schedule.user.status.cron}")
	public void userStatusChangeJob() throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("user-status-change-job-execution-time", System.currentTimeMillis())
			.toJobParameters();

		jobLauncher.run(userStatusChangeJob, jobParameters);
	}

	@Scheduled(cron = "${schedule.order.shipped.cron}")
	public void orderDetailChangeJob() throws JobExecutionException {
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("order-detail-status-change-job-execution-time", System.currentTimeMillis())
			.toJobParameters();

		jobLauncher.run(orderDetailChangeJob, jobParameters);
	}
}
