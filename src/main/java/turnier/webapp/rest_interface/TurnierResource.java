package turnier.webapp.rest_interface;

import java.util.List;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;
import turnier.webapp.domain.TurnierBracket;

/**
 * Daten über Turnier vom Turnier-Webapp. Brauchbar für Data Transfer Object.
 */
public class TurnierResource {

	/** Einzigartige ID-Nummer vom Turnier */
	public Long id;

	/** Der einzigartige Name vom Turnier. */
	public String name;

	/** Die Adresse vom Turneir */
	public String adresse;

	/** Das Datum vom Turnier */
	public String datum;

	/** Das Uhrzeit vom Turnier. */
	public String uhrzeit;

	/** der Veranstalter vom Turnier. */
	public Nutzer organisator;

	/** Max Anzahl der Teilnehmern im Turnier */
	public int maxTeilnehmer;

	/** Status vom Turnier */
	public String turnierStatus;

	/** Teilnehmerliste vom Turnier */
	public List<Nutzer> teilnehmer;

	/** Alle TurnierPaare in einer Liste */
	public List<TurnierBracket> turnierbrackets;

	/** erforderlich für Jackson */
	public TurnierResource() {
	}

	/**
	 * kreirt TurnierResource mit den Daten aus übergebenen Turnierentität.
	 * 
	 * @param entity
	 *            turnier Objekt
	 */
	public TurnierResource(final Turnier entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.adresse = entity.getAdresse();
		this.datum = entity.getDatum();
		this.uhrzeit = entity.getUhrzeit();
		this.organisator = entity.getOrganisator();
		this.turnierbrackets = entity.getTurnierBrackets();
		this.maxTeilnehmer = entity.getMaxTeilnehmer();
		this.turnierStatus = entity.getTurnierStatus().toString();
		this.teilnehmer = entity.getTeilnehmer();
	}

	@Override
	public String toString() {
		return String.format(
				"Turnier{id=%d, name='%s', adresse='%s', datum='%s', uhrzeit='%s', organisator='%s, maxTeilnehmer=%d, turnierstatus='%s', teilnehmer='%s', turnierBracket='%s'}",
				id, name, name, adresse, datum.toString(), uhrzeit.toString(), organisator.toString(), maxTeilnehmer,
				turnierStatus.toString(), teilnehmer.toString(), turnierbrackets.toString());
	}

}
