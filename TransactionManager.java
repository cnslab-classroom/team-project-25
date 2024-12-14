// 입금, 출금, 계좌 간 송금 처리

class TransactionManager {
    public void deposit(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        System.out.println("Deposited: " + amount);
    }

    public void withdraw(Account account,String password, double amount) {
        if(!account.getAccountPassword().equals(password)){
            throw new IllegalArgumentException("Incorrect password.");
        }
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        account.setBalance(account.getBalance() - amount);
        System.out.println("Withdrew: " + amount);
    }

    public void transfer(Account from, Account to, double amount) {
        withdraw(from,from.getAccountPassword(), amount);
        deposit(to, amount);
        System.out.println("Transferred: " + amount);
    }
}