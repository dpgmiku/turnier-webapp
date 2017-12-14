package turnier.webapp.domain;
import javax.persistence.Entity;
import javax.persistence.Id;

import turnier.webapp.domain.base.EntityBase;


@Entity
public class TurnierErgebnisse extends EntityBase<TurnierErgebnisse>{
	
	private Teilnehmer[] teilnehmer;
	
	
	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private TurnierErgebnisse() {
	};
	
	public TurnierErgebnisse(Teilnehmer[] teilnehmer) {
		this.teilnehmer = teilnehmer;
		
	}
	
	
	public Teilnehmer[] kalkuliertPlatzierung(Teilnehmer[] teilnehmer) {
		
		
	return null;
	}
	
	public SpielerStatistik[] updateSpielerStatistiken(Teilnehmer[] teilnehmer) {
		
	return null;	
	}

	/** Keine Teilnehmer hat sich an das Turnier angemeldet*/
	@SuppressWarnings("serial")
	public static class TeilnehmerGibtEsNichtExc extends multex.Exc {}
			
	
	
}
