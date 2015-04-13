package ch.erni.community.ldap.data;

/**
 * @author rap
 */
public class ErniLdapConstants {
	public static final String ERNI_EMPLOYEES_USERS_GROUP_DN = "OU=User,OU=ERNI Consulting,DC=erni2,DC=ch";

	public static final String FILTER = "(objectCategory=Person)";

	public static final String HOST = "dunajec.erni2.ch";

	public static final int PORT = 389;

	public enum Encryption {
		NO_ENCRYPTION, SSL, START_TLS
	}
}
