package turnier.webapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.domain.imports.TurnierRepository;

/**This is a domain test scope service for cleaning the database.
 * @author Michal Kubacki
 * @since 2017-12-03
 */
@Service
class CleanupService {

    private final NutzerRepository nutzerRepository;
    private final TurnierRepository turnierRepository;

     /**Constructs the cleanup service using the passed required repositories as by Ports and Adapters Pattern.*/
    @Autowired
    public CleanupService(final NutzerRepository nutzerRepository, final TurnierRepository turnierRepository) {
        this.nutzerRepository = nutzerRepository;
      this.turnierRepository = turnierRepository;
    }

    /**Deletes all entities from all used repositories.*/
    void deleteAll(){
    nutzerRepository.deleteAll();  
    turnierRepository.deleteAll();
    }

}
