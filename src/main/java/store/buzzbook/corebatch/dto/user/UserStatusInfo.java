package store.buzzbook.corebatch.dto.user;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import store.buzzbook.corebatch.entity.user.UserStatus;


@Builder
@Getter
public class UserStatusInfo {
	private Long id;
	private UserStatus status;
}
