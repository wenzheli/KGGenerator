import java.util.Date;
import java.util.Random;

/**
 * The class defines application object
 *
 * @author wenzheli
 * @date Sep 19 2018
 */
public class Apply {

    public enum ApplyStatus{
        IN_PROGREESS, RETURNING, OVERDUE, REPAID;
    }

    private String id;                        // application id
    private double amount;                    // amount of money to borrow
    private int term;                         // the size of terms (months)
    private String companyName = "贪心科技";    // the name of current working company
    private String job = "计算机";             // the current job type
    private String city = "北京";              // the current working city
    private Phone companyPhone;               // the company phone
    private Phone parentPhone;                // parent phone
    private Phone colleaguePhone;             // colleague phone
    private Email email;                      // the email address
    private Person person;                    // user id who has applied
    private String create = "";                    // the creation time
    private ApplyStatus status;               // application status


    public Apply(String id, double amount, int term, String companyName, String job, String city,
                  Phone companyPhone, Phone parentPhone, Phone colleaguePhone, Email email,
                  Person person, String create, ApplyStatus status){
        this.id = id;
        this.amount = amount;
        this.term = term;
        this.companyName = companyName;
        this.job = job;
        this.city = city;
        this.companyPhone = companyPhone;
        this.parentPhone = parentPhone;
        this.colleaguePhone = colleaguePhone;
        this.email = email;
        this.person = person;
        this.create = create;
        this.status = status;
    }

    /**
     * Constructor, but generate some fields randomly
     */
    public Apply(){ // randomly generate some fields
        // generate amount
        this.amount = 10000 + 190000 * new Random().nextDouble();
        // generate term, minimum 6 months, maximum 24 months
        this.term = 6 + Math.abs(new Random().nextInt()) % 18;
        // set the status
        double rand = new Random().nextDouble();
        if (rand < 1.0/3){
            this.status = ApplyStatus.IN_PROGREESS;
        } else if(rand < 2.0/3){
            this.status = ApplyStatus.REPAID;
        } else{
            this.status = ApplyStatus.RETURNING;
        }

        // TODO: generate companyName, job, city, create
    }

    public void setId(String id){
        this.id = id;
    }

    public void setStatus(ApplyStatus status){
        this.status = status;
    }

    public void setEmail(Email email){
        this.email = email;
    }

    public void setPerson(Person person){
        this.person = person;
    }

    public Person getPerson(){
        return person;
    }

    public Email getEmail(){
        return email;
    }

    public void setParentPhone(Phone phone){
        this.parentPhone = phone;
    }

    public Phone getParentPhone(){
        return parentPhone;
    }

    public void setCompanyPhone(Phone phone){
        this.companyPhone = phone;
    }

    public Phone getCompanyPhone(){
        return companyPhone;
    }

    public void setColleaguePhone(Phone phone){
        this.colleaguePhone = phone;
    }

    public Phone getColleaguePhone(){
        return colleaguePhone;
    }

    public static String getHeader(){
        return "id,amount,term,company_name,job,city,company_name,parent_phone," +
                "colleague_phone,company_phone,email,applicant,create,status";
    }

    public String toString(){
        return id + "," + Double.toString(amount) + "," + Integer.toString(term) + "," + companyName
                + "," + job + "," + city + "," + companyName + "," + parentPhone.getNum() + ","
                + colleaguePhone.getNum() + companyPhone.getNum() + "," + email.getAddress()
                + "," + person.getId() + "," + create.toString() + "," + status.toString();
    }
}
