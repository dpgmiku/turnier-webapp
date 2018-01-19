package turnier.webapp.domain.imports;

import java.util.List;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;

/**
 * Erforderliche Repository für {@link Turnier} Objekte.
 * 
 * @author se2 letzte Gruppe
 * @version 2017-12-12
 * @since 2017-12-12
 */
public interface TurnierRepository {

	/**
	 * gib zurück ein {@link Turnier} Objekt mit dem übergebenen adminname, wenn es
	 * existiert.
	 * 
	 * @param name
	 *            Der Name vom gesuchten Turnier
	 * @return das gefundene Turnier Objekt
	 */
	Turnier find(String name);

	/**
	 * findet alle {@link Turnier} Objekte als Liste, die vom übergebenen
	 * {@link Nutzer} veranstaltet wurden
	 * 
	 * @param nutzer
	 *            der Veranstalter - {@link Nutzer} Objekt
	 * @return alle gefundene {@link Turnier}e Objekte, die von dem nutzer
	 *         veranstaltet wurden
	 */
	List<Turnier> findTurniereVonNutzer(Nutzer nutzer);

	/**
	 * löscht alle {@link Turnier} Objekte. Brauchbar für Test Cases mit einem
	 * leeren DB
	 */
	void deleteAll();

	/**
	 * Gibt einem Turnier eine einzigartige, größere ID und speichert dem Admin im
	 * DB.
	 * 
	 * @param turnier
	 *            {@link Turnier} Objekt zum speichern
	 * @return modifizierte Instanz
	 */
	Turnier save(Turnier turnier);

	/**
	 * Findet alle {@link Turnier}s und gibt alle zurück in einer Liste, "name"
	 * aufsteigend sortiert
	 * 
	 * @return alle gefundene {@link Turnier} Objekte als Liste
	 */
	List<Turnier> findAll();

	/**
	 * löscht {@link Turnier} Objekt mit dem übergebenen Id
	 * 
	 * @param id
	 *            id vom gelöschten {@link Turnier} Objekt
	 */
	void delete(Long id);

}
