package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.Nutzer.PasswortDoesntMatchExc;
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
//Entweder Entity oder Service! 17-12-14 Knabe
@Entity
public class Nutzer extends EntityBase<Nutzer> {

	private String name;
	private String vorname;
	private String nutzername;
	private String passwort;
	private String email;

	
//	@Autowired
//	private transient TurnierRepository turnierRepository;
//
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
	 * löscht den Nutzer aus dem Datenbank.
	 * 
	 * @param passwort
	 *            zum Vergleichen mit dem gespeicherten Passwort im DB
	 * @throws PasswortDoesntMatchExc
	 *             passwort stimmt nicht mit dem gespeicherten Passwort im DB
	 *             überein
	 */
	public Boolean passwortVerifizieren(String passwortZurVerifizierung) throws PasswortDoesntMatchExc {
		if (this.passwort.equals(passwortZurVerifizierung)) {
			return true;
		} else
			throw create(Nutzer.PasswortDoesntMatchExc.class, passwortZurVerifizierung, this.nutzername);
	}
	
	/** ändert das Passwort für die Nutzer
	 * 
	 * @param altesPasswort altes Passwort
	 * @param neuesPasswort neues Passwort
	 * @throws PasswortDoesntMatchExc exception when the password to verify doesnt match with the actuall one.
	 */
	public void passwortAendern(String altesPasswort, String neuesPasswort)
			throws PasswortDoesntMatchExc {
		if (passwortVerifizieren(altesPasswort)) {
		this.passwort = neuesPasswort;

		}
	}


	public void emailAendern(String neueEmail, String passwort) throws PasswortDoesntMatchExc {
		if (passwortVerifizieren(passwort)) {
		this.email=neueEmail;	    
	}
}
	

	


//
//	public SpielerStatistik sieheStatistik(Teilnehmer teilnehmer) {
//		// to do, no clue, what to do here, i dont understand our classdiagramm
//		return null;
//	}

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

	
}
