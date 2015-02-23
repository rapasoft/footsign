package ch.erni.community.ldap;

import ch.erni.community.ldap.data.Credentials;
import ch.erni.community.ldap.data.ErniLdapConstants;
import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.extensions.StartTLSExtendedRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

import javax.net.SocketFactory;
import java.util.logging.Logger;

/**
 * @author rap
 */
public class Connection {

	private static final Logger logger = Logger.getLogger(Connection.class.getName());

	/**
	 * The encryption method (0 - no encryption, 1 - SSL, 2 - StartTLS)
	 */
	private final ErniLdapConstants.Encryption encryption;

	/**
	 * The host address of the LDAP server
	 */
	private final String host;

	/**
	 * The port number of the LDAP server
	 */
	private final int port;

	/**
	 * The DN to use to bind to the server.
	 */
	private final String bindDN;

	/**
	 * The password to use to bind to the server.
	 */
	private final char[] bindPW;

	private LDAPConnection conn;

	private Connection(ErniLdapConstants.Encryption encryption, String host, int port, String bindDN, char[] bindPW) {
		this.encryption = encryption;
		this.host = host;
		this.port = port;
		this.bindDN = bindDN;
		this.bindPW = bindPW;
	}

	public static Connection forCredentials(Credentials credentials) {
		return new Connection(ErniLdapConstants.Encryption.NO_ENCRYPTION, ErniLdapConstants.HOST, ErniLdapConstants.PORT, credentials.getUser(), credentials.getPassword().toCharArray());
	}

	public static Connection forCredentials(String username, String password) {
		return new Connection(ErniLdapConstants.Encryption.NO_ENCRYPTION, ErniLdapConstants.HOST, ErniLdapConstants.PORT, username, password.toCharArray());
	}

	private boolean usesSSL() {
		return encryption == ErniLdapConstants.Encryption.SSL;
	}

	private boolean usesStartTLS() {
		return encryption == ErniLdapConstants.Encryption.START_TLS;
	}

	// TODO @rap: Refactor this since it was CTRL+C -> CTRL+V-ed and it is ugly (but it works)
	LDAPConnection ldapConnection() throws LDAPException {
		SocketFactory socketFactory = null;
		if (usesSSL()) {
			final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
			try {
				socketFactory = sslUtil.createSSLSocketFactory();
			} catch (Exception e) {
				logger.severe("getConnection " + e);
				throw new LDAPException(ResultCode.LOCAL_ERROR, "Cannot initialize SSL", e);
			}
		}

		final LDAPConnectionOptions options = new LDAPConnectionOptions();
		options.setAutoReconnect(true);
		options.setConnectTimeoutMillis(30000);
		options.setFollowReferrals(false);
		options.setMaxMessageSize(0);

		conn = new LDAPConnection(socketFactory, options, host, port);

		if (usesStartTLS()) {
			final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
			try {
				final ExtendedResult r = conn.processExtendedOperation(new StartTLSExtendedRequest(sslUtil.createSSLContext()));
				if (r.getResultCode() != ResultCode.SUCCESS) {
					throw new LDAPException(r);
				}
			} catch (LDAPException le) {
				logger.severe("getConnection" + le);
				conn.close();
				throw le;
			} catch (Exception e) {
				logger.severe("getConnection" + e);
				conn.close();
				throw new LDAPException(ResultCode.CONNECT_ERROR, "Cannot initialize StartTLS", e);
			}
		}

		if ((bindDN != null) && (bindPW != null)) {
			try {
				conn.bind(bindDN, new String(bindPW));
			} catch (LDAPException le) {
				logger.severe("getConnection" + le);
				conn.close();
				throw le;
			}
		}

		return conn;
	}

	public void close() {
		if (conn != null) {
			conn.close();
		}
	}

	@Override
	public String toString() {
		return "Connection{" +
				"encryption=" + encryption +
				", host='" + host + '\'' +
				", port=" + port +
				", bindDN='" + bindDN + '\'' +
				'}';
	}

}