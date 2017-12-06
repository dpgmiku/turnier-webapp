package turnier.webapp.domain;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;

import javax.persistence.Id;


@Entity
public class Teilnehmer {
	
	@Id
	private String id;
	//private SpielerStatistik statistik;

	
	public Teilnehmer(String id, SpielerStatistik statistik) {
		this.id = id;
		//this.statistik = statistik;
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
	/** Turnier {0} name enthält Ziffer, Ort hat weniger als 3 Zeichen und oder maxTeilnehmer ist keine Ziffer*/
	@SuppressWarnings("serial")
	public static class KeineRichtigeEingabenTurnierExc extends multex.Exc {}
		
	/** maxTeilnehmer {0} ist größer als 32*/
	@SuppressWarnings("serial")
	public static class ZuVieleTeilnehmerExc extends multex.Exc {}
	
	/** Turnier {0} existiert gar nicht im Turnier*/
	@SuppressWarnings("serial")
	public static class TurnierGibtEsNichtExc extends multex.Exc {}
	
	/** Teilnehmer {0} existiert gar nicht im Turnier*/
	@SuppressWarnings("serial")
	public static class TeilnehmerGibtEsNichtExc extends multex.Exc {}
	
	/** Turnier {0} gehört nicht zu diesem Teilnehmer {1}*/
	@SuppressWarnings("serial")
	public static class EsIstNichtDeinTurnierExc extends multex.Exc {}
	
	
}
