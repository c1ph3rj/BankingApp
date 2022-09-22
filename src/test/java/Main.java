import java.io.FileReader;
import java.io.IOException;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;

class UserDetails {
    int pin;
    int balance;
    int accountNo;
    String accountType;
    String name;

    UserDetails(String name, int accountNo, int pin, String accountType, int balance) {
        this.pin = pin;
        this.balance = balance;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.name = name;
    }

}

class Main {

    public static void main(String[] args) throws IOException, ParseException {
        Object obj = new JSONParser()
                .parse(new FileReader("C:\\Users\\Temp-user3\\eclipse-workspace\\Jee\\src\\Database.json"));
        JSONArray DB = (JSONArray) obj;
        List<UserDetails> db = new ArrayList<>();
        int data;
        for (int i = 0; i < DB.size(); i++) {
           data  = (int) ((JSONObject) DB.get(i)).get("balance");
//			db.add(new UserDetails((String) ((JSONObject)DB.get(i).get("name")),
//					(int) ((JSONObject) DB.get(i)).get("accountNo"), (int) ((JSONObject) DB.get(i)).get("pin"),
//					(String) ((JSONObject) DB.get(i)).get("accountType"),
//					(int) ((JSONObject) DB.get(i)).get("balance")));
            System.out.println(data);
        }
//		for (int i = 0; i < db.size(); i++) {
//			System.out.println(db.get(i).name);
//		}
    }
}
