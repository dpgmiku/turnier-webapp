package turnier.webapp.domain;

import static multex.MultexUtil.create;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.domain.imports.TurnierRepository;

/**This is a domain service for creating tourniers.
 * 
 * @author Michal Kubacki
 * @since 2017-12-15
 *
 */
@Service
//@Secured("NUTZER") //Only role NUTZER may call the methods in this service class.You can apply this annotation at the class or at the method level. 
public class TurnierService {
	
    //Required repositories as by Ports and Adapters Pattern:
   private final NutzerRepository nutzerRepository;
   private final TurnierRepository turnierRepository;
   
   @Autowired
   public TurnierService(final NutzerRepository nutzerRepository, final TurnierRepository turnierRepository) {
	this.nutzerRepository = nutzerRepository;
	this.turnierRepository = turnierRepository;   
   }

 /**Command: Creates a new Nutzer
  * @param name the name of the new Nutzer
  * @param vorname the vorname of the new Nutzer
  * @param nutzername the unique nutzername of the new Nutzer
  * @param passwort the password of the nutzer
  * @param email the email of the nutzer
  * @return nutzer
 * @throws BunutzernameSchonHinterlegtExc
*             benutzername befindet sich schon im Datenbank
 * @throws EmailSchonHinterlegtExc
 *             email hat schon ein anderer Benutzer hinterlegt
 * @throws ThatsNotAnEmailExc
 *             email entpricht nicht die Email Norm
 * @throws NeuesPasswortNotAllowedExc
 *             passwort ist kürzer als 6 Zeichen oder länger als 255 Zeichen

  */
   public Nutzer createNutzer(final String name, final String vorname, final String nutzername, final String passwort, final String email) {
		final int passwortLength = passwort.length();
		if (passwortLength > 5 && passwortLength < 255) {
			final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
			if (matcher.find()) {
				Nutzer findNutzer = findNutzerByEmail(email);
				if (findNutzer == null) {
					Nutzer findNutzerNutzername = findNutzerByNutzername(nutzername);
					if (findNutzerNutzername == null) {
					return nutzerRepository.save(new Nutzer(name,vorname,nutzername,passwort,email));
					} else
						throw create(BenutzernameSchonHinterlegtExc.class, nutzername,
								findNutzerNutzername.getId());
				} else
					throw create(EmailSchonHinterlegtExc.class, email, findNutzer.getId());
			} else
				throw create(ThatsNotAnEmailExc.class, email);
		} else
			throw create(NeuesPasswortNotAllowedExc.class, passwort, nutzername);
   }
  /**Command: updates password of the given {@link Nutzer} with a new one
   * 
   * @param nutzer nutzer who wants to update his password
   * @param altesPasswort  altes password to verify
   * @param neuesPasswort new Password
   * @return nutzer with a new password
   */
   
   public Nutzer updateNutzerWithPassword(final Nutzer nutzer, final String altesPasswort, final String neuesPasswort ) {
	   
		final int passwortLaenge = neuesPasswort.length();
		if (passwortLaenge > 5 && passwortLaenge < 255) {
       nutzer.passwortAendern(altesPasswort, neuesPasswort);
           return nutzerRepository.save(nutzer);
		}
		throw create(NeuesPasswortNotAllowedExc.class, neuesPasswort, nutzer.getNutzername());


   }
   /**Command: Deletes the given {@link Nutzer}
    * @param nutzer nutzer object which we want to delete from db
    * @param passwort password to verify
    */
 
 
   public void deleteNutzer(final Nutzer nutzer,final  String passwort){
	if (nutzer.getPasswort().equals(passwort)) {   
 
    nutzerRepository.delete(nutzer.getId());
	}
   }
   /**Command: updates email address of the {@link Nutzer} to the new one
    * 
    * @throws EmailSchonHinterlegtExc
    * 	 * @param email
	 *            neue Email auf die man das ändern will
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
   public void updateEmail(final Nutzer nutzer, final String passwort, final String email) throws ThatsNotAnEmailExc, EmailSchonHinterlegtExc {
 {
	   final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		if (matcher.find()) {
			Nutzer findNutzer = nutzerRepository.findEmail(email);
			if (findNutzer == null) {
            nutzer.emailAendern(email, passwort);
            nutzerRepository.save(nutzer);
				
			} else
				throw create(EmailSchonHinterlegtExc.class, email, findNutzer.getId());
		} else
			throw create(ThatsNotAnEmailExc.class, email);

	}   
   }
   
	public void entferneTeilnehmer(Nutzer owner, Turnier turnier, Nutzer nutzer) {
		if (turnierRepository.find(turnier.getName()) == null)
			throw create(TurnierGibtEsNichtExc.class, turnier.getName());
		List<Nutzer> turnierTeilnehmerList = turnier.getTeilnehmer();
		if (!(turnierTeilnehmerList.contains(nutzer)))
			throw create(TeilnehmerGibtEsNichtExc.class, nutzer.getNutzername(), turnier.getName());
		if (!(owner.getNutzername().equals(turnier.getOrganisator().getNutzername())))
			throw create(EsIstNichtDeinTurnierExc.class, turnier.getName(), owner.getNutzername());
		turnier.entferneTeilnehmerAusDemTurnier(nutzer);
		turnierRepository.save(turnier);
	}
   

	public Turnier turnierErstellen(String name, String adresse, String datum, String uhrzeit, Nutzer nutzer,
			int maxTeilnehmer) {
		if (maxTeilnehmer > 32)
			throw create(ZuVieleTeilnehmerExc.class, maxTeilnehmer);
		if (maxTeilnehmer < 2)
			throw create(ZuWenigTeilnehmerExc.class, maxTeilnehmer);
	    final Boolean containsNumber = name.matches(".*\\d+.*");
		if (adresse.length() <= 3 || containsNumber) {

			throw create(KeineRichtigeEingabenTurnierExc.class, name);
		}
		final Turnier findTurnier = this.findTurnierByName(name);
		if (findTurnier != null) {
			
			throw create(TurniernameSchonHinterlegtExc.class, name, findTurnier.getId());
		};

		final Turnier turnier = new Turnier(name, adresse, datum, uhrzeit, nutzer, maxTeilnehmer);

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
   /**Query: Finds the client with the given email address 
    * @param email given email, which should be found in nutzerRepository
    * @return nutzer Object, if not found null
    */
   
   public Nutzer findNutzerByEmail(final String email) {
	return nutzerRepository.findEmail(email);  
   }

   /**Query: Finds the client with the given nutzername 
    * @param nutzername given nutzername, which should be found in nutzerRepository
    * @return nutzer Object, if not found null
    */
   public Nutzer findNutzerByNutzername(final String nutzername) { 
	return nutzerRepository.find(nutzername);   
   }
   
   public Turnier findTurnierByName(final String name) {
	   
	   return turnierRepository.find(name);
   }
   
 public List<Turnier> findTurniers() {
	   
	   return turnierRepository.findAll();
   }
 
 public List<Turnier> findTurnierByOrganisator(final Nutzer organisator) {
	 
	 return turnierRepository.findTurniereVonNutzer(organisator);
 }




	public void loescheEigenesTurnier(Nutzer nutzer, Turnier turnier) {
		if (turnierRepository.find(turnier.getName()) == null)
			throw create(TurnierGibtEsNichtExc.class, turnier.getName());
		if (!(nutzer.getNutzername().equals(turnier.getOrganisator().getNutzername())))
			throw create(EsIstNichtDeinTurnierExc.class, turnier.getName(), nutzer.getNutzername());
		turnierRepository.delete(turnier.getId());

	}

	public void anTurnierAnmelden(Turnier turnier, Nutzer nutzer) {
		if (turnier.getTeilnehmer().contains(nutzer))
			throw create(DuBistSchonAngemeldetExc.class, nutzer.getNutzername(), turnier.getName());
		final TurnierStatus turnierStatus = turnier.getTurnierStatus();
		if (turnierStatus != TurnierStatus.OFFEN)
			throw create(TurnierStatusFailExc.class, turnier.getName(), turnierStatus.toString());
		turnier.fuegeTeilnehmerHinzu(nutzer);
		turnierRepository.save(turnier);
	}

	/** Nutzername {0} ist schon im Turnier {1} angemeldet */
	@SuppressWarnings("serial")
	public static class DuBistSchonAngemeldetExc extends multex.Exc {
	}
	// methods from Teilnehmer Klasse - unecessary split, because OneToOne
   
/**Query: Finds all Nutzers of the Turnier Webapp. They are ordered by their ascending Nutzernames*/
public List<Nutzer> findAllNutzers(){
return nutzerRepository.findAll();	
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
/** Es existiert schon ein Turnier mit diesem Turniername {0} mit dem ID {1} */
@SuppressWarnings("serial")
public static class TurniernameSchonHinterlegtExc extends multex.Exc {
}

}
