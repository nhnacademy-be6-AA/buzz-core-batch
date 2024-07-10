package store.buzzbook.corebatch.service.user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.buzzbook.corebatch.adapter.UserAdapter;
import store.buzzbook.corebatch.dto.user.UserRealBill;
import store.buzzbook.corebatch.entity.user.User;
import store.buzzbook.corebatch.entity.user.UserStatus;
import store.buzzbook.corebatch.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserAdapter userAdapter;
	private final UserRepository userRepository;

	@Value("${schedule.user.status.limit}")
	private int loginLimit;

	@Override
	public List<UserRealBill> getUserRealBills() {
		return userAdapter.get3MonthBills().getBody();
	}

	@Override
	public List<User> getUserListByLastLoginAt() {
		LocalDateTime limitTime = LocalDateTime.now().minusMonths(loginLimit);
		return userRepository.findAllByLastLoginAtBeforeAndStatus(limitTime, UserStatus.ACTIVE);
	}
}
