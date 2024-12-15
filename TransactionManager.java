// 입금, 출금, 계좌 간 송금 처리
import java.util.Date;

class TransactionManager {
    private TransactionHistory transactionHistory;

    public TransactionManager(TransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public void deposit(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        transactionHistory.logTransaction("입금", account.getAccountId(), amount, new Date());
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(Account account, String password, double amount) {
        if (!account.getAccountPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        account.setBalance(account.getBalance() - amount);
        transactionHistory.logTransaction("출금", account.getAccountId(), -amount, new Date());
        System.out.println("Withdrew: " + amount);
    }

   public void transfer(Account from, Account to, String password, double amount) {
        if (!from.getAccountPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        transactionHistory.logTransaction("송금", from.getAccountId(), amount, new Date());
        transactionHistory.logTransaction("입금", to.getAccountId(), amount, new Date());
        System.out.println("Transferred: " + amount + " from account " + from.getAccountId() + " to account " + to.getAccountId());
    }
}