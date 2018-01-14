
	package turnier.webapp.domain.imports;

	import java.util.Optional;
	import java.util.List;

	import turnier.webapp.domain.Nutzer;
	import turnier.webapp.domain.Turnier;
import turnier.webapp.domain.TurnierBracket;


	public interface TurnierBracketRepository {
		
		/**Required repository for {@link TurnierBracket} objects.
		 * @author se2 letzte Gruppe
		 * @version 2017-01-08
		 * @since 2017-01-08
		 */
		
		
		/**Returns all of {@link Turnier} objects as list, which {@link Nutzer} organised , if existing.
	     * @param nutzer nutzer organiser
	     * @throws IllegalArgumentException  id is null
	     */
	//	List<TurnierBracket> findTurnierBracketVonTurnier(Turnier turnier);
				
	   /**löscht alle {@link TurnierBracket} Objekte. Brauchbar für Test Cases mit einem leeren DB*/
	   void deleteAll();
	    
	    /**Gives the {@link TurnierBracket} a unique, higher ID and saves the turnierBracket.
	     * @return the modified instance*/
	    TurnierBracket save(TurnierBracket turnierBracket);
	    
	    
	    /**Finds all {@link Turnier}s and returns them ordered by descending name.*/
	    //List<TurnierBracket> findAll();

	    /**Deletes {@link TurnierBracket} object with given Id*/
		//void delete(Long id);

	}


