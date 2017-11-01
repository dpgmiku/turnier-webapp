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

}
