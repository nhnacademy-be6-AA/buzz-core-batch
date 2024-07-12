package store.buzzbook.corebatch.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import store.buzzbook.corebatch.entity.order.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
	public OrderStatus findByName(String name);
}
