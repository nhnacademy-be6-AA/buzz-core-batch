package store.buzzbook.corebatch.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.buzzbook.corebatch.dto.user.UserStatusInfo;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`user`")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Size(min = 6, max = 20)
	@Column(name = "login_id", unique = true)
	private String loginId;

	@NotNull
	@Size(min = 6, max = 15)
	@Column(name = "contact_number")
	private String contactNumber;

	@NotNull
	@Size(max = 20)
	private String name;

	@NotNull
	@Size(max = 255)
	@Email
	private String email;

	@NotNull
	@Size(max = 255)
	private String password;

	@NotNull
	private LocalDate birthday;

	@NotNull
	@Column(name = "created_at")
	private LocalDateTime createAt;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@NotNull
	@Enumerated(value = EnumType.STRING)
	private UserStatus status;

	@NotNull
	@Column(name = "modify_at")
	@Past
	private LocalDateTime modifyAt;

	@NotNull
	@Column(name = "is_admin")
	@ColumnDefault("false")
	private boolean isAdmin;

	public void dormant(){
		this.status = UserStatus.DORMANT;
	}
}
