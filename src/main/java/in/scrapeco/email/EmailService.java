package in.scrapeco.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import in.scrapeco.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Async // Send email asynchronously
	public void sendVerificationEmail(User user, String token) {
		String subject = "Verify your email";
		String confirmationUrl = "http://localhost:8090/api/auth/confirm?token=" + token;
		String emailContent = buildEmail(user.getUsername(), confirmationUrl);

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(user.getEmail());
			helper.setSubject(subject);
			helper.setText(emailContent, true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private String buildEmail(String name, String link) {
		return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n"
				+ "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
				+ "    <tbody><tr>\n" + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n"
				+ "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
				+ "          <tbody><tr>\n" + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
				+ "              <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
				+ "                <tbody><tr>\n" + "                  <td style=\"padding-left:10px\">\n"
				+ "                    <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block;font-size:24px\">Confirm your email</span>\n"
				+ "                  </td>\n" + "                </tr>\n" + "              </tbody></table>\n"
				+ "            </td>\n" + "          </tr>\n" + "        </tbody></table>\n" + "      </td>\n"
				+ "    </tr>\n" + "  </tbody></table>\n"
				+ "  <table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
				+ "    <tbody><tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n" + "    <tr>\n"
				+ "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.6;color:#333333\">\n"
				+ "        <p style=\"margin:0\">Hi " + name + ",</p>\n"
				+ "        <p style=\"margin:16px 0\">Thank you for registering. Please click on the link below to activate your account:</p>\n"
				+ "        <blockquote style=\"border-left:10px solid #b1b4b6;padding:15px 15px;font-size:19px;color:#0044cc\">\n"
				+ "          <p style=\"margin:0\"><a href=\"" + link
				+ "\" style=\"color:#0044cc;text-decoration:none;font-weight:bold\">Activate Now</a></p>\n"
				+ "        </blockquote>\n"
				+ "        <p style=\"margin:16px 0\">This link will expire in 15 minutes. We look forward to having you onboard!</p>\n"
				+ "        <p style=\"margin:24px 0\">Thanks and Regards,<br>Scrapeco Team</p>\n" + "      </td>\n"
				+ "    </tr>\n" + "    <tr>\n" + "      <td height=\"30\"><br></td>\n" + "    </tr>\n"
				+ "  </tbody></table>\n" + "</div>";
	}

}
