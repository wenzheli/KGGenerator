import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Email entity and its fields.
 *
 * @author wenzheli
 * @date Sep 19 2018
 */
public class Email{

    private FlagType flagType = FlagType.WHITE;  // WHITE or BLACK
    private String address = "";   // email address
    private List<String> messages = new ArrayList<String>(); // email message, each message is a long string

    public Email(String address, List<String> messages){
        this.address = address;
        this.messages = messages;
    }

    /**
     * Constructor, randomly generate some fields
     */
    public Email(){
        // generate random email address
        String[] postFixs = new String[]{"sina.com", "gmail.com", "163.com",
                "hotmail.com", "126.com"};

        // generate random 5-digit numbers
        for (int i = 0; i < 5; i++){
            this.address += 1 + Math.abs(new Random().nextInt()) % 9;
        }

        this.address += "@" + postFixs[Math.abs(new Random().nextInt()) % 5];
    }

    public void setMessages(List<String> messages){
        this.messages = messages;
    }

    public List<String> getMessages(){
        return messages;
    }

    public String getMessage(){
        return address;
    }

    public String getAddress(){
        return address;
    }

    public FlagType getFlagType(){
        return flagType;
    }

    public void setFlagType(FlagType flagType){
        this.flagType = flagType;
    }

    public static String getHeader(){
        return "address,flagType,messages";
    }

    public String toString(){
        String res = address + "," + flagType + ",[";
        for (int i = 0; i < messages.size()-1; i++){
            res += messages.get(i) + ",";
        }
        if (messages.size() > 0) {
            res += messages.get(messages.size()-1);
        }
        res += "]";

        return res;
    }
}
