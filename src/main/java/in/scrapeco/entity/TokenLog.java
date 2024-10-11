package in.scrapeco.entity;

import java.time.OffsetDateTime;

import in.scrapeco.entity.enums.TokenLogPurpose;
import in.scrapeco.entity.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "token_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenLog extends Audit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_log_id")
	private Integer id;

	@Column(name = "user_id", length = 256)
	private String userId;

	@Column(name = "user_type", columnDefinition = "enum('USER', 'ADMIN')")
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@Column(name = "purpose", columnDefinition = "enum('LOGIN', 'RESET')")
	@Enumerated(EnumType.STRING)
	private TokenLogPurpose purpose;

	@Column(name = "email", length = 256)
	private String email;

	@Column(name = "token", columnDefinition = "text")
	private String token;

	@Column(name = "ip", length = 128)
	private String ip;

	@Column(name = "attempt")
	private Integer attempt;

	@Column(name = "is_valid")
	private Integer isValid = 1;

	@Column(name = "expired_at")
	private OffsetDateTime expiredAt;

}
