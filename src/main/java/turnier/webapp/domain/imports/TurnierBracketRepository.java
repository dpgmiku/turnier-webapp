
package turnier.webapp.domain.imports;

import turnier.webapp.domain.TurnierBracket;

/**
 * Erforderliche Repository für {@link TurnierBracket} Objekte.
 * 
 * @author se2 letzte Gruppe
 * @version 2017-01-08
 * @since 2017-01-08
 */
public interface TurnierBracketRepository {

	/**
	 * löscht alle {@link TurnierBracket} Objekte. Brauchbar für Test Cases mit
	 * einem leeren DB
	 */
	void deleteAll();

	/**
	 * Gibt einem Admin eine einzigartige, größere ID und speichert dem Admin im DB.
	 * 
	 * @param turnierBracket
	 *            {@link TurnierBracket} Objekt zum speichern
	 * @return modifizierte Instanz
	 */
	TurnierBracket save(TurnierBracket turnierBracket);

	/**
	 * löscht dem übergebenen {@link TurnierBracket} Objekt
	 * 
	 * @param turnierBracket
	 *            {@link TurnierBracket} Objetk zum löschen
	 */
	void delete(TurnierBracket turnierBracket);

}
