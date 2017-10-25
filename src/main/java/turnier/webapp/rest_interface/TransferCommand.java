package turnier.webapp.rest_interface;

/**Command to transfer the amount in euros from the Account with the given sourceAccountId to the Account with the given destinationAccountId.*/
public class TransferCommand {

	public Long sourceAccountId;
	public Long destinationAccountId;
	public double amount;

    @Override
    public String toString() {
        return String.format(
                "TransferCommand{sourceAccountId=%d, destinationAccountId=%d, amount='%s'}",
                sourceAccountId, destinationAccountId, amount);
    }

}
