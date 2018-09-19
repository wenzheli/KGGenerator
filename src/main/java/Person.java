import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * The class defines person entity
 *
 * @author wenzheli
 * @date Sep 19 2018
 */
public class Person {

    public enum SexType{
        MALE, FEMALE
    }

    private String id;
    private String name;
    private SexType sex;
    private Phone phone;
    private FlagType flagType = FlagType.WHITE;

    public Person(String id, String name, SexType sex, Phone phone){
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
    }

    /**
     * Constructor, randomly generate some fields
     */
    public Person() throws IOException {
        // randomly assign the name
        List<String> names = ResourceReader.getSampleNames();
        int rand_idx = Math.abs(new Random().nextInt())%names.size();
        this.name = names.get(rand_idx);

        // randomly assign the sex type
        if (Math.abs(new Random().nextInt())%2 == 0){
            this.sex = SexType.MALE;
        } else{
            this.sex = SexType.FEMALE;
        }
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public SexType getSex(){
        return sex;
    }

    public Phone getPhone(){
        return phone;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setPhone(Phone phone){
        this.phone = phone;
    }

    public FlagType getFlagType(){
        return flagType;
    }

    public void setFlagType(FlagType flagType){
        this.flagType = flagType;
    }

    public static String getHeader(){
        return "id,name,sex,phone";
    }

    public String toString(){
        return id + "," + name + ","
                + sex.toString() + ","
                + phone.getNum();
    }
}
