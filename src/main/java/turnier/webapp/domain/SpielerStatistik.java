package turnier.webapp.domain;

public class SpielerStatistik {
	
	private Teilnehmer spieler;
	private int gewonneneSpiele;
	private int verloreneSpiele;
	private int unentschiedeneSpiele;
	private int platzierung;
	private int spieleIngesamtGespielt;
	private float gewinnWahrscheinlichkeit;
	private int anzahlBesuchterTurniere;
	private float faehigkeitslevel;
	
	private static float durchnittswert;
	private static float mittlereAbweichung;
	
	public  Teilnehmer[] kalkuliertPlatzierung(Teilnehmer[] teilnehmern) {
		
		return null;
	}
	
	public float kalkuliertFaehigkeitsLevel() {
		return -1.0f;
		
	}
	

}
