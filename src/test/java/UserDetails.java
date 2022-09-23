
class UserDetails {
    private int pin;
    private int balance;
    private final int accountNo;
    private final String accountType;
    public final String name;

    UserDetails(String name, int accountNo, int pin, String accountType, int balance) {
        this.pin = pin;
        this.balance = balance;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.name = name;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {

        this.balance = balance;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getName() {
        return name;
    }

}