import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    String response = "";
    private JSONArray DB;
    private final List < UserDetails > db = new ArrayList < > ();
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n__________ Java Bank __________\n");
    }

    private void userDatabase() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader("src/test/java/Database.json"));
        DB = (JSONArray) obj;
        for (Object item: DB) {
            String name = (String)((JSONObject) item).get("name");
            int accountNo = ((Long)((JSONObject) item).get("accountNo")).intValue();
            int pin = ((Long)((JSONObject) item).get("pin")).intValue();
            String accountType = (String)((JSONObject) item).get("accountType");
            int balance = ((Long)((JSONObject) item).get("balance")).intValue();
            db.add(new UserDetails(name, accountNo, pin, accountType, balance));
        }
    }

    private void update() throws IOException {
        FileWriter fileWriter = new FileWriter("src/test/java/Database.json");
        fileWriter.write(DB.toJSONString());
        fileWriter.close();
    }

    void userVerification() throws IOException {
        boolean userVerified = false;
        System.out.println("Enter your name :");
        String userName = scanner.nextLine();
        for (int i = 0; i < db.size(); i++) {
            if (userName.equals(db.get(i).getName())) {
                clearScreen();
                System.out.println("Welcome " + userName + "\nEnter pin :\n");
                int noOfAttempts = 1;
                while (noOfAttempts <= 3) {
                    String checkPin = scanner.nextLine();
                    try {
                        int pin = Integer.parseInt(checkPin);
                        if (pin == db.get(i).getPin()) {
                            clearScreen();
                            userAccountOperations(db.indexOf(db.get(i)));
                            break;
                        } else if (String.valueOf(pin).length() < 4) {
                            System.out.println(((noOfAttempts == 3) ? "Maximum Attempts reached.\nThank you!" :
                                    "wrong pin!,Try again."));
                            noOfAttempts++;
                        } else {
                            System.out.println(((noOfAttempts == 3) ? "Maximum Attempts reached.\nThank you!" :
                                    "wrong pin!,Try again."));
                            noOfAttempts++;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(((noOfAttempts == 3) ? "Maximum Attempts reached.\nThank you!" :
                                "wrong pin!,Try again."));
                        noOfAttempts++;
                    }
                }
                userVerified = true;
            }
        }
        if (!userVerified)
            System.out.println("No users found");
    }
    private void userAccountOperations(int i) throws IOException {
        System.out.println("Enter account type: \nPress 1 - Savings account\nPress 2 - Current Account\n>>>");
        String accountType;
        do {
            String accountTypeInput = scanner.next();

            accountType = (accountTypeInput.equals("1")) ? "Savings" : (accountTypeInput.equals("2")) ? "Current" : "unknown";
            if (accountType.equals(db.get(i).getAccountType())) {
                clearScreen();
                System.out.println("Select one of the option below to continue :");
                System.out.printf("1.Deposit\n2.Withdraw\n3.CheckBalance\n4.ChangePin\npress 5 to quit.%n");
                Pattern pattern = Pattern.compile("^[1-5]$");
                do {
                    String option = scanner.next();
                    Matcher matcher = pattern.matcher(option);
                    switch (option) {
                        case "1" -> deposit(i);
                        case "2" -> withdrawal(i);
                        case "3" -> checkBalance(i);
                        case "4" -> changePin(i);
                        case "5" -> {
                                response = "Thank you visit again.";
                            System.out.println(response);
                        }
                        default -> {
                                response = String.valueOf(matcher.matches());
                            System.out.println("Enter a valid option from above");
                        }
                    }
                } while (response.equals("false"));
            } else {
                response = "Enter a valid account type";
                System.out.println(response);
            }
        } while (accountType.equals("unknown") || response.equals("Enter a valid account type"));
    }
    void checkBalance(int i) {
        clearScreen();
        System.out.println("Your " + db.get(i).getAccountType() + " Account balance is :\n" + db.get(i).getBalance());
        System.out.println("Thank you " + db.get(i).getName());
    }
    void displayBalance(int i) throws IOException {
        update();
        clearScreen();
        System.out.println(
                "Do you want to display the balance :\nPress 1 - display balance\nPress 2 - cancel");
        do {
            int balanceDisplay = scanner.nextInt();
            response = (balanceDisplay == 1) ?
                    ("Your account balance is : \n" + db.get(i).getBalance()) + ("\nThank You " + db.get(i).getName()) :
                    (balanceDisplay == 2) ? ("Thank You " + db.get(i).getName()) : "Enter a valid option!";
            System.out.println(response);
        } while (response.contains("valid option"));
    }
    void deposit(int i) {
        clearScreen();
        System.out.println("Enter a amount to be deposit: ");
        do {
            String checkAmount = scanner.next();
            try {
                int amount = Integer.parseInt(checkAmount);
                if (((amount % 100) == 0) &&
                        amount >= 100) {
                    amount = db.get(i).getBalance() + amount;
                    db.get(i).setBalance(amount);
                    ((JSONObject) DB.get(i)).replace("balance", amount);
                    displayBalance(i);
                } else {
                    response = "Invalid Amount! , Try again.";
                    System.out.println(response);
                }
            } catch (Exception e) {
                response = "Invalid Amount! , Try again.";
                System.out.println(response);
            }
        } while (response.equals("Invalid Amount! , Try again."));
    }
    void withdrawal(int i) throws IOException {
        clearScreen();
        System.out.println("Enter the amount to be withdraw\n(note: only in multiples of 100)\n>>>");
        do {
            try {
                int amount = scanner.nextInt();
                if (amount < db.get(i).getBalance() && ((amount % 100) == 0) &&
                        amount >= 100) {
                    amount = db.get(i).getBalance() - amount;
                    db.get(i).setBalance(amount);
                    ((JSONObject) DB.get(i)).replace("balance", amount);
                    displayBalance(i);
                    break;
                } else {
                    response = "Invalid Amount! , Try again.";
                }
            } catch (InputMismatchException e) {
                response = "Invalid Amount! , Try again.";
            }
            System.out.println(response);

        } while (response.equals("Invalid Amount! , Try again."));
    }

    void changePin(int i) {
        clearScreen();
        String pinBeLike = "^[0-9]{4}$";
        Pattern pinPattern = Pattern.compile(pinBeLike);
        do {
            System.out.println("Enter your new pin:");
            String pin = scanner.next();
            Matcher matcher = pinPattern.matcher(pin);
            try {
                if (Integer.parseInt(pin) == db.get(i).getPin()) {
                    response = "This is your old pin!\nEnter valid pin to continue.";
                } else if (matcher.matches()) {
                    response = "Your pin has been Changed successfully\nLogin again to continue.\nThank-you " + db.get(i).getName();
                    ((JSONObject) DB.get(i)).put("pin", Integer.parseInt(pin));
                    update();
                } else {
                    response = "Enter a valid pin";
                }
            } catch (Exception e) {
                response = "Enter a valid pin";
            }
            System.out.println(response);
        } while (response.contains("valid"));
    }


    UserInterface() throws IOException, ParseException {
        userDatabase();
        userVerification();
    }
}