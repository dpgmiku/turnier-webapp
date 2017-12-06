package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.base.EntityBase;
import turnier.webapp.domain.imports.NutzerRepository;

import static multex.MultexUtil.create;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Configurable
@Service
@Entity
//@Secured("NUTZER") //Only role NUTZER may call the methods in this domain class. You can apply this annotation at the class or at the method level.
public class Nutzer extends EntityBase<Nutzer> {

	private String name;
	private String vorname;
	private String nutzername;
	private String passwort;
	private String email;
	
	@Autowired
	private transient NutzerRepository nutzerRepository;    
	  
	
	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	public Nutzer() {
	};

	public Nutzer(String name, String vorname, String nutzername, String passwort, String email) {

		this.setName(name);
		this.setVorname(vorname);
		this.setNutzername(nutzername);
		this.setPasswort(passwort);
		this.setEmail(email);

	}
	

	
	/**this method was written only for testing and to make the next milestone easier to demonstrate.
	 *  It will be belonging in the future to the class Gast. It duplicates the code, I didn't want to outsource it to the new
	 *  private methode, so I don't have to make it back as is it now in the future  **/
	public void nutzerSpeichern() throws BenutzernameSchonHinterlegtExc, EmailSchonHinterlegtExc, ThatsNotAnEmailExc, NeuesPasswortNotAllowedExc{
		final int passwortLength = this.passwort.length();
		if (passwortLength > 5 && passwortLength < 255)
		{
		final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this.email);
		if (matcher.find()) {
			 Nutzer findNutzer = nutzerRepository.findEmail(this.email);
			if (findNutzer == null) 
			{
				Nutzer findNutzerNutzername = nutzerRepository.find(this.nutzername);
			    if (findNutzerNutzername == null) {
				Nutzer nutzerSave = nutzerRepository.save(this);
			    }
			    else throw create(Nutzer.BenutzernameSchonHinterlegtExc.class, nutzername, findNutzerNutzername.getId());
			}
			else throw create(Nutzer.EmailSchonHinterlegtExc.class, email, findNutzer.getId());
		} else
			throw create(Nutzer.ThatsNotAnEmailExc.class, email);
		}
		else 
			throw create(Nutzer.NeuesPasswortNotAllowedExc.class, this.passwort, this.nutzername);
		}

	public void nutzerLoeschen(String passwort) throws PasswortDoesntMatchExc{
		if (passwortVerifizieren(passwort)) {
			nutzerRepository.delete(getId());
		}

	}

	public Nutzer passwortAendern(String altesPasswort, String neuesPasswort) throws PasswortDoesntMatchExc, NeuesPasswortNotAllowedExc{
		if (passwortVerifizieren(altesPasswort)) {
			final int passwortLaenge = neuesPasswort.length();
			if (passwortLaenge > 5 && passwortLaenge < 255) {
				passwort = neuesPasswort;
		     nutzerRepository.updatePasswort(getId(), passwort);
		     return nutzerRepository.find(nutzername);
			}
			throw create(Nutzer.NeuesPasswortNotAllowedExc.class, neuesPasswort, this.nutzername);

		}
		return this; 
	}
	
	public Nutzer findNutzer(String nutzername) {
		
	return nutzerRepository.find(nutzername);
			
	}


	public Nutzer emailAendern(String neueEmail, String passwort) throws PasswortDoesntMatchExc, ThatsNotAnEmailExc, EmailSchonHinterlegtExc {

		if (passwortVerifizieren(passwort)) {
			final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(neueEmail);
			if (matcher.find()) {
				Nutzer findNutzer = nutzerRepository.findEmail(neueEmail);
				if (findNutzer == null) {
					
					this.email = neueEmail;
					nutzerRepository.updateEmail(getId(), email);
                    return nutzerRepository.find(nutzername);
				}
				else throw create(Nutzer.EmailSchonHinterlegtExc.class, neueEmail, findNutzer.getId());
			} else
				throw create(Nutzer.ThatsNotAnEmailExc.class, neueEmail);

		}
		return null;

	}

	private Boolean passwortVerifizieren(String passwortZurVerifizierung) throws PasswortDoesntMatchExc {
		if (this.passwort.equals(passwortZurVerifizierung)) {
			return true;
		} else
			throw create(Nutzer.PasswortDoesntMatchExc.class, passwortZurVerifizierung, this.nutzername);
	}
	
	@Override
	public String toString() {
		return String.format("Nutzer{id=%d, name='%s', vorname='%s', nutzername='%s', passwort='%s', email='%s'}",
				 getId(), name, vorname, nutzername, passwort, email);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNutzername() {
		return nutzername;
	}

	public void setNutzername(String nutzername) {
		this.nutzername = nutzername;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Passwort {0} für den Nutzername {1} stimmt nicht mit dem Passwort Attribut
	 * überein.
	 */
	@SuppressWarnings("serial")
	public static class PasswortDoesntMatchExc extends multex.Exc {
	}

	/**
	 * Neues Passwort {0} für den Nutzername {1} ist kürzer als 6 Zeichen oder
	 * länger als 255 Zeichen.
	 */
	@SuppressWarnings("serial")
	public static class NeuesPasswortNotAllowedExc extends multex.Exc {
	}

	/** {0} entspricht nicht der Email-Adressen-Norm. */
	@SuppressWarnings("serial")
	public static class ThatsNotAnEmailExc extends multex.Exc {
	}

	/** Es existiert schon ein Nutzer mit dieser Email {0} mit dem ID {1}*/
	@SuppressWarnings("serial")
	public static class EmailSchonHinterlegtExc extends multex.Exc {
	}

	/** Es existiert schon ein Nutzer mit diesem Nutzername {0} mit dem ID {1}*/
	@SuppressWarnings("serial")
	public static class BenutzernameSchonHinterlegtExc extends multex.Exc {
	}

	
}
