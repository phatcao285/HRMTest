package com.phat.mail;

import com.phat.helpers.PropertiesHelper;
import com.phat.helpers.SystemHelper;
import com.phat.utils.LogUtils;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Properties;

public class EmailSender {
    public static void sendMail(String OS, String browser, int total, int pass, int fail, int skip) {
        final String fromEmail = PropertiesHelper.getValue("FROM_MAIL");
        final String password = PropertiesHelper.getValue("APP_PASSWORD");
        final String toEmail = PropertiesHelper.getValue("TO_MAIL");

        Properties props = new Properties();
        props.put("mail.smtp.host", PropertiesHelper.getValue("SERVER"));
        props.put("mail.smtp.port", PropertiesHelper.getValue("PORT"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("📌 [Automation] HRM Regression Report ");
            String mailBody = """
                    <p>Hi,</p>
                    <p>This is the result for automation testing:</p>
                    <b>OS:</b> %s<br/>
                           <b>Browser:</b> %s</p>
                    <table border="1" cellspacing="0" cellpadding="6" style="border-collapse:collapse; text-align:center;">
                      <tr style="background-color:#f2f2f2;">
                        <th>Test Summary</th>
                        <th>Quantity</th>
                      </tr>
                      <tr>
                        <td>📊 <b>Total TCs</b></td>
                        <td>%d</td>
                      </tr>
                      <tr style="color:green;">
                        <td>✅ <b>Passed TCs</b></td>
                        <td>%d</td>
                      </tr>
                      <tr style="color:red;">
                        <td>❌ <b>Failed TCs</b></td>
                        <td>%d</td>
                      </tr>
                      <tr style="color:orange;">
                        <td>⚠ <b>Skipped TCs</b></td>
                        <td>%d</td>
                      </tr>
                    </table>
                    
                    <br/>
                    <p>Please read the report in the attachment.</p>
                    <p>Regards,<br/>Minh Long</p>
                    """.formatted(OS,browser,total, pass, fail, skip);

            // Nội dung text
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(mailBody, "text/html; charset=UTF-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            // 📂 Lấy tất cả file trong thư mục screenshots
            File folder = new File(SystemHelper.getCurrentDir() + "allure-report");
            File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".html"));

            if (listOfFiles != null && listOfFiles.length > 0) {
                for (File file : listOfFiles) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(file);
                    multipart.addBodyPart(attachmentPart);
                }
            } else {
                LogUtils.info("⚠ Not found any file in path" + SystemHelper.getCurrentDir() + "allure-report");
            }

            // set multipart vào email
            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
