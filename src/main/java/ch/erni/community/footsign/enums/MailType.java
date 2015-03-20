package ch.erni.community.footsign.enums;

/**
 * Created by veda on 3/20/2015.
 */
public enum MailType {

    CONFIRMATION_MAIL("confirmation_mail"),
    PLANED_MAIL("planed_mail");

    private String mailType;

    private MailType(String type){
        mailType = type;
    }

    public String getValue() {
        return mailType;
    }

}
