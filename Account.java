// 계좌 데이터 모델 (Account ID, 소유자, 잔액 관리)

public class Account {
    private String accountId;
    private String accountPassword;
    private String owner;
    private double balance;

    public Account(String accountId, String accountPassword, String owner, double balance) {
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.owner = owner;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

     public String getAccountPassword() {
        return accountPassword;
    }
}
