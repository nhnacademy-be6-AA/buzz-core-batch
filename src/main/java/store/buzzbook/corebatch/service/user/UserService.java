package store.buzzbook.corebatch.service.user;

import java.util.List;

import store.buzzbook.corebatch.dto.user.UserRealBill;
import store.buzzbook.corebatch.dto.user.UserStatusInfo;
import store.buzzbook.corebatch.entity.user.User;

public interface UserService {
	List<UserRealBill> getUserRealBills();

	List<User> getUserListByLastLoginAt();
}
