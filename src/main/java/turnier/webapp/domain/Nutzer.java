package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.base.EntityBase;
import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.domain.imports.TurnierRepository;

import static multex.MultexUtil.create;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configurable
//@Service //Entweder Entity oder Service! 17-12-14 Knabe
@Entity
// @Secured("NUTZER") //Only role NUTZER may call the methods in this domain
// class. You can apply this annotation at the class or at the method level.
public class Nutzer extends EntityBase<Nutzer> {

	private String name;
	private String vorname;
	private String nutzername;
	private String passwort;
	private String email;

	@Autowired
	private transient NutzerRepository nutzerRepository;

	@Autowired
	private transient TurnierRepository turnierRepository;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Nutzer() {
	};

	public Nutzer(String name, String vorname, String nutzername, String passwort, String email) {

		this.setName(name);
		this.setVorname(vorname);
		this.setNutzername(nutzername);
		this.setPasswort(passwort);
		this.setEmail(email);

	}

	/**
	 * this method was written only for testing and to make the next milestone
	 * easier to demonstrate. It will be belonging in the future to the class Gast.
	 * It duplicates the code, I didn't want to outsource it to the new private
	 * methode, so I don't have to make it back as is it now in the future
	 * 
	 * @throws BunuterznameSchonHinterlegtExc
	 *             benutzername befindet sich schon im Datenbank
	 * @throws EmailSchonHinterlegtExc
	 *             email hat schon ein anderer Benutzer hinterlegt
	 * @throws ThatsNotAnEmailExc
	 *             email entpricht nicht die Email Norm
	 * @throws NeuesPasswortNotAllowedExc
	 *             passwort ist kürzer als 6 Zeichen oder länger als 255 Zeichen
	 **/
	public void nutzerSpeichern() throws BenutzernameSchonHinterlegtExc, EmailSchonHinterlegtExc, ThatsNotAnEmailExc,
			NeuesPasswortNotAllowedExc {
		final int passwortLength = this.passwort.length();
		if (passwortLength > 5 && passwortLength < 255) {
			final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this.email);
			if (matcher.find()) {
				Nutzer findNutzer = nutzerRepository.findEmail(this.email);
				if (findNutzer == null) {
					Nutzer findNutzerNutzername = nutzerRepository.find(this.nutzername);
					if (findNutzerNutzername == null) {
						Nutzer nutzerSave = nutzerRepository.save(this);
					} else
						throw create(Nutzer.BenutzernameSchonHinterlegtExc.class, nutzername,
								findNutzerNutzername.getId());
				} else
					throw create(Nutzer.EmailSchonHinterlegtExc.class, email, findNutzer.getId());
			} else
				throw create(Nutzer.ThatsNotAnEmailExc.class, email);
		} else
			throw create(Nutzer.NeuesPasswortNotAllowedExc.class, this.passwort, this.nutzername);
	}

	/**
	 * löscht den Nutzer aus dem Datenbank.
	 * 
	 * @param passwort
	 *            zum Vergleichen mit dem gespeicherten Passwort im DB
	 * @throws PasswortDoesntMatchExc
	 *             passwort stimmt nicht mit dem gespeicherten Passwort im DB
	 *             überein
	 */
	public void nutzerLoeschen(String passwort) throws PasswortDoesntMatchExc {
		if (passwortVerifizieren(passwort)) {
			nutzerRepository.delete(getId());
		}

	}

	/**
	 * ändert das Passwort des Nutzer, wenn die Passwortkriterien erfüllt sind und
	 * das verifizierte Passwort stimmt mit dem gespeicherten im DB überein.
	 * 
	 * @param altesPasswort
	 *            Passwort zum Verifizierung
	 * @param neuesPasswort
	 *            neues Passwort
	 * @return Nutzer Object mit dem neu gespeicherten Passwort
	 * @throws PasswortDoesntMatchExc
	 *             passwort stimmt nicht mit dem gespeicherten Passwort im DB
	 *             überein
	 * @throws NeuesPasswortNotAllowedExc
	 *             passwort ist kürzer als 6 Zeichen oder länger als 255 Zeichen
	 **/

	public Nutzer passwortAendern(String altesPasswort, String neuesPasswort)
			throws PasswortDoesntMatchExc, NeuesPasswortNotAllowedExc {
		if (passwortVerifizieren(altesPasswort)) {
			final int passwortLaenge = neuesPasswort.length();
			if (passwortLaenge > 5 && passwortLaenge < 255) {
				passwort = neuesPasswort;
				// nutzerRepository.updatePasswort(getId(), passwort);

				return nutzerRepository.save(this);
			}
			throw create(Nutzer.NeuesPasswortNotAllowedExc.class, neuesPasswort, this.nutzername);

		}
		return this;
	}

	/**
	 * this method was written only for testing and to make the next milestone
	 * easier to demonstrate. It will be belonging in the future to the class Admin.
	 * It duplicates the code, I didn't want to outsource it to the new private
	 * methode, so I don't have to make it back as is it now in the future
	 * 
	 * @return Nutzer object, wenn es im DB gefunden ist, andernfalls null
	 */

	public Nutzer findNutzer(String nutzername) {

		return nutzerRepository.find(nutzername);

	}

	/**
	 * ändert Email Adresse
	 * 
	 * @param neueEmail
	 *            neue Email die man
	 * @param passwort
	 *            passwort zur Verifizierung
	 * @return Nutzer Nutzer Object mit dem neu gespeicherter Email-Adresse
	 * @throws PasswortDoesntMatchExc
	 *             passwort stimmt nicht mit dem gespeicherten Passwort im DB
	 *             überein
	 * @throws ThatsNotAnEmailExc
	 *             email entpricht nicht die Email Norm
	 * @throws EmailSchonHinterlegtExc
	 *             email hat schon ein anderer Benutzer hinterlegt
	 */

	public Nutzer emailAendern(String neueEmail, String passwort)
			throws PasswortDoesntMatchExc, ThatsNotAnEmailExc, EmailSchonHinterlegtExc {

		if (passwortVerifizieren(passwort)) {
			final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(neueEmail);
			if (matcher.find()) {
				Nutzer findNutzer = nutzerRepository.findEmail(neueEmail);
				if (findNutzer == null) {

					this.email = neueEmail;
					// nutzerRepository.updateEmail(getId(), email);
					return nutzerRepository.save(this);
				} else
					throw create(Nutzer.EmailSchonHinterlegtExc.class, neueEmail, findNutzer.getId());
			} else
				throw create(Nutzer.ThatsNotAnEmailExc.class, neueEmail);

		}
		return null;

	}

	/**
	 * Auslagerung von der Methode zur Passwort Verifizierung (private)
	 * 
	 * @param passwortZurVerifizierung
	 *            Passwort zur Verifizierung
	 * @return true wenn Passwort übereinstimmt, false wenn nicht
	 * @throws PasswortDoesntMatchExc
	 *             passwort stimmt nicht mit dem gespeicherten Passwort im DB
	 *             überein
	 */

	private Boolean passwortVerifizieren(String passwortZurVerifizierung) throws PasswortDoesntMatchExc {
		if (this.passwort.equals(passwortZurVerifizierung)) {
			return true;
		} else
			throw create(Nutzer.PasswortDoesntMatchExc.class, passwortZurVerifizierung, this.nutzername);
	}

	// methods from Teilnehmer Klasse - unecessary split, because OneToOne

	public Turnier turnierErstellen(String name, String adresse, LocalDate datum, LocalTime uhrzeit,
			int maxTeilnehmer) {
		if (maxTeilnehmer > 32)
			throw create(Nutzer.ZuVieleTeilnehmerExc.class, maxTeilnehmer);
		if (maxTeilnehmer < 2)
			throw create(Nutzer.ZuWenigTeilnehmerExc.class, maxTeilnehmer);
		final char[] charArray = name.toCharArray();
		Boolean zifferInName = false;
		for (int i = 0; i < name.length(); i++) {
			if (Character.isDigit(charArray[i])) {
				zifferInName = true;
				break;
			}

		}

		if (adresse.length() <= 3 || zifferInName) {

			throw create(Nutzer.KeineRichtigeEingabenTurnierExc.class, name);
		}

		final Turnier turnier = new Turnier(name, adresse, datum, uhrzeit, this, maxTeilnehmer);

		return turnierRepository.save(turnier);
	}

	/**
	 * Turnier {0} name enthält Ziffer, Ort hat weniger als 3 Zeichen und oder
	 * maxTeilnehmer ist keine Ziffer
	 */
	@SuppressWarnings("serial")
	public static class KeineRichtigeEingabenTurnierExc extends multex.Exc {
	}

	/** maxTeilnehmer {0} ist größer als 32 */
	@SuppressWarnings("serial")
	public static class ZuVieleTeilnehmerExc extends multex.Exc {
	}

	/** maxTeilnehmer {0} ist kleiner als 2 */
	@SuppressWarnings("serial")
	public static class ZuWenigTeilnehmerExc extends multex.Exc {
	}

	public void entferneTeilnehmer(Turnier turnier, Nutzer nutzer) {
		if (turnierRepository.find(turnier.getName()) == null)
			throw create(Nutzer.TurnierGibtEsNichtExc.class, turnier.getName());
		ArrayList<Nutzer> turnierTeilnehmerList = turnier.getTeilnehmer();
		if (!(turnierTeilnehmerList.contains(nutzer)))
			throw create(TeilnehmerGibtEsNichtExc.class, nutzer.getNutzername(), turnier.getName());
		if (!(this.nutzername.equals(turnier.getOrganisator().getNutzername())))
			throw create(EsIstNichtDeinTurnierExc.class, turnier.getName(), this.nutzername);
		turnier.entferneTeilnehmerAusDemTurnier(nutzer);
	}

	public void loescheEigenesTurnier(Turnier turnier) {
		if (turnierRepository.find(turnier.getName()) == null)
			throw create(Nutzer.TurnierGibtEsNichtExc.class, turnier.getName());
		if (!(this.nutzername.equals(turnier.getOrganisator().getNutzername())))
			throw create(EsIstNichtDeinTurnierExc.class, turnier.getName(), this.nutzername);
		turnierRepository.delete(turnier.getId());

	}

	public void anTurnierAnmelden(Turnier turnier) {
		//	if (turnierRepository.find(turnier.getName()) == null)
	//		throw create(Nutzer.TurnierGibtEsNichtExc.class, turnier.getName());
		if (turnier.getTeilnehmer().contains(this))
			throw create(DuBistSchonAngemeldetExc.class, this.nutzername, turnier.getName());
		final TurnierStatus turnierStatus = turnier.getTurnierStatus();
		if (turnierStatus != TurnierStatus.OFFEN)
			throw create(TurnierStatusFailExc.class, turnier.getName(), turnierStatus.toString());
		turnier.fuegeTeilnehmerHinzu(this);
	}

	/** Nutzername {0} ist schon im Turnier {1} angemeldet */
	@SuppressWarnings("serial")
	public static class DuBistSchonAngemeldetExc extends multex.Exc {
	}

	/**
	 * Turnier {0} hat Status {1}. Es ist unmöglich sich mit diesem Status
	 * anzumelden
	 */
	@SuppressWarnings("serial")
	public static class TurnierStatusFailExc extends multex.Exc {
	}

	/** Turnier {0} existiert gar nicht im Turnier Webapp */
	@SuppressWarnings("serial")
	public static class TurnierGibtEsNichtExc extends multex.Exc {
	}

	/** Teilnehmer {0} existiert gar nicht im Turnier {1} */
	@SuppressWarnings("serial")
	public static class TeilnehmerGibtEsNichtExc extends multex.Exc {
	}

	/** Turnier {0} gehört nicht zu diesem Teilnehmer {1} */
	@SuppressWarnings("serial")
	public static class EsIstNichtDeinTurnierExc extends multex.Exc {
	}

	public SpielerStatistik sieheStatistik(Teilnehmer teilnehmer) {
		// to do, no clue, what to do here, i dont understand our classdiagramm
		return null;
	}

	// getter and setter - selfeplenatory

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

	// innere Exception Klassen
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

	/** Es existiert schon ein Nutzer mit dieser Email {0} mit dem ID {1} */
	@SuppressWarnings("serial")
	public static class EmailSchonHinterlegtExc extends multex.Exc {
	}

	/** Es existiert schon ein Nutzer mit diesem Nutzername {0} mit dem ID {1} */
	@SuppressWarnings("serial")
	public static class BenutzernameSchonHinterlegtExc extends multex.Exc {
	}

}
