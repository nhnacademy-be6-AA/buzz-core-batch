package store.buzzbook.corebatch.job;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import store.buzzbook.corebatch.entity.order.OrderDetail;
import store.buzzbook.corebatch.entity.order.OrderStatus;
import store.buzzbook.corebatch.repository.order.OrderDetailRepository;
import store.buzzbook.corebatch.repository.order.OrderStatusRepository;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrderConfig {
	private static final String SHIPPING_OUT = "SHIPPING_OUT";
	private static final String SHIPPED = "SHIPPED";
	private static final int SHIPPING_OUT_DAYS = 5;

	private final JobRepository jobRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final OrderStatusRepository orderStatusRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean("orderDetailChangeJob")
	public Job orderDetailChangeJob(Step orderDetailChangeStep) {
		return new JobBuilder("orderDetailChangeStep", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(orderDetailChangeStep).build();
	}

	@JobScope
	@Bean("orderDetailChangeStep")
	public Step orderDetailChanageStep(ItemReader<OrderDetail> orderDetailItemReader,
		ItemProcessor<OrderDetail, OrderDetail> orderDetailItemProcessor,
		ItemWriter<OrderDetail> orderDetailItemWriter) {
		return new StepBuilder("orderDetailChangeStep", jobRepository)
			.<OrderDetail, OrderDetail>chunk(500, transactionManager)
			.reader(orderDetailItemReader)
			.processor(orderDetailItemProcessor)
			.writer(orderDetailItemWriter).build();
	}

	@StepScope
	@Bean
	public ItemReader<OrderDetail> orderDetailItemReader() {
		return new ItemReader<OrderDetail>() {
			private final OrderStatus orderStatus = orderStatusRepository.findByName(SHIPPING_OUT);
			private final List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderStatus(orderStatus);
			private int nextOrderDetailIndex = 0;

			@Override
			public OrderDetail read() {
				OrderDetail nextOrderDetail = null;
				if (nextOrderDetailIndex < orderDetails.size()) {
					nextOrderDetail = orderDetails.get(nextOrderDetailIndex);
					nextOrderDetailIndex++;
				}
				return nextOrderDetail;
			}
		};
	}

	@StepScope
	@Bean
	public ItemProcessor<OrderDetail, OrderDetail> orderDetailItemProcessor() {
		return orderDetail -> {
			if (isCreatedBeforeDays(orderDetail.getUpdateAt(), SHIPPING_OUT_DAYS)) {
				orderDetail.changeOrderStatus(orderStatusRepository.findByName(SHIPPED));
			}

			return orderDetail;
		};
	}

	private static boolean isCreatedBeforeDays(LocalDateTime createAt, int sub) {
		LocalDateTime daysAgo = LocalDateTime.now().minus(sub, ChronoUnit.DAYS);
		return createAt.isBefore(daysAgo);
	}

	@StepScope
	@Bean
	public RepositoryItemWriter<OrderDetail> orderDetailItemWriter() {
		return new RepositoryItemWriterBuilder<OrderDetail>()
			.repository(orderDetailRepository)
			.methodName("save")
			.build();
	}
}
