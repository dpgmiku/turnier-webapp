package turnier.webapp.infrastructure;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.infrastructure.imports.ImportedNutzerJpaRepository;
/**A Repository for Nutzer entities implemented with Spring Data JPA.
 * @author se2 letzte Gruppe
 * @since 2017-10-01
 */
//Komponente ohne Zustand, nur mit Methoden
@Service
public class NutzerJpaRepository implements NutzerRepository {
	
	private final ImportedNutzerJpaRepository impl;
	
	//Für alle Parameter werden typmaessig passende Beans (Komponenten) gesucht oder erzeugt
	   @Autowired
	    public NutzerJpaRepository(final ImportedNutzerJpaRepository impl) {
	        this.impl = impl;
	    }
	
	@Override
	public Nutzer find(String nutzername) {
		// TODO Auto-generated method stub
		return impl.findOneByNutzername(nutzername);
	}

	@Override
	public void deleteAll() {
			impl.deleteAll();		
	}

	@Override
	public Nutzer save(Nutzer nutzer) {
		return impl.save(nutzer);
	}
	
	@Override
	public List<Nutzer> findAll() {
		return impl.findAllByOrderByNutzernameAsc();
	}

	@Override
	public Nutzer findEmail(String email) {
		return impl.findOneByEmail(email);
	}

	@Override
	public void updateEmail(Long id, String email) {
	 impl.setEmailById(email, id);
	}

	@Override
	public void updatePasswort(Long id, String passwort) {
      impl.setPasswortById(passwort, id);
	}

	@Override
	public void delete(Long id) {
		impl.deleteById(id);
		
	}
	
}
