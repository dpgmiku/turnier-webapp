package turnier.webapp.domain;
@Entity 
public class Teilnehmer {
	
	private SpielerStatistik statistik;
	
	public Teilnehmer() {
		
		
	}
	
	public Turnier turnierErstellen(String name, String adresse, Date date, Time time, int anzahl) {
		
		
		return null;
	}
	
	public void entferneTeilnehmer(Turnier turnier, Teilnehmer teilnehmer) {
		
		
	}
	
	public void loescheEigenesTurnier(Turnier turnier) {
		
		
	}
	
	public Boolean anTurnierAnmelden(Turnier turnier) {
		
		return true;
	}
	
	public SpielerStatistik sieheStatistik(Teilnehmer teilnehmer) {
		
		return null;
	}

}
