import java.sql.Timestamp;
import java.util.Date;

/**
 * This class encodes each entry of calling history
 *
 * @author wenzheli
 * @date Sep 19 2018
 */
public class Phone2Phone {
    private String from;        // calling from
    private String to;          // calling to
    private Date startTime;     // the start time of call happens
    private Date endTime;       // the end time of the call

    public Phone2Phone(String from, String to, Date startTime, Date endTime){
        this.from = from;
        this.to = to;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Constructor, randomly generate some fields.
     */
    public Phone2Phone(String from, String to){
        this.from = from;
        this.to = to;
        // randomly generate the starting time and end time.
        long rangebegin = Timestamp.valueOf("2018-03-08 00:57:00").getTime();
        long rangeend = Timestamp.valueOf("2018-09-18 00:58:00").getTime();
        long diff = rangeend - rangebegin + 1;
        long durMin = 60000 * 5;
        long durMax = 60000 * 60;

        long startStamp =  rangebegin + (long)(Math.random() * diff);
        long duration = durMin + (long)(Math.random() * (durMax - durMin));

        this.startTime = new Timestamp(startStamp);
        this.endTime = new Timestamp(startStamp + duration);
    }

    public static String getHeader(){
        return "from,to,start_time,end_time";
    }

    public String toString(){
        return from + "," + to + "," + startTime.toString() + "," + endTime.toString();
    }


}
