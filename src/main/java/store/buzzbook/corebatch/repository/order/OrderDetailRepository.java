package store.buzzbook.corebatch.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.corebatch.entity.order.OrderDetail;
import store.buzzbook.corebatch.entity.order.OrderStatus;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
	List<OrderDetail> findAllByOrderStatus(OrderStatus orderStatus);
}
