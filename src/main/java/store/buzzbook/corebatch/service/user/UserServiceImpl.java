package store.buzzbook.corebatch.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.buzzbook.corebatch.adapter.UserAdapter;
import store.buzzbook.corebatch.dto.user.UserRealBill;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserAdapter userAdapter;

	@Override
	public List<UserRealBill> getUserRealBills() {
		return userAdapter.get3MonthBills().getBody();
	}
}
