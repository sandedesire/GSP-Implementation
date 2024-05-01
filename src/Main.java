import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {
    public static  List<UserData> database = new ArrayList<>();
    public static  List<UserBasket> userBasketDatabase = new ArrayList<>();
    public static  Set<String> uniqueIDs = new HashSet<>();
    public static  Set<String> uniqueITems = new HashSet<>();
    public static  Set<String> uniqueTimestamp = new HashSet<>();
    public static  List<SubSequence> sequences = new ArrayList<>();
    public static  List<List<SubSequence>> logs = new ArrayList<>();
    public static  float minSup = (float)0.2;
    public static  List<List<String>> winners = new ArrayList<>();


    public static void main(String[] args) {

        //Parsing the CSV file to get data
        //File resourceFile = new File("/home/maitre/Documents/GSPTestData.csv");
        //File resourceFile = new File("/home/maitre/Downloads/archive/OnlineRetail.csv");





        File resourceFile = new File("/home/maitre/Downloads/" +
                "ClassicAssociationDiscoveryGroundTruthData.csv");


        System.out.println("GSP Implementation");

        String line = "";
        String splitBy = ",";
        try
        {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(resourceFile));
            //Printing all the  data for verification
            //Getting the header
            String[] header = br.readLine().split(splitBy);
            System.out.println("Header is: " + Arrays.asList(header));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] entry = line.split(splitBy);
                UserData userData = new UserData(entry[0],entry[1],entry[2]);
                //UserData userData = new UserData(entry[0],entry[2],entry[4]);

                database.add(userData);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        //Testing our Database

        for (UserData x : database){
            System.out.println(x.getUserID());
            System.out.println(x.getItemDescription());
            System.out.println(x.getTimeStamp());
        }

        //Data Cleaning. TO BE IMPLEMENTED




        //Collecting UniqueIDs and UniqueItems
        setUniqueIDsAndItemsAndTimestamp(database);
        System.out.println("ALL UNIQUE IDS AND ITEMS");
        System.out.println(uniqueIDs);
        System.out.println(uniqueITems);

        //Creating our List of UserBasket making use of UniqueIds
        for(String x : uniqueIDs){
            userBasketDatabase.add(new UserBasket(x));
        }

        //Viewing the size of ALL our users
        System.out.println("SIZE OF USERBASKET LIST");
        System.out.println(userBasketDatabase.size());

        //Setting all items each user ever bought and All Timestamp
        setAllItemsEachUserEverboughtAndAllTimeStamp(database,
                userBasketDatabase);
        System.out.println("VERIFICATION AFTER UNIQUENESS ITEMS AND TIMESTAMP");
        System.out.println(userBasketDatabase.get(2).getAllItemsEverBought());
        System.out.println(userBasketDatabase.get(2).getUsersTimeStamp());

        /*
        for(UserBasket ub: userBasketDatabase) {
            setAllUsersMap(ub.allItemsEverBought,ub.usersTimeStamp,database);
        }
        */

        setAllUsersMap(database,userBasketDatabase);
        System.out.println("Checking all users item to timestamp and timestamp to item");
        for(UserBasket x : userBasketDatabase){
            System.out.println(x.getItemToAllTimeStamp());
            System.out.println(x.getTimeStampToAllItems());

        }

        //Joining and Pruning Phase
        //Making the subsequences
        for(String x : uniqueITems){
            List<String> xitems = new ArrayList<>();
            xitems.add(x);
            SubSequence subsequence = new SubSequence(xitems);
            sequences.add(subsequence);
        }
        logs.add(sequences);

        //Printing our initial List of Sequneces
        System.out.println("Printing our initial List of Sequneces");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }

        /*
        //Continue Coding From Here
        //Write Functions to 1. Calculate Support 2. Prune 3. Join
        //In the above order

        calculateSupport(userBasketDatabase,logs.getLast());
        //Testing the support function
        System.out.println("After Calculating the support");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }

        //Testing the Pruning Function
        prune(logs.getLast(),(float)0.3);
        System.out.println("After Pruning. ");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }

        /*
        //Testing  the Join Function
        List<SubSequence> joined = initialJoin(logs.getLast());
        System.out.println("After First Join");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }


        //Refine the Join and Support functions
        //Check the bookmark java list combinations
        combineTwo(logs.getLast());
        System.out.println("After CombineTwo. ");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }
        //Calculating Support
        calculateSupport(userBasketDatabase,logs.getLast());
        System.out.println("After Calculating Support 2. ");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }

        combineThree(logs.getLast());
        System.out.println("After CombineThree. ");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }
        //Calculating Support
        calculateSupport(userBasketDatabase,logs.getLast());
        System.out.println("After Calculating Support 3. ");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined);
            System.out.println(x.support);
        }



         */

        System.out.println("------------------GSP----------------------------------------");

        round1(logs.getLast());
        round2(logs.getLast());
        round3(logs.getLast());
        round4(logs.getLast());
        round5(logs.getLast());
        getWinnerSubSequenceForm(logs);
        System.out.println(winners);












    }

    public static void setUniqueIDsAndItemsAndTimestamp(List<UserData> database){
        for(UserData x : database){
            uniqueIDs.add(x.getUserID());
            uniqueITems.add(x.getItemDescription());
            uniqueTimestamp.add(x.getTimeStamp());
        }
    }

    public static void setAllItemsEachUserEverboughtAndAllTimeStamp(List<UserData> database,
                                              List<UserBasket> userBasketDatabase ){
        for(UserBasket ub : userBasketDatabase){
            for(UserData ud : database){
                if(ub.getUserID().equals(ud.getUserID().trim())){
                    ub.getAllItemsEverBought().add(ud.getItemDescription());
                    ub.getUsersTimeStamp().add(ud.getTimeStamp());
                }
            }

        }


    }



    public static void setAllUsersMap(List<UserData> database,
                                      List<UserBasket> userBasketDatabase){

        for(UserBasket ub : userBasketDatabase){


            for(String item : ub.allItemsEverBought){
                //System.out.println("User's current item is:"+ item);
                Map<String,List<String>> itemToTimes = new HashMap<>();
                List<String> times = new ArrayList<>();
                //String currentItem = null;
                //currentItem = item;
                for(UserData ud : database){
                    if(ud.getItemDescription().equals(item) && ub.getUserID().equals(ud.getUserID())){
                        times.add(ud.getTimeStamp());

                    }


                }
                itemToTimes.put(item,times);
                ub.itemToAllTimeStamp.add(itemToTimes);

            }


            for(String time : ub.usersTimeStamp){
                Map<String,List<String>> timeToItems = new HashMap<>();
                List<String> items = new ArrayList<>();
                //String currentItem = null;
                //currentItem = item;
                for(UserData ud : database){
                    if(ud.getTimeStamp().equals(time) && ub.getUserID().equals(ud.getUserID())){
                        items.add(ud.getItemDescription());

                    }


                }
                timeToItems.put(time,items);
                ub.timeStampToAllItems.add(timeToItems);

            }



        }


    }


    public static List<SubSequence> initialJoin(List<SubSequence> sequences){
        List<SubSequence> firstJoin = new ArrayList<>();
        for(SubSequence x: sequences){
            String lastY = "";
            List<String> xitems = x.getItemsJoined();
            for(SubSequence y : sequences){
                boolean allItemsSame = false;
                if(x.itemsJoined.size() == y.itemsJoined.size()
                && !(x.itemsJoined.getLast().equals(y.itemsJoined.getLast()))){
                    for(int i = 0; i < x.itemsJoined.size() - 1; i++){
                        if( !(x.itemsJoined.get(i).equals(y.itemsJoined.get(i)))){
                            break;
                        }

                    }
                    lastY = y.itemsJoined.getLast();


                }
            }
            if(!lastY.equals("")){
                xitems.add(lastY);

            }
            firstJoin.add(new SubSequence(xitems));
        }
        return  firstJoin;
    }

    public static void combineTwo(List<SubSequence> sequences){
        List<SubSequence> answer = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for(SubSequence x : sequences){
            for(String y : x.itemsJoined){
                set.add(y);

            }
        }

        List<String> elements = new ArrayList<>(set);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                //System.out.println("Elements Combined are :"+ elements.get(i)+elements.get(j));
                List<String> elem = new ArrayList<>();
                elem.add(elements.get(i));
                elem.add(elements.get(j));
                answer.add(new SubSequence(elem));

            }
        }
        logs.add(answer);

    }

    public static void combineThree(List<SubSequence> sequences){
        List<SubSequence> answer = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for(SubSequence x : sequences){
            for(String y : x.itemsJoined){
                set.add(y);

            }
        }

        List<String> elements = new ArrayList<>(set);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                for(int k = j+1 ; k<elements.size(); k++){
                    List<String> elem = new ArrayList<>();
                    elem.add(elements.get(i));
                    elem.add(elements.get(j));
                    elem.add(elements.get(k));
                    answer.add(new SubSequence(elem));

                }


            }
        }
        logs.add(answer);

    }
    public static void combineFour(List<SubSequence> sequences){
        List<SubSequence> answer = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for(SubSequence x : sequences){
            for(String y : x.itemsJoined){
                set.add(y);

            }
        }

        List<String> elements = new ArrayList<>(set);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                for(int k = j+1 ; k<elements.size(); k++){
                    for(int l = k+1; l<elements.size();l++){
                        List<String> elem = new ArrayList<>();
                        elem.add(elements.get(i));
                        elem.add(elements.get(j));
                        elem.add(elements.get(k));
                        elem.add(elements.get(l));
                        answer.add(new SubSequence(elem));
                    }


                }


            }
        }
        logs.add(answer);

    }
    public static void combineFive(List<SubSequence> sequences){
        List<SubSequence> answer = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for(SubSequence x : sequences){
            for(String y : x.itemsJoined){
                set.add(y);

            }
        }

        List<String> elements = new ArrayList<>(set);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                for(int k = j+1 ; k<elements.size(); k++){
                    for(int l = k+1; l<elements.size();l++){
                        for(int n = l+1; n<elements.size();n++){
                            List<String> elem = new ArrayList<>();
                            elem.add(elements.get(i));
                            elem.add(elements.get(j));
                            elem.add(elements.get(k));
                            elem.add(elements.get(l));
                            elem.add(elements.get(n));
                            answer.add(new SubSequence(elem));
                        }

                    }


                }


            }
        }
        logs.add(answer);

    }



    public static void calculateSupport(List<UserBasket> userBasketDatabase,
                                        List<SubSequence> sequence){
        for(SubSequence subSequence : sequence){
            int count = 0;
            for(UserBasket ub : userBasketDatabase){
                if(ub.allItemsEverBought.containsAll(subSequence.itemsJoined)){
                    count = count + 1;
                }
            }
            subSequence.setSupport((float)count/userBasketDatabase.size()) ;


        }
    }

    public static void prune(List<SubSequence> sequences, float minSup){
        List<SubSequence> toDelete = new ArrayList<>();
        for(SubSequence x : sequences){
            x.pruned = true;
            if(x.support < minSup){
                toDelete.add(x);

            }
        }
        for(SubSequence y : toDelete){
            System.out.println("Element Deleted is: "+ y.itemsJoined);
            sequences.remove(y);
        }
        logs.add(sequences);
    }



    public static void round1(List<SubSequence> seq){
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 1");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
        }
        System.out.println("----------------------------------------------------------");



    }

    public static void round2(List<SubSequence> seq){
        combineTwo(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 2");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
        }
        System.out.println("----------------------------------------------------------");


    }

    public static void round3(List<SubSequence> seq){
        combineThree(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 3");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
        }
        System.out.println("----------------------------------------------------------");

    }

    public static void round4(List<SubSequence> seq){
        combineFour(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 4");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
        }
        System.out.println("----------------------------------------------------------");

    }

    public static void round5(List<SubSequence> seq){
        combineFive(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 5");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
        }
        System.out.println("----------------------------------------------------------");

    }

    public static void getWinnerSubSequenceForm(List<List<SubSequence>> l){
        for(int i = l.size() - 1;i<l.size();i--){
            if(l.get(i).size() != 0){
                System.out.println("The Winning Subsequence(s) is/are:");
                for(SubSequence s : l.get(i)){
                    System.out.println("Sequence: "+ s.itemsJoined+
                            " has support: "+s.support);
                    winners.add(s.itemsJoined);
                }
                return;


            }
        }

    }
    

}