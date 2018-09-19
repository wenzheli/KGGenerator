import java.util.ArrayList;
import java.util.List;

/**
 * BlackList (黑名单库） three types of
 * entities, person, email and phone
 */
public class BlackList {
    private List<String> personIds;  // list of person ids
    private List<String> emailIds;   // list of email ids
    private List<String> phoneIds;   // list of phone ids

    public BlackList(){
        personIds = new ArrayList<String>();
        emailIds = new ArrayList<String>();
        phoneIds = new ArrayList<String>();
    }

    public void setPersons(List<String> persons){
        this.personIds = persons;
    }

    public void setEmails(List<String> emails){
        this.emailIds = emails;
    }

    public void setPhones(List<String> phones){
        this.phoneIds = phones;
    }

    public void addPerson(String personId){
        this.personIds.add(personId);
    }

    public void addEmail(String emailId){
        this.emailIds.add(emailId);
    }

    public void addPhone(String phoneId){
        this.phoneIds.add(phoneId);
    }

    public List<String> getPersonIds(){
        return personIds;
    }

    public List<String> getEmailIds(){
        return emailIds;
    }

    public List<String> getPhoneIds(){
        return phoneIds;
    }
}

