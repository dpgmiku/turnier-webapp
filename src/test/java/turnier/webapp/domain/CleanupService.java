package turnier.webapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.imports.AdminRepository;
import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.domain.imports.TurnierBracketRepository;
import turnier.webapp.domain.imports.TurnierRepository;

/**
 * Es ist ein Domain Test Scope Service zum Aufräumen vom DB.
 * 
 * @author Michal Kubacki
 * @since 2017-12-03
 */
@Service
class CleanupService {

	private final NutzerRepository nutzerRepository;
	private final TurnierRepository turnierRepository;
	private final AdminRepository adminRepository;
	private final TurnierBracketRepository turnierBracketRepository;

	/**
	 * kreirt einen Aufräumservice mit Hilfe von den erforderlichen Repositories von
	 * Ports und Adapters Pattern
	 * 
	 * @param nutzerRepository
	 *            Repository von Nutzerentitäten
	 * @param turnierRepository
	 *            Repository von Turnierentitäten
	 * @param adminRepository
	 *            Repository von Adminentitäten
	 * @param turnierBracketRepository
	 *            Repository von TurnierBracketentitäten
	 */
	@Autowired
	public CleanupService(final NutzerRepository nutzerRepository, final TurnierRepository turnierRepository,
			final AdminRepository adminRepository, final TurnierBracketRepository turnierBracketRepository) {
		this.nutzerRepository = nutzerRepository;
		this.turnierRepository = turnierRepository;
		this.adminRepository = adminRepository;
		this.turnierBracketRepository = turnierBracketRepository;
	}

	/** Deletes all entities from all used repositories. */
	void deleteAll() {
		turnierRepository.deleteAll();
		nutzerRepository.deleteAll();
		adminRepository.deleteAll();
		turnierBracketRepository.deleteAll();
	}

}
