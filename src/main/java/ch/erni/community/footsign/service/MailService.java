package ch.erni.community.footsign.service;

import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.enums.MailType;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by veda on 3/19/2015.
 */
@Service
public class MailService {

    private static final Logger logger = Logger.getLogger(MailService.class.getName());

    @Autowired
    ErniLdapCache erniLdapCache;

    @Autowired
    MatchRepository matchRepository;

	private JavaMailSender javaMailSender;

    @Autowired
	private SpringTemplateEngine springTemplateEngine;

	@Autowired
	MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendCancelationMail(Match match) {
        Set<User> team = new HashSet<>();
        team.addAll(match.getTeam1());
        team.addAll(match.getTeam2());
        team.stream().filter(User::isCancelledMatchNotification).collect(Collectors.toList()).forEach(user -> {
            final Context ctx = new Context();
            ctx.setVariable("user_name", user.getFullName());
            ctx.setVariable("day_of_match", match.getFormatedDateOfMatch());
            ctx.setVariable("team1", match.getTeam1());
            ctx.setVariable("team2", match.getTeam2());
            ctx.setVariable("games", match.getGames());
            sendMail(MailType.CANCELATION_MAIL, user.getEmail(), ctx);
        });

    }

    public void sendConfirmationMail(Match match) {

        Set<User> team = new HashSet<>();
        team.addAll(match.getTeam1());
        team.addAll(match.getTeam2());
        team.stream().filter(User::isConfirmMatchNotification).collect(Collectors.toList()).forEach(user -> {
            final Context ctx = new Context();
            ctx.setVariable("user_name", user.getFullName());
            ctx.setVariable("day_of_match", match.getFormatedDateOfMatch());
            ctx.setVariable("team1", match.getTeam1());
            ctx.setVariable("team2", match.getTeam2());
            ctx.setVariable("games", match.getGames());
            sendMail(MailType.CONFIRMATION_MAIL, user.getEmail(), ctx);
        });
    }

    public void sendPlaneMatchMail(Match match) throws MessagingException {
        Set<User> team = new HashSet<>();
        team.addAll(match.getTeam1());
        team.addAll(match.getTeam2());
        team.stream().filter(User::isPlannedMatchNofitication).collect(Collectors.toList()).forEach(user -> {
            final Context ctx = new Context();
            ctx.setVariable("user_name", user.getFullName());
            ctx.setVariable("day_of_match", match.getFormatedDateOfMatch());
            ctx.setVariable("team1", match.getTeam1());
            ctx.setVariable("team2", match.getTeam2());
			sendMail(MailType.PLANNED_MAIL, user.getEmail(), ctx);
		});
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
		} else if (MailType.PLANNED_MAIL.equals(mailType)) {
			helper.setSubject("Planning mail");
        } else if (MailType.CANCELATION_MAIL.equals(mailType)){
            helper.setSubject("Cancelation mail");
        } else {
            helper.setSubject("Information mail");
        }
    }
}
