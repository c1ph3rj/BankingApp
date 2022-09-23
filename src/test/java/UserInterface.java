import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;

class UserInterface{
    private Scanner scanner = new Scanner(System.in);
    private static List<UserDetails> db = new ArrayList<>();
    private void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n__________ Java Bank __________\n");
    }

    private void userDatabase() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader("D:\\projects\\Java\\intellij\\BankingApp\\src\\test\\java\\Database.json"));
        //FileWriter fileWriter = new FileWriter("D:\\projects\\Java\\intellij\\BankingApp\\src\\test\\java\\Database.json");
        JSONArray DB = (JSONArray) obj;

        for (Object item : DB) {
            String name = (String) ((JSONObject) item).get("name");
            int accountNo = ((Long) ((JSONObject) item).get("accountNo")).intValue();
            int pin = ((Long) ((JSONObject) item).get("pin")).intValue();
            String accountType = (String) ((JSONObject) item).get("accountType");
            int balance = ((Long) ((JSONObject) item).get("balance")).intValue();
            db.add(new UserDetails(name, accountNo
                    , pin, accountType
                    , balance
            ));
        }
    }

    void userVerification() {
        boolean userverified = false;
        clearScreen();
        System.out.println("Enter your name :");
        String userName = scanner.nextLine();
        for(int i = 0;i<db.size();i++){
            if (userName.equals(db.get(i).getName())) {
                clearScreen();
                System.out.println("Welcome "+ userName +"\nEnter pin :\n");
                int noOfAttempts = 1;
                while (noOfAttempts <= 3) {
                    int pin = scanner.nextInt();
                    if (pin == db.get(i).getPin()) {
                        clearScreen();
                        userAccountOperations(db.indexOf(db.get(i)));
                        System.out.println("done");
                        break;
                    } else if (String.valueOf(pin).length() < 4) {
                        System.out.println(((noOfAttempts == 3) ? "Maximum Attempts reached.\nThank you!"
                                : "wrong pin!,Try again."));
                        noOfAttempts++;
                    } else {
                        System.out.println(((noOfAttempts == 3) ? "Maximum Attempts reached.\nThank you!"
                                : "wrong pin!,Try again."));
                        noOfAttempts++;
                    }
                }
                userverified = true;
            }
        }
        if(userverified == false)
            System.out.println("No users found");
    }
    private void userAccountOperations( int i) {

        System.out.println("Enter account type: \nPress 1 - Savings account\nPress 2 - Current Account\n>>>");
        String accountType;
        do{
            int accountTypeInput = scanner.nextInt();
            accountType = (accountTypeInput == 1) ? "Savings" : (accountTypeInput == 2) ? "Current" : "unknown";
            if (accountType.equals(db.get(i).getAccountType())) {
                clearScreen();
                System.out.println("Enter the amount to be withdraw\n(note: only in multiples of 100)\n>>>");
                String response ="Invalid Amount! , Try again.";
                while(response =="Invalid Amount! , Try again."){
                    int amount = scanner.nextInt();
                    if (amount < db.get(i).getBalance() && ((amount % 100) == 0)
                            && amount >= 100) {
                        amount = db.get(i).getBalance() - amount;
                        db.get(i).setBalance(amount);
                        clearScreen();
                        System.out.println(
                                "Do you want to display the balance :\nPress 1 - display blance\nPress 2 - cancel");
                        do{
                            int balanceDisplay = scanner.nextInt();
                            response = (balanceDisplay == 1)
                                    ? ("Your account balance is : \n" + db.get(i).getBalance())
                                    : (balanceDisplay == 2) ? ("Thank You " + db.get(i).getName()) : "Enter a valid option!";
                            System.out.println(response);
                        }while(response.contains("valid option"));
                    } else{
                        response = "Invalid Amount! , Try again.";
                        System.out.println(response);
                    }

                }
            } else
                System.out.println("Enter a valid account type");
        }while(accountType == "unknown");
    }

    UserInterface() throws IOException, ParseException {
//        dataBase();
        userDatabase();
        userVerification();
    }
}