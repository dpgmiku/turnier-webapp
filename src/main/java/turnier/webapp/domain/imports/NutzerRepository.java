package turnier.webapp.domain.imports;

import java.util.Optional;
import java.util.List;

import turnier.webapp.domain.Nutzer;

/**
 * Erforderliche Repository für {@link Nutzer} Objekte
 * 
 * @author se2 letzte Gruppe
 * @version 2017-11-01
 * @since 2017-10-01
 */
public interface NutzerRepository {

	/**
	 * gib zurück ein {@link Nutzer} Objekt mit dem übergebenen nutzername, wenn es
	 * existiert.
	 * 
	 * @param nutzername
	 *            der Name vom gesuchten Nutzer
	 * @return das gefundene Nutzer Objekt
	 */
	Nutzer find(String nutzername);

	/**
	 * gib zurück ein {@link Nutzer} Objekt mit dem übergebenen nutzername, wenn es
	 * existiert.
	 * 
	 * @param email
	 *            die Emailadresse vom gesuchten Nutzer
	 * @return das gefundene Nutzer Objekt
	 */
	Nutzer findEmail(String email);

	/**
	 * löscht alle {@link Nutzer} Objekte. Brauchbar für Test Cases mit einem leeren
	 * DB
	 */
	void deleteAll();

	/**
	 * Gibt einem Nutzer eine einzigartige, größere ID und speichert dem Nutzer im
	 * DB.
	 * 
	 * @param nutzer
	 *            {@link Nutzer} Objekt zum speichern
	 * @return modifizierte Instanz
	 */
	Nutzer save(Nutzer nutzer);

	/**
	 * Findet alle {@link Nutzer}s und gibt alle zurück in einer Liste, "nutzername"
	 * aufsteigend sortiert
	 * 
	 * @return alle gefundene {@link Nutzer} Objekt als Liste
	 */
	List<Nutzer> findAll();

	/**
	 * löscht {@link Nutzer} Objekt mit dem übergebenen Id
	 * 
	 * @param id
	 *            id vom gelöschten {@link Nutzer} Objekt
	 */
	void delete(Long id);
}
