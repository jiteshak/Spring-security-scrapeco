package in.scrapeco.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import in.scrapeco.service.LoggerService;
import jakarta.mail.internet.MimeMessage;


@Service
@RequestScope
public class CommunicationUtil {

    @Value("${mail.type}")
    private String mailType;

    @Value("${spring.mail.from}")
    private String mailFrom;

    @Value("${mail.url}")
    private String mailUrl;

    @Value("${spring.mail.from.name}")
    private String mailFromName;

    @Value("${mail.apikey}")
    private String mailApiKey;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private LoggerService loggerService;

    @Async
    public void sendMail(String to, String subject, String textBody, String htmlBody) {
        try {
            if (mailType.equalsIgnoreCase("smtp")) {
                sendMailSmtp(to, subject, textBody, htmlBody);
            } else if (mailType.equalsIgnoreCase("rest")) {
                sendMailRest(to, subject, textBody, htmlBody);
            } else if (mailType.equalsIgnoreCase("aws")) {
                sendMailAws(to, subject, textBody, htmlBody);
            }
        } catch (Exception ex) {
           loggerService.error("Mail sending error : " + ex.getMessage(), "sendMail", "");
        }
    }

    private void sendMailSmtp(String to, String subject, String textBody, String htmlBody) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);

            helper.setTo(to);
            helper.setFrom(mailFrom);
            helper.setSubject(subject);

            if (!textBody.isEmpty()) {
                helper.setText(textBody);
            }
            if (!htmlBody.isEmpty()) {
                helper.setText(htmlBody, true);
            }
            loggerService.info("mail smtp : [to=" + to + ", sendMailSmtp, subject=" + subject + "]", "sendMailSmtp", "");
            javaMailSender.send(msg);
        } catch (Exception ex) {
            loggerService.error("mail error : " + ex.getMessage(), "sendMailSmtp", "");
        }
    }

    private void sendMailRest(String to, String subject, String textBody, String htmlBody) {
        try{
            String body = htmlBody;
            if(!textBody.isEmpty()){
                body = textBody;
            }
            body = body.replace('"','\'').replaceAll("\n","").replaceAll("\t","");

            String url = mailUrl;

            String requestJson = "{\"personalisations\":[{"
                    + "\"recipient\":\"" + to + "\"}],"
                    + "\"from\":{\"fromEmail\":\"" + mailFrom + "\",\"fromName\":\"" + mailFromName + "\"},"
                    + "\"subject\":\"" + subject + "\",\"content\":\"" + body + "\"}";

            loggerService.info("Mail rest : " + requestJson, "sendMailRest", ""); 

            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("api_key", mailApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(requestJson, headers);
            String result = rt.postForObject(url, request,String.class);
        
            loggerService.info("Mail success : " + result, "sendMailRest", ""); 
        } catch (Exception ex){
        }
    }

    private void sendMailAws(String to, String subject, String textBody, String htmlBody) {
        try{
        }catch (Exception ex){}
    }
}


