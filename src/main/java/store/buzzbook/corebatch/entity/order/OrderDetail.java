package store.buzzbook.corebatch.entity.order;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import store.buzzbook.corebatch.entity.product.Product;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private int price;
	@NotNull
	private int quantity;
	@NotNull
	@Column(columnDefinition = "TINYINT(1)")
	private boolean wrap;

	@NotNull
	@Setter
	@ManyToOne
	@JoinColumn(referencedColumnName = "id", name = "order_status_id", nullable = false)
	private OrderStatus orderStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id", name = "wrapping_id")
	private Wrapping wrapping;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id", name = "product_id", nullable = false)
	private Product product;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id", name = "order_id", nullable = false)
	private Order order;

	@NotNull
	private LocalDateTime createAt;

	@NotNull
	private LocalDateTime updateAt;

	public void changeOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
		this.updateAt = LocalDateTime.now();
	}
}
