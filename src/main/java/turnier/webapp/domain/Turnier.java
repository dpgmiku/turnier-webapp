package turnier.webapp.domain;

public class Turnier {
	
	private String name;
	private String adresse;
	private String datum;
	private String uhrzeit;
	private Teilnehmer organisator;
	private int maxTeilnehmer;
	private Teilnehmer[] teilnehmer;
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
