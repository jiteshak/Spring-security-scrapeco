package in.scrapeco.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "otp_log")
public class OtpLog extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "otp_log_id")
	private Integer otpLogId;

	@NotNull
	@Column(name = "user_id", length = 256)
	private String userId;

	@Column(name = "email", length = 256)
	private String email;

	@NotNull
	@Column(name = "otp", length = 12)
	private String otp;

	@Column(name = "otp_text", columnDefinition = "text")
	private String otpText;

	@Column(name = "otp_end_at")
	private OffsetDateTime otpEndAt;

	@Column(name = "otp_attempt")
	private Integer otpAttempt = 0;

	@NotNull
	@Column(name = "session_closed", columnDefinition = "tinyint(1) DEFAULT 0")
	private Integer sessionClosed = 0;
}
