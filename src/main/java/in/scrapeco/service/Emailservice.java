package in.scrapeco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import in.scrapeco.entity.OtpLog;
import in.scrapeco.entity.User;
import in.scrapeco.entity.dto.UserDto;
import in.scrapeco.entity.enums.UserType;
import in.scrapeco.utils.CommunicationUtil;

@Service
public class Emailservice {

	@Autowired
	private CommunicationUtil communicationUtil;

	@Autowired
	private LoggerService loggerService;

	@Value("${app.user.url.base}")
	private String userBaseUrl;

	@Value("${app.admin.url.base}")
	private String adminBaseUrl;

	@Value("${app.url.reset}")
	private String appResetUrl;

	@Value("${app.environment}")
	private String appEnvironment;

	@Value("${app.url.login}")
	private String loginUrl;

	public void forgotPassword(UserDto user, String token) {
		String to = user.getEmail();

		String appBaseUrl = userBaseUrl;
		if (user.getUserTypeEntity().getUserType().equals(UserType.ADMIN)) {
			appBaseUrl = adminBaseUrl;
		}

		String resetPasswordLink = appBaseUrl + appResetUrl;

		String link = resetPasswordLink.replace("{token}", token);
		String subject = "TransBnk : Forgot Password";
		String htmlBody = "Hi " + user.getFirstName() + user.getLastName() + ",<br/>" + "<br/>"
				+ "We received a request to reset your account password.<br/>" + "<br/>" + "<a href='" + link
				+ "'>Click here</a> to change your password.<br/>" + "<br/>" + "Didn't request this change?<br/>"
				+ "If you didn't request a new password, let us know.<br/>" + "<br/>" + "Regards,<br/>"
				+ "TransBnk Team";

		communicationUtil.sendMail(to, subject, "", htmlBody);
//        loggerService.info("Forgot Password", "forgotPassword", "");

	}

	public void sendPasswordToUser(User user, String token) {
		String to = user.getEmail();
		String password = user.getPassword();

		String appBaseUrl = userBaseUrl;
		if (user.getUserTypeEntity().getUserType().equals(UserType.ADMIN)) {
			appBaseUrl = adminBaseUrl;
		}

		// String resetPasswordLink = appBaseUrl + appResetUrl;

		String loginLink = appBaseUrl + loginUrl;

		// String link = resetPasswordLink.replace("{token}", token);
		String subject = "Welcome to TransBnk Solution Pvt. Ltd.";
		String htmlBody = "Hi " + user.getFirstName() + user.getLastName() + ",<br/>" + "<br/>" + "Welcome to " + "<b>"
				+ "TransBnk Solution Pvt. Ltd." + "</b>"
				+ "(Digital Banking Platform),use the below link & credentials to login.<br/>" + "<br/>" + "<b>"
				+ "URL:  " + "</b>" + loginLink + "<br/>" + "<br/>" + "<b>" + "Username:  " + "</b>" + to + "<br/>"
				+ "<b>" + "Password:  " + "</b>" + password + "<br/>" + "<br/>" + "Regards,<br/>" + "TransBnk Team";

		communicationUtil.sendMail(to, subject, "", htmlBody);
		loggerService.info("Send Password To User", "sendPasswordToUser", "");

	}

	public String sendCustomerOtp(UserDto user, OtpLog otpLog) {

		String htmlBody = "Hi " + user.getFirstName() + user.getLastName() + ",<br/>" + "<br/>" + otpLog.getOtp()
				+ " is your two factor authentication code. " + "<br/> code expires in 15 minutes." + "<br/>"
				+ "Regards,<br/>" + "TransBnk.<br/><br/>";

		String subject = "TransBnk : Verification Otp";

		communicationUtil.sendMail(user.getEmail(), subject, "", htmlBody);

		loggerService.info("Send Customer OTP", "sendCustomerOtp", "");
		return htmlBody;
	}
}
