package turnier.webapp.domain.imports;

import java.util.Optional;

import turnier.webapp.domain.Account;

/**Required repository for {@link Account} objects.
 * @author Christoph Knabe
 * @since 2017-03-03
 */
public interface AccountRepository {

    /**Returns the {@link Account} object with the given id, if existing.
     * @throws IllegalArgumentException  id is null
     */
	Optional<Account> find(Long id);

    /**Deletes all Accounts. Useful for test scenarios in order to start with an empty account set.*/
    void deleteAll();

    /**Gives the account a unique, higher ID and saves the client.
     * @return the modified instance*/
    Account save(Account account);

}
