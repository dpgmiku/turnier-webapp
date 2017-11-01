package turnier.webapp.domain;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class TurnierErgebnisse {
	
	@Id
	private Teilnehmer[] teilnehmer;
	
	
	public TurnierErgebnisse(Teilnehmer[] teilnehmer) {
		this.teilnehmer = teilnehmer;
		
	}
	
	
	public Teilnehmer[] kalkuliertPlatzierung(Teilnehmer[] teilnehmer) {
		
		
	return null;
	}
	
	public SpielerStatistik[] updateSpielerStatistiken(Teilnehmer[] teilnehmer) {
		
	return null;	
	}

}
