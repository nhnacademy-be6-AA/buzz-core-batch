package store.buzzbook.corebatch.job;

import java.time.LocalDateTime;
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
import store.buzzbook.corebatch.common.exception.EmptyGradeTableException;
import store.buzzbook.corebatch.dto.user.UserRealBill;
import store.buzzbook.corebatch.dto.user.UserRealBillInfo;
import store.buzzbook.corebatch.dto.user.UserRealBillInfoDetail;
import store.buzzbook.corebatch.entity.payment.BillStatus;
import store.buzzbook.corebatch.entity.user.Grade;
import store.buzzbook.corebatch.entity.user.GradeLog;
import store.buzzbook.corebatch.entity.user.GradeName;
import store.buzzbook.corebatch.entity.user.User;
import store.buzzbook.corebatch.repository.GradeLogRepository;
import store.buzzbook.corebatch.repository.GradeRepository;
import store.buzzbook.corebatch.repository.UserRepository;
import store.buzzbook.corebatch.service.user.UserService;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GradeConfig {
	private final JobRepository jobRepository;
	private final UserRepository userRepository;
	private final GradeRepository gradeRepository;
	private final GradeLogRepository gradeLogRepository;
	private final PlatformTransactionManager transactionManager;
	private final UserService userService;

	@Bean("gradeChangeJob")
	public Job gradeChangeJob(Step gradeChangeStep) {

		return new JobBuilder("gradeChangeJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(gradeChangeStep)
			.build();
	}

	@JobScope
	@Bean("gradeChangeStep")
	public Step gradeChangeStep(ItemReader<UserRealBill> userRealBillItemReaderReader,
		ItemProcessor<UserRealBill, GradeLog> userGradeProcessor,
		ItemWriter<GradeLog> userGradeLogWriter) {
		return new StepBuilder("birthdayCouponStep", jobRepository)
			.<UserRealBill, GradeLog>chunk(500, transactionManager)
			.reader(userRealBillItemReaderReader)
			.processor(userGradeProcessor)
			.writer(userGradeLogWriter)
			.build();
	}

	@StepScope
	@Bean
	public ItemReader<UserRealBill> userRealBillItemReaderReader() {
		log.info("user real bill reader started");

		return new ItemReader<UserRealBill>() {
			private final List<UserRealBill> bills = userService.getUserRealBills();
			private int nextUserIndex = 0;

			@Override
			public UserRealBill read() {
				UserRealBill nextUser = null;
				if (nextUserIndex < bills.size()) {
					nextUser = bills.get(nextUserIndex);
					nextUserIndex++;
				}
				return nextUser;
			}
		};
	}

	@StepScope
	@Bean
	public ItemProcessor<UserRealBill, GradeLog> userGradeProcessor() {

		return bill ->{
			GradeName gradeName = calculateGradeName(bill);
			Grade targetGrade = gradeRepository.findByName(gradeName).orElseThrow(EmptyGradeTableException::new);
			User targetUser = userRepository.getReferenceById(bill.getUserId());

			return GradeLog.builder()
				.grade(targetGrade)
				.user(targetUser)
				.changeAt(LocalDateTime.now()).build();
		};
	}

	@StepScope
	@Bean
	public RepositoryItemWriter<GradeLog> userGradeLogWriter() {
		return new RepositoryItemWriterBuilder<GradeLog>()
			.repository(gradeLogRepository)
			.methodName("save")
			.build();
	}


	private GradeName calculateGradeName(UserRealBill bill) {
		List<UserRealBillInfo> billList = bill.getUserRealBillInfoList();
		int totalRealPay = 0;

		for (int i = 0; i < billList.size(); i++) {
			UserRealBillInfo billInfo = billList.get(i);

			totalRealPay -= billInfo.getDeliveryRate();

			List<UserRealBillInfoDetail> detailList = billInfo.getDetailList();
			for (int j = 0; j < detailList.size(); j++) {
				UserRealBillInfoDetail detail = detailList.get(j);

				if(detail.getStatus().equals(BillStatus.DONE)){
					totalRealPay += detail.getPrice();
				}else {
					totalRealPay -= detail.getPrice();
				}

			}
		}

		List<Grade> gradeList = gradeRepository.findAll();

		if(gradeList.isEmpty()){
			log.warn("등급 테이블이 텅텅 비어있습니다.");
			throw new EmptyGradeTableException();
		}


		GradeName gradeName = GradeName.NORMAL;
		for (Grade grade : gradeList){
			if(grade.getStandard() <= totalRealPay){
				gradeName = grade.getName();
			}
		}
		return gradeName;
	}
}
