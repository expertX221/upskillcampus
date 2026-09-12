import java.util.*;

/**
 * Banking Information System
 * Core Java Internship & Project - upskill Campus / UCT
 *
 * A console-based banking application demonstrating:
 * - Account creation
 * - Deposit and withdrawal
 * - Money transfer
 * - Balance enquiry
 * - Transaction history
 * - Simple PIN authentication
 */
public class BankingInformationSystem {

    static class Transaction {
        private final String type;
        private final double amount;
        private final double balanceAfter;
        private final String details;
        private final Date date;

        Transaction(String type, double amount, double balanceAfter, String details) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.details = details;
            this.date = new Date();
        }

        @Override
        public String toString() {
            return String.format(
                "%-12s Amount: ₹%.2f | Balance: ₹%.2f | %s | %s",
                type, amount, balanceAfter, details, date
            );
        }
    }

    static class Account {
        private final String accountNumber;
        private final String name;
        private final String phone;
        private final String pin;
        private double balance;
        private final List<Transaction> transactions = new ArrayList<>();

        Account(String accountNumber, String name, String phone, String pin, double openingBalance) {
            this.accountNumber = accountNumber;
            this.name = name;
            this.phone = phone;
            this.pin = pin;
            this.balance = openingBalance;

            transactions.add(new Transaction(
                "OPENING", openingBalance, balance, "Account opened"
            ));
        }

        String getAccountNumber() {
            return accountNumber;
        }

        String getName() {
            return name;
        }

        String getPhone() {
            return phone;
        }

        boolean authenticate(String enteredPin) {
            return pin.equals(enteredPin);
        }

        double getBalance() {
            return balance;
        }

        boolean deposit(double amount) {
            if (amount <= 0) {
                return false;
            }

            balance += amount;
            transactions.add(new Transaction(
                "DEPOSIT", amount, balance, "Cash deposited"
            ));
            return true;
        }

        boolean withdraw(double amount) {
            if (amount <= 0 || amount > balance) {
                return false;
            }

            balance -= amount;
            transactions.add(new Transaction(
                "WITHDRAW", amount, balance, "Cash withdrawn"
            ));
            return true;
        }

        void addTransferTransaction(double amount, double newBalance, String details) {
            transactions.add(new Transaction(
                "TRANSFER", amount, newBalance, details
            ));
        }

        void showTransactions() {
            if (transactions.isEmpty()) {
                System.out.println("No transactions available.");
                return;
            }

            System.out.println("\n--- Transaction History ---");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }

        void showDetails() {
            System.out.println("\n--- Account Details ---");
            System.out.println("Account Number : " + accountNumber);
            System.out.println("Account Holder : " + name);
            System.out.println("Phone Number   : " + phone);
            System.out.printf("Balance        : ₹%.2f%n", balance);
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, Account> accounts = new LinkedHashMap<>();
    private static int nextAccountNumber = 1001;

    public static void main(String[] args) {
        seedDemoAccount();

        System.out.println("========================================");
        System.out.println("      BANKING INFORMATION SYSTEM");
        System.out.println("========================================");

        while (true) {
            showMainMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Thank you for using Banking Information System.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1, 2 or 3.");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n1. Create Account");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    private static void seedDemoAccount() {
        Account demo = new Account(
            "1000",
            "Demo User",
            "9999999999",
            "1234",
            5000.00
        );
        accounts.put(demo.getAccountNumber(), demo);
    }

    private static void createAccount() {
        System.out.println("\n--- Create New Account ---");

        String name = readNonEmpty("Enter account holder name: ");
        String phone = readPhone("Enter 10-digit phone number: ");
        String pin = readPin("Set a 4-digit PIN: ");
        double openingBalance = readNonNegativeDouble("Enter opening balance: ");

        String accountNumber = String.valueOf(nextAccountNumber++);
        Account account = new Account(
            accountNumber, name, phone, pin, openingBalance
        );

        accounts.put(accountNumber, account);

        System.out.println("\nAccount created successfully.");
        System.out.println("Your account number is: " + accountNumber);
    }

    private static void login() {
        System.out.println("\n--- Account Login ---");

        String accountNumber = readNonEmpty("Enter account number: ");
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        String pin = readNonEmpty("Enter PIN: ");

        if (!account.authenticate(pin)) {
            System.out.println("Incorrect PIN.");
            return;
        }

        System.out.println("Login successful. Welcome, " + account.getName() + "!");
        accountMenu(account);
    }

    private static void accountMenu(Account account) {
        while (true) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Account Details");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Check Balance");
            System.out.println("6. Transaction History");
            System.out.println("7. Logout");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    account.showDetails();
                    break;
                case 2:
                    deposit(account);
                    break;
                case 3:
                    withdraw(account);
                    break;
                case 4:
                    transfer(account);
                    break;
                case 5:
                    System.out.printf("Current Balance: ₹%.2f%n", account.getBalance());
                    break;
                case 6:
                    account.showTransactions();
                    break;
                case 7:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void deposit(Account account) {
        double amount = readPositiveDouble("Enter deposit amount: ");

        if (account.deposit(amount)) {
            System.out.printf("Deposit successful. New balance: ₹%.2f%n",
                    account.getBalance());
        } else {
            System.out.println("Deposit failed.");
        }
    }

    private static void withdraw(Account account) {
        double amount = readPositiveDouble("Enter withdrawal amount: ");

        if (account.withdraw(amount)) {
            System.out.printf("Withdrawal successful. New balance: ₹%.2f%n",
                    account.getBalance());
        } else {
            System.out.println("Withdrawal failed. Check amount and available balance.");
        }
    }

    private static void transfer(Account sender) {
        String receiverNumber = readNonEmpty("Enter receiver account number: ");

        if (sender.getAccountNumber().equals(receiverNumber)) {
            System.out.println("Sender and receiver cannot be the same account.");
            return;
        }

        Account receiver = accounts.get(receiverNumber);

        if (receiver == null) {
            System.out.println("Receiver account not found.");
            return;
        }

        double amount = readPositiveDouble("Enter transfer amount: ");

        if (amount > sender.getBalance()) {
            System.out.println("Transfer failed. Insufficient balance.");
            return;
        }

        sender.withdraw(amount);
        receiver.deposit(amount);

        // Replace generic deposit/withdraw descriptions with transfer descriptions.
        sender.addTransferTransaction(
            amount,
            sender.getBalance(),
            "Transferred to A/C " + receiverNumber
        );

        receiver.addTransferTransaction(
            amount,
            receiver.getBalance(),
            "Received from A/C " + sender.getAccountNumber()
        );

        System.out.printf("Transfer successful. New balance: ₹%.2f%n",
                sender.getBalance());
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static String readNonEmpty(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    private static String readPhone(String message) {
        while (true) {
            String phone = readNonEmpty(message);

            if (phone.matches("\\d{10}")) {
                return phone;
            }

            System.out.println("Phone number must contain exactly 10 digits.");
        }
    }

    private static String readPin(String message) {
        while (true) {
            String pin = readNonEmpty(message);

            if (pin.matches("\\d{4}")) {
                return pin;
            }

            System.out.println("PIN must contain exactly 4 digits.");
        }
    }

    private static double readPositiveDouble(String message) {
        while (true) {
            double value = readDouble(message);

            if (value > 0) {
                return value;
            }

            System.out.println("Amount must be greater than zero.");
        }
    }

    private static double readNonNegativeDouble(String message) {
        while (true) {
            double value = readDouble(message);

            if (value >= 0) {
                return value;
            }

            System.out.println("Amount cannot be negative.");
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric amount.");
            }
        }
    }
}
