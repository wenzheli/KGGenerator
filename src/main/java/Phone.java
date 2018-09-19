/**
 * Phone entity and its fields
 *
 * @author wenzheli
 * @date Sep 19 2018
 */
public class Phone {
    private String number; // phone number
    private FlagType flag = FlagType.WHITE;  // whether the number is marked as black or white
    private int comm = 1;  // the index of communities

    public Phone(String number){
        this.number = number;
    }

    public void SetFlag(FlagType flag){
        this.flag = flag;
    }

    public FlagType getFlag(){
        return flag;
    }

    public String getNum(){
        return number;
    }

    public void setComm(int comm){
        this.comm = comm;
    }

    public int getcomm(){
        return comm;
    }
}

