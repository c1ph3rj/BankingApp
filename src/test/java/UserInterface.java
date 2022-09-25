//Importing the required packages.
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//_______________ Business Logic ________________
class UserInterface {
    //Creating the private variables , these variables are used inside these class and cannot be accessed outside the class.
    private final Scanner scanner = new Scanner(System.in);
    String response = "";
    private JSONArray DB;
    private final List < UserDetails > db = new ArrayList < > ();
    private void clearScreen() {
        //Method to clear a screen.
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n__________ Java Bank __________\n");
    }

    private void userDatabase() throws IOException, ParseException {
        //Method to extract the data and store the values in list of class object from the json file.
        //extracting the data as the object using JSONParser.
        Object obj = new JSONParser().parse(new FileReader("src/test/java/Database.json"));
        //the JSONObject is in an array form so restore it as a JSON array.
        DB = (JSONArray) obj;
        //Looping through the array of object.
        for (Object item: DB) {
            //Storing the object values in a variables.
            String name = (String)((JSONObject) item).get("name");
            int accountNo = ((Long)((JSONObject) item).get("accountNo")).intValue();
            int pin = ((Long)((JSONObject) item).get("pin")).intValue();
            String accountType = (String)((JSONObject) item).get("accountType");
            int balance = ((Long)((JSONObject) item).get("balance")).intValue();
            //Adding the Object values to the ArrayList of Class constructors.
            db.add(new UserDetails(name, accountNo, pin, accountType, balance));
        }
    }

    private void update() throws IOException {
        //Method to store the data after Modified.
        FileWriter fileWriter = new FileWriter("src/test/java/Database.json");
        fileWriter.write(DB.toJSONString());
        fileWriter.close();
    }

    void userVerification() throws IOException {
        //Method to verify the user. if the user have credentials it  allows the user otherwise not.
        boolean userVerified = false;
        System.out.println("Enter your name :");
        String userName = scanner.nextLine();
        //looping through the array list of class.
        for (int i = 0; i < db.size(); i++) {
            //verifying the username.
            if (userName.equals(db.get(i).getName())) {
                //If true it enters the loop.
                clearScreen();
                System.out.println("Welcome " + userName + "\nEnter pin :\n");
                //setting variables for attempts.
                int noOfAttempts = 1;
                //If the attempt is greater than 3 it won't execute.
                while (noOfAttempts <= 3) {
                    String checkPin = scanner.nextLine();
                    try { //try the block of code if they throw exception or not.
                        //verifying the user pin, to confirm the user.
                        int pin = Integer.parseInt(checkPin);
                        if (pin == db.get(i).getPin()) {
                            clearScreen();
                            //If the user enters correct pin it execute to the next step.
                            userAccountOperations(db.indexOf(db.get(i)));
                            break;
                        }
                        //Statements to execute if the pin is wrong.
                        else if (String.valueOf(pin).length() < 4) {
                            System.out.println(((noOfAttempts == 3) ? "Maximum Attempts reached.\nThank you!" :
                                    "wrong pin!,Try again."));
                            //Increasing the no of attempts based on condition.
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
        if (!userVerified) // if the user is not on the db it prints the statements.
            System.out.println("No users found");
    }

    private void userAccountOperations(int i) throws IOException {
        //Method for displaying the options and asking the correct account type.
        System.out.println("Enter account type: \nPress 1 - Savings account\nPress 2 - Current Account\n>>>");
        String accountType;
        do {
            String accountTypeInput = scanner.next();

            accountType = (accountTypeInput.equals("1")) ? "Savings" : (accountTypeInput.equals("2")) ? "Current" : "unknown";
            if (accountType.equals(db.get(i).getAccountType())) {
                clearScreen();
                System.out.println("Select one of the option below to continue :");
                //Displaying the options to the user and asking for the input.
                System.out.printf("1.Deposit\n2.Withdraw\n3.CheckBalance\n4.ChangePin\npress 5 to quit.%n");
                Pattern pattern = Pattern.compile("^[1-5]$");
                do {
                    String option = scanner.next();
                    Matcher matcher = pattern.matcher(option);
                    switch (option) {
                        //executes the option based on the users input.
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
        } while (accountType.equals("unknown") || response.equals("Enter a valid account type")); // loops execute until the enters the correct value.
    }
    void checkBalance(int i) {
        //Method to check the balance.
        clearScreen();
        System.out.println("Your " + db.get(i).getAccountType() + " Account balance is :\n" + db.get(i).getBalance());
        System.out.println("Thank you " + db.get(i).getName());
    }
    void displayBalance(int i) throws IOException {
        //Method to ask the user if the want balance or not. based on the user input it executes the code.
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
        } while (response.contains("valid option"));//loops execute until getting correct input from the user.
    }
    void deposit(int i) {
        //Method to deposit the amount to the account.
        clearScreen();
        System.out.println("Enter a amount to be deposit: ");
        do {
            String checkAmount = scanner.next();
            try { // try the block of code and catch if they throw any exception.
                int amount = Integer.parseInt(checkAmount);
                if (((amount % 100) == 0) &&
                        amount >= 100) {
                    amount = db.get(i).getBalance() + amount;
                    db.get(i).setBalance(amount);
                    //adding the amount to the db.
                    ((JSONObject) DB.get(i)).replace("balance", amount);
                    displayBalance(i);
                } else {
                    response = "Invalid Amount! , Try again.";
                    System.out.println(response);
                }
            } catch (Exception e) { //Catch the exception if try block throws any.
                response = "Invalid Amount! , Try again.";
                System.out.println(response);
            }
        } while (response.equals("Invalid Amount! , Try again.")); //loop execute until correct input from the user.
    }
    void withdrawal(int i) throws IOException {
        //Method to withdraw money form your account.
        clearScreen();
        System.out.println("Enter the amount to be withdraw\n(note: only in multiples of 100)\n>>>");
        do {
            try {// try the block of code,if it throws any exception or not.
                int amount = scanner.nextInt();
                if (amount < db.get(i).getBalance() && ((amount % 100) == 0) &&
                        amount >= 100) {
                    amount = db.get(i).getBalance() - amount;
                    db.get(i).setBalance(amount);
                    //reducing the amount from the db.
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

        } while (response.equals("Invalid Amount! , Try again.")); //Loops execute until it occur the correct input from the user.
    }

    void changePin(int i) {
        //Method to change the pin of the user.
        clearScreen();
        String pinBeLike = "^[0-9]{4}$"; //verifying if the user enter the pin in correct format or not using regex.
        Pattern pinPattern = Pattern.compile(pinBeLike);
        do {
            System.out.println("Enter your new pin:");
            String pin = scanner.next();
            Matcher matcher = pinPattern.matcher(pin);
            try { // try and if block throws excepts, catch block executes.
                if (Integer.parseInt(pin) == db.get(i).getPin()) {
                    response = "This is your old pin!\nEnter valid pin to continue.";
                } else if (matcher.matches()) {
                    response = "Your pin has been Changed successfully\nLogin again to continue.\nThank-you " + db.get(i).getName();
                    //Storing the new pin to the db.
                    ((JSONObject) DB.get(i)).put("pin", Integer.parseInt(pin));
                    update();
                } else {
                    response = "Enter a valid pin";
                }
            } catch (Exception e) {
                response = "Enter a valid pin";
            }
            System.out.println(response);
        } while (response.contains("valid")); // loops continue until the user enter the correct input.
    }


    UserInterface() throws IOException, ParseException {
        //Constructor to call the methods used in the class.
        userDatabase();
        userVerification();
    }
}