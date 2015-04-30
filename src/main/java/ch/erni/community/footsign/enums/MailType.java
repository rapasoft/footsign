package ch.erni.community.footsign.enums;

/**
 * Created by veda on 3/20/2015.
 */
public enum MailType {

    CONFIRMATION_MAIL("confirmation_mail"),
	PLANNED_MAIL("planned_mail"),
	CANCELATION_MAIL("cancelation_mail");

    private String mailType;

	MailType(String type) {
		mailType = type;
    }

    public String getValue() {
        return mailType;
    }

}
