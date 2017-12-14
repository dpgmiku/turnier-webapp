package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import turnier.webapp.domain.base.EntityBase;


@Entity
public class SpielerStatistik extends EntityBase<SpielerStatistik>{
	    
		//private Teilnehmer spieler;
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
		
	
		/** Necessary for JPA entities internally. */
		@SuppressWarnings("unused")
		private SpielerStatistik() {
		};
	
	
	public SpielerStatistik(Teilnehmer spieler, int gewonneneSpiele, int verloreneSpiele, int unentschiedeneSpiele,
			int platzierung, int spieleIngesamtGespielt, float gewinnWahrscheinlichkeit, int anzahlBesuchterTurniere,
			float faehigkeitslevel) {
		//this.spieler = spieler;
		this.gewonneneSpiele = gewonneneSpiele;
		this.verloreneSpiele = verloreneSpiele;
		this.unentschiedeneSpiele = unentschiedeneSpiele;
		this.platzierung = platzierung;
		this.spieleIngesamtGespielt = spieleIngesamtGespielt;
		this.gewinnWahrscheinlichkeit = gewinnWahrscheinlichkeit;
		this.anzahlBesuchterTurniere = anzahlBesuchterTurniere;
		this.faehigkeitslevel = faehigkeitslevel;
	}
  
	public  Teilnehmer[] kalkuliertPlatzierung(Teilnehmer[] teilnehmern) {
		
		return null;
	}
	
	public float kalkuliertFaehigkeitsLevel() {
		return -1.0f;
		
	}
	
	/** Keine Teilnehmer hat sich an das Turnier angemeldet*/
	@SuppressWarnings("serial")
	public static class TeilnehmerGibtEsNichtExc extends multex.Exc {}	
	

}
