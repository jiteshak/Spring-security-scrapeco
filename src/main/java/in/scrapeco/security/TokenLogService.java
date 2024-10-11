package in.scrapeco.security;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import in.scrapeco.entity.TokenLog;
import in.scrapeco.entity.enums.TokenLogPurpose;
import in.scrapeco.entity.enums.UserType;
import in.scrapeco.exception.UnauthorizedException;
import in.scrapeco.repository.TokenLogRepository;
import in.scrapeco.utils.SecurityUtil;

@Service
public class TokenLogService {

	@Value("${app.otp.attempt}")
	Integer maxOtpAttempt;

	@Autowired
	private TokenLogRepository tokenLogRepository;

	public int createLoginLog(String userId, String email, String userType, OffsetDateTime expirationDate, String ip) {

		TokenLog tokenLog = new TokenLog();

		UserType tokenLogUserType = userType.equals("ADMIN") ? UserType.ADMIN : UserType.USER;

		tokenLog.setUserId(userId + "");
		tokenLog.setUserType(tokenLogUserType);

		tokenLog.setPurpose(TokenLogPurpose.LOGIN);
		tokenLog.setEmail(email);
		tokenLog.setIp(ip);
		tokenLog.setToken("");
		tokenLog.setAttempt(1);
		tokenLog.setExpiredAt(expirationDate);
		tokenLog.setIsValid(1);
		tokenLog.setCreatedAt(OffsetDateTime.now());
		tokenLog = tokenLogRepository.save(tokenLog);

		return tokenLog.getId();
	}

	public TokenLog getTokenLogById(int id) {

		Optional<TokenLog> tokenLog = tokenLogRepository.findById(id);

		if (!tokenLog.isPresent()) {
			throw new UnauthorizedException("Unauthorized");
		}

		return tokenLog.get();
	}

	public void closeLoginLog(Integer id) {

		TokenLog tokenLog = getTokenLogById(id);
		tokenLog.setExpiredAt(OffsetDateTime.now());
		tokenLog.setIsValid(2);
		tokenLogRepository.save(tokenLog);
	}

	public String createResetLog(Long userId, String email, UserType userType, String remoteAddress) {
		Optional<TokenLog> tokenLog = tokenLogRepository.findFirstByUserIdAndUserTypeAndPurposeAndIsValid(userId + "",
				userType, TokenLogPurpose.RESET, 1);

		if (tokenLog.isPresent()) {
			TokenLog tokenLog1 = tokenLog.get();
			if (OffsetDateTime.now().isBefore(tokenLog1.getExpiredAt())) {
				throw new ServiceException(
						"Password recovery mail already sent. Please check your spam or wait for 15  min for new reset request");
			}

			tokenLog1.setIsValid(0);
			tokenLogRepository.save(tokenLog1);
		}
		OffsetDateTime expirationDate = OffsetDateTime.now().plusMinutes(15L);

		TokenLog tl = new TokenLog();
		tl.setUserId(userId + "");
		tl.setIp(remoteAddress);
		tl.setUserType(userType);
		tl.setPurpose(TokenLogPurpose.RESET);
		tl.setEmail(email);
		tl.setToken(SecurityUtil.generateOpaque(userId));
		tl.setExpiredAt(expirationDate);
		tl.setAttempt(0);
		tl.setIsValid(1);
		tl.setCreatedAt(OffsetDateTime.now());
		tl = tokenLogRepository.save(tl);

		return tl.getToken();
	}

	public TokenLog verifyTokenLogByToken(String token, String remoteAddress) {
		Optional<TokenLog> tokenLog0 = tokenLogRepository.findFirstByToken(token);

		if (!tokenLog0.isPresent()) {
			throw new ServiceException("Link has been expired or invalid");
		}

		TokenLog tokenLog = tokenLog0.get();

		if (tokenLog.getIsValid() != 1 || tokenLog.getExpiredAt().isBefore(OffsetDateTime.now())) {
			if (tokenLog.getUserType().equals(UserType.ADMIN)) {
				throw new ServiceException("OTP is invalid or expired");
			} else {
				throw new ServiceException("Link has been expired or invalid");
			}
		}

		this.setTokenLogAttempted(tokenLog);
		if (tokenLog.getAttempt() > maxOtpAttempt) {
			throw new ServiceException("You have exceeded login attempts");
		}

		this.setTokenLogUsed(tokenLog, remoteAddress);
		return tokenLog;
	}

	public void setTokenLogAttempted(TokenLog tokenLog) {
		tokenLog.setAttempt(tokenLog.getAttempt() + 1);
		tokenLogRepository.save(tokenLog);
	}

	public void setTokenLogUsed(TokenLog tokenLog, String remoteAddress) {
		tokenLog.setIsValid(2);
		tokenLog.setIp(remoteAddress);
		tokenLogRepository.save(tokenLog);
	}
}
