package turnier.webapp.domain.imports;

import java.util.List;
import java.util.Optional;

import turnier.webapp.domain.Account;
import turnier.webapp.domain.AccountAccess;
import turnier.webapp.domain.Amount;
import turnier.webapp.domain.Client;

/**Required repository for {@link turnier.webapp.domain.AccountAccess} objects.
 * @author  Michal Kubacki
 * @since 2017-03-08
 */
public interface AccountAccessRepository {

    /**Deletes all {@link AccountAccess} objects. Linked {@link Client}s or {@link Account}s must be deleted before.*/
    void deleteAll();

    /**Saves the passed object. Linked {@link Client}s or {@link Account}s must be saved before.
     * @return the saved instance*/
    AccountAccess save(AccountAccess accountAccess);

    /**Deletes the given {@link AccountAccess} object.*/
    void delete(AccountAccess accountAccess);

    /**Returns all {@link AccountAccess} objects, which the given client may manage.
     * @param asOwner if true returns only {@link AccountAccess} objects, where the {@link Client} is owner.
     */
    List<AccountAccess> findManagedAccountsOf(Client client, boolean asOwner);

    /**Returns all {@link AccountAccess} objects, which have a reference to an account with a balance equal or greater than the given minBalance.
     * @return access objects to the full accounts, ordered by the descending balance, secondly by descending ids of their managing clients.*/
    List<AccountAccess> findFullAccounts(final Amount minBalance);

    /**Returns the {@link AccountAccess} object for the given client and account, if existing.*/
    Optional<AccountAccess> find(Client client, Account account);

}
