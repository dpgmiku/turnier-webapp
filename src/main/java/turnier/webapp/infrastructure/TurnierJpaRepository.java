package turnier.webapp.infrastructure;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;
import turnier.webapp.domain.imports.TurnierRepository;
import turnier.webapp.infrastructure.imports.ImportedTurnierJpaRepository;
/**A Repository for Nutzer entities implemented with Spring Data JPA.
 * @author se2 letzte Gruppe
 * @since 2017-10-01
 */
//Komponente ohne Zustand, nur mit Methoden
@Service
public class TurnierJpaRepository implements TurnierRepository {
	
	private final ImportedTurnierJpaRepository impl;
	
	//Für alle Parameter werden typmaessig passende Beans (Komponenten) gesucht oder erzeugt
	   @Autowired
	    public TurnierJpaRepository(final ImportedTurnierJpaRepository impl) {
	        this.impl = impl;
	    }

	@Override
	public Turnier find(String name) {
		return impl.findOneByName(name);
	}

	@Override
	public List<Turnier> findTurniereVonNutzer(Nutzer nutzer) {
		return impl.findByOrganisator(nutzer);
	}

	@Override
	public void deleteAll() {
      impl.deleteAll();		
	}

	@Override
	public Turnier save(Turnier turnier) {
		return impl.save(turnier);
	}

	@Override
	public List<Turnier> findAll() {
		return impl.findAllByOrderByNameAsc();
	}

	@Override
	public void delete(Long id) {
      impl.deleteById(id);		
	}


	
	
}
