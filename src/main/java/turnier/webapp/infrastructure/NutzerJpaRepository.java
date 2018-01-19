package turnier.webapp.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.infrastructure.imports.ImportedNutzerJpaRepository;

/**
 * Repository für Nutzerentitäten, die mit Spring Data JPA implementiert wurden.
 * 
 * @author se2 letzte Gruppe
 * @since 2017-10-01
 */
// Komponente ohne Zustand, nur mit Methoden
@Service
public class NutzerJpaRepository implements NutzerRepository {

	private final ImportedNutzerJpaRepository impl;

	// Für alle Parameter werden typmaessig passende Beans (Komponenten) gesucht
	// oder erzeugt
	@Autowired
	public NutzerJpaRepository(final ImportedNutzerJpaRepository impl) {
		this.impl = impl;
	}

	@Override
	public Nutzer find(String nutzername) {
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
	public void delete(Long id) {
		impl.deleteById(id);

	}

}
