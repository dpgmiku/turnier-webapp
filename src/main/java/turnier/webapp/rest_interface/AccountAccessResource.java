package turnier.webapp.rest_interface;

import turnier.webapp.domain.Account;
import turnier.webapp.domain.AccountAccess;

/**Data about an Account of a bank, and the access a Client of the bank has to it. Usable as Data Transfer Object.*/
public class AccountAccessResource {
	
	public Long clientId;
	public boolean isOwner;
	public Long accountId;
	public String accountName;
	public String accountBalance;
	
	/**Constructs a data transfer object from the given domain entity.*/
	public AccountAccessResource(final AccountAccess entity){
    	final Account account = entity.getAccount();
		this.clientId = entity.getClient().getId();
		this.isOwner = entity.isOwner();
		this.accountId = account.getId();
		this.accountName = account.getName();
		this.accountBalance = Double.toString(account.getBalance().toDouble());
	}

}
