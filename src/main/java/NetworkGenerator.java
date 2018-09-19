import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The class contains all the procedures for network generation. All the corresponding nodes
 * are generated randomly.
 *
 * @author wenzheli
 */
public class NetworkGenerator {

    /** Store the list of entities in this knowledge graph */
    List<Apply> applyList;      // list of applications
    List<Phone> phoneList;      // list of phones
    List<Person> personList;    // list of persons
    List<Email> emailList;      // list of emails
    List<Phone2Phone> p2pList;  // list of <phone, phone> calling records
    BlackList blackList;

    /** Define the network parameters to tune*/
    public static final int NUM_PHONE = 50000;  // number of phones
    public static final int NUM_COMM = 1000;     // number of communities
    public static final int NUM_PERSON = 10000;  // number of persons

    public static final double IN_COMMUNITY_MAX_BLACK_RATIO = 0.2;
    public static final double IN_COMMUNITY_MIN_BLACK_RATIO = 0.01;

    public static final double PHONE_BLACK_LIST_RATIO = 0.001;
    public static final double PERSON_BLACK_LIST_RATIO = 0.01;
    public static final double EMAIL_BLACK_LIST_RATIO = 0.001;

    public static final double PROB_BLACK_PERSON = 0.3;
    public static final double PROB_BLACK_PHONE = 0.3;
    public static final double PROB_BLACK_OTHER = 0.1;


    public NetworkGenerator(){
        applyList = new ArrayList<Apply>();
        phoneList = new ArrayList<Phone>();
        personList = new ArrayList<Person>();
        emailList = new ArrayList<Email>();
        p2pList = new ArrayList<Phone2Phone>();
        blackList = new BlackList();
    }


    /**
     * This the main function of network generation.
     */
    public void run() throws IOException {
        // Section 1:  Generate the phone network.

        // (i) : generate the phone numbers
        generatePhones(NUM_PHONE);
        System.out.println("已生成" + NUM_PHONE + "电话号");

        // (ii): set the community and flag type for each phone number
        List<List<Phone>> comm2PhoneList = new ArrayList<List<Phone>>();
        for (int i = 0; i < NUM_COMM; i++){
            comm2PhoneList.add(new ArrayList<Phone>());
        }
        setComAndFlag(comm2PhoneList, NUM_COMM, IN_COMMUNITY_MIN_BLACK_RATIO,
                IN_COMMUNITY_MAX_BLACK_RATIO);
        System.out.println("已完成构建社区");


        // (iii): set the black list
        setBlackListForPhone(PHONE_BLACK_LIST_RATIO);
        System.out.println("已生成电话黑名单");


        // (iv): connect the phone to make a network
        setConnections();
        System.out.println("已完成构建电话网络，总共生成" + p2pList.size() + "链接");


        // Section 2: Generate list of person objects, and create relations
        //   between person and phone
        generatePersons(NUM_PERSON);
        System.out.println("已生成" + NUM_PERSON + "人");

        setBlackListForPersons(PERSON_BLACK_LIST_RATIO);
        System.out.println("已生成人物黑名单");

        // Section 3: Generate list of applications
        generateApplies();
        System.out.println("已完成生成进件，总共" + applyList.size() + "进件");


    }

    private void generateApplies(){
        Set<String> emailSet = new HashSet<String>();
        int appId = 100000;
        for (Person p : personList){
            // for each person, randomly generate list of applies.
            double[] prob = new double[]{0.35, 0.3, 0.2, 0.05, 0.04, 0.03, 0.02, 0.01};
            int applyCnt = 1 + MathUtils.getSample(prob);
            for (int i = 0; i < applyCnt; i++){
                // generate the list of application objects
                Apply apply = new Apply();
                apply.setPerson(p);
                // create new email and connect to application
                Email newEmail = new Email();
                while(true){
                    if (!emailSet.contains(newEmail.getAddress())){
                        if (new Random().nextDouble() < EMAIL_BLACK_LIST_RATIO){
                            newEmail.setFlagType(FlagType.BLACK);
                            blackList.addEmail(newEmail.getAddress());
                            apply.setEmail(newEmail);
                        }

                        apply.setEmail(newEmail);
                        emailSet.add(newEmail.getAddress());
                        emailList.add(newEmail);
                        break;
                    }
                    newEmail = new Email();
                }

                int N = phoneList.size();
                // randomly assign phones to parent, colleague and company
                apply.setColleaguePhone(phoneList.get(Math.abs(new Random().nextInt() % N)));
                apply.setCompanyPhone(phoneList.get(Math.abs(new Random().nextInt() % N)));
                apply.setParentPhone(phoneList.get(Math.abs(new Random().nextInt() % N)));

                // randomly assign the status of applications, but this is based on the
                // FlagType of phones and person object.
                double blackProb = 0;
                blackProb += apply.getColleaguePhone().getFlag() == FlagType.BLACK
                        ? PROB_BLACK_OTHER : 0;
                blackProb += apply.getCompanyPhone().getFlag() == FlagType.BLACK
                        ? PROB_BLACK_OTHER : 0;
                blackProb += apply.getParentPhone().getFlag() == FlagType.BLACK
                        ? PROB_BLACK_OTHER : 0;
                blackProb += apply.getPerson().getFlagType() == FlagType.BLACK
                        ? PROB_BLACK_PERSON : 0;
                blackProb += apply.getPerson().getPhone().getFlag() == FlagType.BLACK
                        ? PROB_BLACK_PHONE : 0;
                blackProb += apply.getEmail().getFlagType() == FlagType.BLACK
                        ? PROB_BLACK_OTHER : 0;

                if (new Random().nextDouble() < blackProb){
                    apply.setStatus(Apply.ApplyStatus.OVERDUE);
                }

                apply.setId(Integer.toString(appId));
                appId++;
                applyList.add(apply);

            }
        }
    }

    /**
     * Generate list of (unique) person objects
     *
     * @param count  the number of person objects to generate
     * @return       the list of person objects
     */
    private void generatePersons(int count) throws IOException {

        int N = phoneList.size();
        int startId = 20000001;
        for (int i = 0; i < count; i++){
            // set the user id
            Person p = new Person();
            p.setId(Integer.toString(startId + i));

            // connect to phones
            int randIdx = Math.abs(new Random().nextInt()) % N;
            p.setPhone(phoneList.get(randIdx));

            personList.add(p);
        }
    }

    /**
     * Generate list of (unique) phone numbers
     * @param count  the number of phone numbers to generate
     * @return   the list of numbers
     */
    private void generatePhones(int count){
        Set<Phone> pList = new HashSet<Phone>();

        while (count > 0){
            Phone phone = generate();
            if (!pList.contains(phone)){ // make sure it is unique
                pList.add(phone);
                phoneList.add(phone);
                count--;
            }else
                continue;
        }
    }

    /**
     * Generate one single phone number, each phone starts with
     * 13, 14, 15, 18 followed by 7 digits
     *
     * @return  one single phone number
     */
    private Phone generate(){
        // define the available prefixs
        String[] prefixs = new String[]{"13", "14", "15", "18"};
        String phoneNo = prefixs[Math.abs(new Random().nextInt())%4];
        for (int i = 0; i < 8; i++){
            phoneNo += Math.abs(new Random().nextInt())%10;
        }

        return new Phone(phoneNo);
    }

    /**
     * Create the phone network. This is done by generative process of network,
     * given the number of communities, community between connection probability,
     * between community connection probability, black ratio of nodes, connection
     * probability between black nodes & white nodes, etc.
     *
     * @param comm2PhoneList  the list of phones within each community
     * @param numCom          the number of communities
     * @param blackMinRatio   for each community, the minimum ratio of black nodes
     * @param blackMaxRatio   for each community, the maximum ratio of black nodes
     */
    public void setComAndFlag(List<List<Phone>> comm2PhoneList, int numCom,
                              double blackMinRatio, double blackMaxRatio){

        int MAX = 2000, MIN = 100;   // the maximum and minimum number of nodes in one community
        int total = 0;

        // randomly assign the community size
        int[] commSize = new int[numCom];
        for (int i = 0; i < commSize.length; i++){
            int size = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
            commSize[i] = size;
            total += size;
        }

        // compute the base multinomial distribution
        double[] probDist = new double[numCom];
        for (int i = 0; i < numCom; i++){
            probDist[i] = commSize[i]*1.0/total;
        }

        // loop each phone, and set the community
        for (Phone p : phoneList){
            // draw from multinomial distribution
            int k = MathUtils.getSample(probDist);
            p.setComm(k);
            comm2PhoneList.get(k).add(p);
        }

        // randomly select the 5% of communities and assign the portion of nodes within
        // that community to black flag
        int numSel = (int) (numCom * 0.05);   //  the number of selected communities
        for (int i = 0; i < numSel; i++){
            // for each selected community, assign black flags to nodes
            int selectedCom = (int)(Math.random() * numCom);
            List<Phone> tmp = comm2PhoneList.get(selectedCom);

            // draw the percentage of black nodes within this community
            double ratio = blackMinRatio + Math.random() * (blackMaxRatio - blackMinRatio);
            for (Phone p : tmp){
                if (Math.random() < ratio){  // assign the black flag to nodes
                    p.SetFlag(FlagType.BLACK);
                }
            }
        }
    }

    /**
     * Set the black list of phones.
     *
     * @param ratio  denotes the ratio of black phones in the list
     */
    private void setBlackListForPhone(double ratio){

        for (Phone p : phoneList){
            if (Math.random() < ratio){
                blackList.addPhone(p.getNum());
            }
        }
    }


    /**
     * Set the black list of persons
     * @param ratio  the ratio of black list
     */
    private void setBlackListForPersons(double ratio){
        for (Person p : personList){
            if (Math.random() < ratio){
                p.setFlagType(FlagType.BLACK);
                blackList.addPerson(p.getId());
            }
        }
    }


    /**
     * Make connections between phones. The generation process is governed by the community
     * connection probability and flag-type connection probability
     */
    public void setConnections(){
        double inCommProb = 0.8, diffCommProb = 0.2;
        double bbProb = 0.6, wwProb = 0.4, bwProb = 0.2;
        double sparsity = 0.1;

        int N = phoneList.size();
        for (int i = 0; i < N; i++){
            if (i % 1000 == 1)
            System.out.println("正在处理第" + i + "个电话，已生成电话网络：" + 100.0*i/NUM_PHONE + "%");
            for (int j = i+1; j < N; j++){
                // make connection based on generation process
                Phone p1 = phoneList.get(i);
                Phone p2 = phoneList.get(j);
                double prob = 1;
                if (p1.getcomm() == p2.getcomm()){
                    prob *= inCommProb;
                } else{
                    prob *= diffCommProb;
                }

                if (p1.getFlag() != p2.getFlag()){
                    prob *= bwProb;
                } else{
                    if (p1.getFlag() == FlagType.WHITE){
                        prob *= wwProb;
                    }else{
                        prob *= bbProb;
                    }
                }

                if (new Random().nextDouble()  < prob * sparsity){
                    // create Phone2Phone object, and add it to the list
                    Phone2Phone p2p = new Phone2Phone(p1.getNum(), p2.getNum());
                    p2pList.add(p2p);
                    //System.out.println(p2pList.size());
                }
            }
        }
    }

    /**
     * Write all the records into the files
     */
    public void out2file() throws IOException {
        // write the person information into the file
        FileWriter writerPerson = new FileWriter("output/person.txt");
        writerPerson.write(Person.getHeader());
        writerPerson.write("\n");
        for (Person p : personList){
            writerPerson.write(p.toString());
            writerPerson.write("\n");
        }
        writerPerson.close();
        System.out.println("申请人数据已写入 person.txt ");

        // write the email information into the file
        FileWriter writerEmail = new FileWriter("output/email.txt");
        writerEmail.write(Email.getHeader());
        writerEmail.write("\n");
        for (Email e : emailList){
            writerEmail.write(e.toString());
            writerEmail.write("\n");
        }
        writerEmail.close();
        System.out.println("邮箱数据已写入 email.txt ");


        // write the phone network into the file
        FileWriter writerPhone = new FileWriter("output/phone.txt");
        writerPhone.write(Phone2Phone.getHeader());
        writerPhone.write("\n");
        for (Phone2Phone p2p : p2pList){
            writerPhone.write(p2p.toString());
            writerPhone.write("\n");
        }
        writerPhone.close();
        System.out.println("通话记录数据已写入 phone.txt ");


        // write the application information into the file
        FileWriter writerApply = new FileWriter("output/apply.txt");
        writerApply.write(Apply.getHeader());
        writerApply.write("\n");
        for (Apply a : applyList){
            writerApply.write(a.toString());
            writerApply.write("\n");
        }
        writerApply.close();
        System.out.println("进件数据已写入 apply.txt ");


        // write the black list into the file
        FileWriter writerBlack = new FileWriter("output/black.txt");
        writerBlack.write("id,type");
        writerBlack.write("\n");

        for (String id : blackList.getPhoneIds()){
            writerBlack.write(id + ",PHONE");
            writerBlack.write("\n");
        }

        for (String id : blackList.getEmailIds()){
            writerBlack.write(id + ",EMAIL");
            writerBlack.write("\n");
        }

        for (String id : blackList.getPersonIds()){
            writerBlack.write(id + ",PERSON");
            writerBlack.write("\n");
        }

        writerBlack.close();
        System.out.println("黑名单数据已写入 black.txt ");


    }
}
