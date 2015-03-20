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


/**
 * Created by veda on 3/19/2015.
 */
@Service
public class MailService {

    private JavaMailSender javaMailSender;

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

    public void sendConfirmationMail(Match match) throws UserNotFoundException, MessagingException {

        Set<User> team = new HashSet<>();
        team.addAll(match.getTeam1());
        team.addAll(match.getTeam2());
        //User user = team.iterator().next();
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

    //todo for plan matches
    public void sendPlaneMatchMail(String recipientShortDomainName) {
        final IContext ctx= new Context();
    }


    public void sendMail(MailType mailType, String recepientMail, IContext ctx) throws MessagingException{
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setFrom("footsign@example.com");
        if(MailType.CONFIRMATION_MAIL.equals(mailType)){
            message.setSubject("Confirmation mail");
        }else if(MailType.CONFIRMATION_MAIL.equals(mailType)){
            message.setSubject("Planning mail");
        }else {
            message.setSubject("Information mail");
        }
        message.setTo(recepientMail);

        final String text = springTemplateEngine.process(mailType.getValue(), ctx);
        message.setText(text, true); // true = isHtml

        this.javaMailSender.send(mimeMessage);
    }
}
