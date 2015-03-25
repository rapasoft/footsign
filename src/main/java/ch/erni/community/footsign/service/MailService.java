package ch.erni.community.footsign.service;

import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.enums.MailType;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by veda on 3/19/2015.
 */
@Service
public class MailService {

    private JavaMailSender javaMailSender;

    private static final Logger logger = Logger.getLogger(MailService.class.getName());

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    ErniLdapCache erniLdapCache;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendConfirmationMail(Match match) {

        Set<User> team = new HashSet<>();
        team.addAll(match.getTeam1());
        team.addAll(match.getTeam2());
        for(User user : team) {
            final Context ctx = new Context();
            ctx.setVariable("user_name", user.getFullName());
            ctx.setVariable("day_of_match", match.getFormatedDateOfMatch());
            ctx.setVariable("team1", match.getTeam1());
            ctx.setVariable("team2", match.getTeam2());
            ctx.setVariable("games", match.getGames());
            sendMail(MailType.CONFIRMATION_MAIL, user.getEmail(), ctx);
        }
    }

    public void sendPlaneMatchMail(Match match) throws MessagingException {
        Set<User> team = new HashSet<>();
        team.addAll(match.getTeam1());
        team.addAll(match.getTeam2());
        for(User user : team) {
            final Context ctx = new Context();
            ctx.setVariable("user_name", user.getFullName());
            ctx.setVariable("day_of_match", match.getFormatedDateOfMatch());
            ctx.setVariable("team1", match.getTeam1());
            ctx.setVariable("team2", match.getTeam2());
            sendMail(MailType.PLANED_MAIL, user.getEmail(), ctx);
        }
    }

    public void sendMail(MailType mailType, String recepientMail, IContext ctx) {
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            setSubjectForMail(mailType, message);
            message.setFrom("footsign@erni.sk");
            message.setTo(recepientMail);

            final String text = springTemplateEngine.process(mailType.getValue(), ctx);
            message.setText(text, true); // true = isHtml

            this.javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "Send Mail Exception", e);
        }
    }

    private void setSubjectForMail(MailType mailType, MimeMessageHelper helper) throws MessagingException {
        if(MailType.CONFIRMATION_MAIL.equals(mailType)){
            helper.setSubject("Confirmation mail");
        }else if(MailType.PLANED_MAIL.equals(mailType)){
            helper.setSubject("Planning mail");
        }else {
            helper.setSubject("Information mail");
        }
    }
}