package turnier.webapp.domain;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Turnier {
	@Id
	private String name;
	private String adresse;
	private String datum;
	private String uhrzeit;
	@ManyToOne
	private Teilnehmer organisator;
	private int maxTeilnehmer;
	private Teilnehmer[] teilnehmer;
	@ManyToOne
	private TurnierBracket turnierbaum;
	
	public Teilnehmer teilnehmerSuchen(String name) {
		
		return null;
	}
	
	
	public TurnierBracket kreireTurnierbaum(Teilnehmer[]  teilnehmer) {
		
		return null;
	}
	
	public void starteTurnier() {

	}
	
	public TurnierErgebnisse beendeTurnier(TurnierBracket turnierbaum) {
		
		return null;
	}
		
	
	
	

}
