package turnier.webapp.rest_interface;

/**Befehl zur Passwortänderung einer Nutzer, sein Passwort wird verifiziert.*/
public class PasswordChangeCommand {

/**das alte Passwort zur Verifizierung*/
public String verifyPasswd;
/**das neue Passwort*/
public String newPassword;

}
