package store.buzzbook.corebatch.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import store.buzzbook.corebatch.entity.payment.BillStatus;

@Getter
@Builder
@AllArgsConstructor
public class UserRealBillInfoDetail {
	private BillStatus status;
	private int price;
}
