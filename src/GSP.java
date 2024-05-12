package GSPImplementation;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Math.abs;
import GSPImplementation.UserBasket;
import GSPImplementation.UserData;
import GSPImplementation.Confidence;
import GSPImplementation.SubSequence;







public class GSP {

    public  File resultFile;
    public  File resourceFile;


    public  String pathToResult;
    public  StringBuilder stringBuilder = new StringBuilder();
    public  DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    public   int UserIDColumn ;
    public   int ItemColumn;
    public   int TimeStampColumn;

    public   List<UserData> database = new ArrayList<>();
    public   List<UserBasket> userBasketDatabase = new ArrayList<>();
    public   Set<String> uniqueIDs = new HashSet<>();
    public   Set<String> uniqueITems = new HashSet<>();
    public   Set<String> uniqueTimestamp = new HashSet<>();
    public   List<SubSequence> sequences = new ArrayList<>();
    public   List<List<SubSequence>> logs = new ArrayList<>();
    //minSup >= 0.15 and minConf >=0.3 in GUI code
    public   float minSup;
    public   float minConf;
    public   int minTimeDiff;

    public   List<List<String>> winners = new ArrayList<>();
    public   Set<String> confidence = new HashSet<>();
    public   List<List<String>> confidenceCombinations = new ArrayList<>();
    public   List<Confidence> confidenceObjects = new ArrayList<>();

    public   Date start = new Date();
    public   Date end;




    public  GSP(File resourceFile,int UserIDColumn,int ItemColumn,int TimeStampColumn,
                float minSup,float minConf,int minTimeDiff ) {

        this.UserIDColumn = UserIDColumn;
        this.ItemColumn = ItemColumn;
        this.TimeStampColumn = TimeStampColumn;
        this.minSup = minSup;
        this.minConf = minConf;
        this.minTimeDiff = minTimeDiff;
        createResultFile();


        //Parsing the CSV file to get data
        //File resourceFile = new File("/home/maitre/Documents/GSPTestData.csv");
        //File resourceFile = new File("/home/maitre/Downloads/archive/OnlineRetail.csv");









        /*
        File resourceFile = new File("/home/maitre/Downloads/" +
                "ClassicAssociationDiscoveryGroundTruthData.csv");

         */








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
            stringBuilder.append("Header is: " + Arrays.asList(header)+"\n");
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] entry = line.split(splitBy);




                UserData userData = new UserData(entry[UserIDColumn],
                        entry[ItemColumn],
                        entry[TimeStampColumn]);









                // UserIDColumn = 6, ItemColumn = 2, TimeStampColumn = 4
                //UserData userData = new UserData(entry[6],entry[2],entry[4]);

                database.add(userData);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        //Testing our Database

        /*
        for (UserData x : database){
            System.out.println(x.getUserID());
            System.out.println(x.getItemDescription());
            System.out.println(x.getTimeStamp());
        }

         */

        //Running Parameters
        System.out.println("-----------------------------------");
        System.out.println("Minimum Confidence:"+minConf);
        System.out.println("-----------------------------------");
        System.out.println("Minimum Support:"+minSup);


        System.out.println("-----------------------------------");

        System.out.println("Minimum Time Difference:"+minTimeDiff);
        System.out.println("That is, every rule generated with a time diffrerence ");
        System.out.println("greater than "+minTimeDiff+" days will be deleted");
        System.out.println("-----------------------------------");


        stringBuilder.append("-----------------------------------\n");
        stringBuilder.append("Minimum Confidence:"+minConf+"\n");
        stringBuilder.append("-----------------------------------\n");
        stringBuilder.append("Minimum Support:"+minSup+"\n");
        stringBuilder.append("-----------------------------------\n");

        stringBuilder.append("Minimum Time Difference:"+minTimeDiff+"\n");
        stringBuilder.append("That is, every rule generated with a time diffrerence ");
        stringBuilder.append("greater than "+minTimeDiff+" days will be deleted\n");
        stringBuilder.append("-----------------------------------\n");





        //Data Cleaning. TO BE IMPLEMENTED
        System.out.println("Number of rows Before data cleaning:"+database.size());
        stringBuilder.append("Number of rows Before data cleaning:"+database.size()+"\n");
        dataCleaning(database);
        System.out.println("Number of rows After data cleaning:"+database.size());
        stringBuilder.append("Number of rows After data cleaning:"+database.size()+"\n");




        //Collecting UniqueIDs and UniqueItems
        setUniqueIDsAndItemsAndTimestamp(database);
        //System.out.println("ALL UNIQUE IDS AND ITEMS");
        //System.out.println(uniqueIDs);
        //System.out.println(uniqueITems);

        //Creating our List of UserBasket making use of UniqueIds
        for(String x : uniqueIDs){
            userBasketDatabase.add(new UserBasket(x));
        }

        //Viewing the size of ALL our users
        System.out.println("SIZE OF USERBASKET LIST:" +userBasketDatabase.size());
        stringBuilder.append("SIZE OF USERBASKET LIST:" +userBasketDatabase.size()+"\n");


        //Setting all items each user ever bought and All Timestamp
        setAllItemsEachUserEverboughtAndAllTimeStamp(database,
                userBasketDatabase);
        System.out.println("VERIFICATION AFTER UNIQUENESS ITEMS AND TIMESTAMP");
        System.out.println(userBasketDatabase.get(2).getAllItemsEverBought());
        System.out.println(userBasketDatabase.get(2).getUsersTimeStamp());

        setAllUsersMap(database,userBasketDatabase);





        System.out.println("Checking all users item to timestamp and timestamp to item");
        stringBuilder.append("Checking all users item to timestamp and timestamp to item\n");
        for(UserBasket x : userBasketDatabase){
            System.out.println("User with ID:" + x.getUserID());
            System.out.println(x.getItemToAllTimeStamp());
            System.out.println(x.getTimeStampToAllItems());
            System.out.println("----------------------------------------------------------");

            //stringBuilder.append("User with ID:" + x.getUserID()+"\n");
            //stringBuilder.append(x.getItemToAllTimeStamp()+"\n");
            //stringBuilder.append(x.getTimeStampToAllItems()+"\n");
            //stringBuilder.append("----------------------------------------------------------\n");


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
        /*
        System.out.println("Printing our initial List of Sequences");
        stringBuilder.append("Printing our initial List of Sequences\n");
        for(SubSequence x : logs.getLast()){
            System.out.println(x.itemsJoined+" has Support"+x.support);
            System.out.println("-------------------------------------");

            stringBuilder.append(x.itemsJoined+" has Support"+x.support +"\n");
            stringBuilder.append("-------------------------------------\n");
        }

         */





        System.out.println("------------------GSP----------------------------------------");

        round1(logs.getLast());
        round2(logs.getLast());
        round3(logs.getLast());
        round4(logs.getLast());
        round5(logs.getLast());

        getWinnerSubSequenceForm(logs);
        //Will Finish it's implementation in future versions
        //processWinnerSubsequences(winners);

        //Calculate Confidence

        calculateConfidence(winners);
        System.out.println(confidenceCombinations);
        stringBuilder.append(confidenceCombinations+"\n");

        //DETERMINING THE MINIMUM TIMEDIFFERENCE AMONG THE RULES
        isolateRespetiveUsers();
        getPrefixTimeStamp();
        getPostfixTimeStamp();







        saveMinTimeDifference();
        pruneTimeDiff(confidenceObjects);

        //Printing the Confidence Objects
        for(Confidence c : confidenceObjects){

            System.out.println("Prefix:"+ c.prefix);
            System.out.println("Postfix:"+ c.postfix);
            System.out.println("PrefAndPos:"+ c.prefAndPos);

            System.out.println("Buying:"+c.postfix+" -------> Also Buying:"+c.prefix+" with Confidence:"+
                    c.confidenceValue);



            System.out.print("All users who bought PrefAndPos:");
            stringBuilder.append("All users who bought PrefAndPos:");
            for(UserBasket ub : c.prefAndPosUsers){
                System.out.print(ub.getUserID()+",");
                stringBuilder.append(ub.getUserID()+",");

            }
            System.out.println("\nPrefix TimeFrame:"+c.prefTimeFrame);
            System.out.println("Postfix TimeFrame:"+c.posTimeFrame);
            System.out.println("Minimum Time Diffrerence is:"+c.minTimeDifference+" days!");



            System.out.println("\n------------------------------------------------");

            stringBuilder.append("\nPrefix:"+ c.prefix+"\n");
            stringBuilder.append("Postfix:"+ c.postfix+"\n");
            stringBuilder.append("PrefAndPos:"+ c.prefAndPos+"\n");
            stringBuilder.append("Buying:"+c.postfix+" -------> Also Buying:"+c.prefix+" with Confidence:"+
                    c.confidenceValue+"\n");
            stringBuilder.append("\nPrefix TimeFrame:"+c.prefTimeFrame+"\n");
            stringBuilder.append("Postfix TimeFrame:"+c.posTimeFrame+"\n");
            stringBuilder.append("Minimum Time Diffrerence is:"+c.minTimeDifference+" days!\n");

            stringBuilder.append("------------------------------------------------\n");


        }













        System.out.println("Running Time Statistics");
        stringBuilder.append("Running Time Statistics\n");

        System.out.println("Start:"+start);
        stringBuilder.append("Start:"+start+"\n");

        end = new Date();

        System.out.println("End:"+end);
        stringBuilder.append("End:"+end+"\n");
        long time_difference = end.getTime() - start.getTime();
        System.out.println("It took:"+time_difference+" Milliseconds");
        stringBuilder.append("It took:"+time_difference+" Milliseconds\n");

        saveResultsToFile(stringBuilder);


    }


    public  void createResultFile(){
        resultFile = new File("GSPResults"+start.toString()+".txt");


        pathToResult = resultFile.getAbsolutePath();
        System.out.println("The Result File can be accessed at:"+pathToResult);
        try {
            FileWriter myWriter = new FileWriter(resultFile);
            myWriter.write("EFFICIENT GSP IMPLEMENTATION ON LARGE DATASET BY KOM SAMUEL @LUXEMBOURG UNIVERSITY\n\n");
            myWriter.write("-----------------------------------------------------------------------------------\n");

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred creating the FILE.");
            e.printStackTrace();
        }
    }

    public  void saveResultsToFile(StringBuilder stringBuilder){
        try {
            FileWriter myWriter = new FileWriter(resultFile,true);
            myWriter.write(stringBuilder.toString());

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred Saving the Result.");
            e.printStackTrace();
        }

    }

    public  void setUniqueIDsAndItemsAndTimestamp(List<UserData> database){
        for(UserData x : database){
            uniqueIDs.add(x.getUserID());
            uniqueITems.add(x.getItemDescription());
            uniqueTimestamp.add(x.getTimeStamp());
        }
    }

    public  void setAllItemsEachUserEverboughtAndAllTimeStamp(List<UserData> database,
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



    public  void setAllUsersMap(List<UserData> database,
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


            /*
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
            */



        }


    }




    public  void combineTwo(List<SubSequence> sequences){
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

    public  void combineThree(List<SubSequence> sequences){
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
    public  void combineFour(List<SubSequence> sequences){
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
    public  void combineFive(List<SubSequence> sequences){
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



    public  void calculateSupport(List<UserBasket> userBasketDatabase,
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

    public  void prune(List<SubSequence> sequences, float minSup){
        List<SubSequence> toDelete = new ArrayList<>();
        for(SubSequence x : sequences){
            x.pruned = true;
            if(x.support < minSup){
                toDelete.add(x);

            }
        }
        for(SubSequence y : toDelete){
            //System.out.println("Element Deleted is: "+ y.itemsJoined);
            //stringBuilder.append("Element Deleted is: "+ y.itemsJoined+"\n");
            sequences.remove(y);
        }
        logs.add(sequences);
    }



    public  void round1(List<SubSequence> seq){
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 1");
        stringBuilder.append("General Statistics After Round 1\n");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
            stringBuilder.append("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support+"\n");
        }
        System.out.println("----------------------------------------------------------");
        stringBuilder.append("----------------------------------------------------------\n");



    }

    public  void round2(List<SubSequence> seq){
        combineTwo(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 2");
        stringBuilder.append("General Statistics After Round 2\n");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
            stringBuilder.append("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support+"\n");
        }
        System.out.println("----------------------------------------------------------");
        stringBuilder.append("----------------------------------------------------------\n");


    }

    public  void round3(List<SubSequence> seq){
        combineThree(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 3");
        stringBuilder.append("General Statistics After Round 3\n");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
            stringBuilder.append("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support+"\n");
        }
        System.out.println("----------------------------------------------------------");
        stringBuilder.append("----------------------------------------------------------\n");

    }

    public  void round4(List<SubSequence> seq){
        combineFour(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 4");
        stringBuilder.append("General Statistics After Round 4\n");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
            stringBuilder.append("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support+"\n");
        }
        System.out.println("----------------------------------------------------------");
        stringBuilder.append("----------------------------------------------------------\n");

    }

    public  void round5(List<SubSequence> seq){
        combineFive(logs.getLast());
        calculateSupport(userBasketDatabase,logs.getLast());
        prune(logs.getLast(),(float)minSup);
        System.out.println("General Statistics After Round 5");
        stringBuilder.append("General Statistics After Round 5\n");
        for(SubSequence s : logs.getLast()){
            System.out.println("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support);
            stringBuilder.append("Sub Sequence of form:"+ s.itemsJoined +" " +
                    "has Support: "+ s.support+"\n");
        }
        System.out.println("----------------------------------------------------------");
        stringBuilder.append("----------------------------------------------------------\n");

    }

    public  void getWinnerSubSequenceForm(List<List<SubSequence>> l){


        for(int i = l.size() - 1;i<l.size();i--){
            if(i==-1){
                System.out.println("Size of Winning Subsequence is 0");
                stringBuilder.append("Size of Winning Subsequence is 0\n");
                return;
            }
            if(!(l.get(i).isEmpty())){
                System.out.println("The Winning Subsequence(s) is/are:");
                stringBuilder.append("The Winning Subsequence(s) is/are:\n");
                for(SubSequence s : l.get(i)){
                    System.out.println("Sequence: "+ s.itemsJoined+
                            " has support: "+s.support);
                    stringBuilder.append("Sequence: "+ s.itemsJoined+
                            " has support: "+s.support+"\n");
                    winners.add(s.itemsJoined);
                }
                return;


            }
        }

    }






    public  void calculateConfidence(List<List<String>> winnerItems){
        for(List<String> list : winnerItems){
            for(String x : list){
                confidence.add(x);
            }
        }
        System.out.println("We are Calculating Confidence using the items:"+ confidence);
        stringBuilder.append("We are Calculating Confidence using the items:"+
                confidence+"\n");
        //Adding all unique elements to confidenceCombination
        for(String x : confidence){
            List<String> elem = new ArrayList<>();
            elem.add(x);
            confidenceCombinations.add(elem);
        }

        supTwo(confidence);
        supThree(confidence);
        supFour(confidence);
        supFive(confidence);

        System.out.println("Creating the Confidence Objects...");
        stringBuilder.append("Creating the Confidence Objects...\n");
        for(List<String> prefix : confidenceCombinations){
            for(List<String> postfix : confidenceCombinations){
                Confidence confidence1 = new Confidence(prefix,postfix);
                confidenceObjects.add(confidence1);
            }
        }
        System.out.println("Combining Prefix and Postfix in a set");
        stringBuilder.append("Combining Prefix and Postfix in a set\n");
        for(Confidence c:confidenceObjects){
            Set<String> s = new HashSet<>();
            for(String e1:c.prefix){
                s.add(e1);
            }

            for(String e2:c.postfix){
                s.add(e2);
            }

            List<String> s1 = new ArrayList<>(s);
            c.prefAndPos = s1;

        }

        System.out.println("Calculating Confidence");
        System.out.println("------------------------------------------------");
        stringBuilder.append("------------------------------------------------\n");
        stringBuilder.append("Calculating Confidence\n");
        for(Confidence conf: confidenceObjects){
            conf.confidenceValue = (float)(subConditionalProbability(conf.prefAndPos) / subConditionalProbability(conf.postfix));
        }
        pruneConf(confidenceObjects);
        System.out.println("There are:"+confidenceObjects.size()+" rules generated.");
        stringBuilder.append("There are:"+confidenceObjects.size()+" rules generated.\n");





    }

    public  void pruneConf(List<Confidence> sequences){
        List<Confidence> toDelete = new ArrayList<>();
        for(Confidence x : sequences){

            if(x.confidenceValue < minConf || x.postfix.equals(x.prefix)
                    || x.postfix.containsAll(x.prefix)  || x.prefix.containsAll(x.postfix)){
                toDelete.add(x);

            }
        }
        for(Confidence y : toDelete){
            //System.out.println("Element Deleted is: "+ y.itemsJoined);
            sequences.remove(y);
        }

    }

    public  float subConditionalProbability(List<String> subElement){
        int count = 0;
        float result;
        for(UserBasket ub : userBasketDatabase){
            if(ub.allItemsEverBought.containsAll(subElement)){
                count = count + 1;
            }

        }
        result = (float)count/userBasketDatabase.size();

        return result;
    }

    public   void supTwo(Set<String> element){
        List<String> elements = new ArrayList<>(element);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                //System.out.println("Elements are :"+ elements.get(i)+elements.get(j));
                List<String> elem = new ArrayList<>();
                elem.add(elements.get(i));
                elem.add(elements.get(j));

                confidenceCombinations.add(elem);


            }
        }
    }

    public   void supThree(Set<String> element){
        List<String> elements = new ArrayList<>(element);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                for(int k = j+1; k<elements.size();k++){
                    //System.out.println("Elements are :"+ elements.get(i)+elements.get(j));
                    List<String> elem = new ArrayList<>();
                    elem.add(elements.get(i));
                    elem.add(elements.get(j));
                    elem.add(elements.get(k));

                    confidenceCombinations.add(elem);
                }



            }
        }
    }

    public   void supFour(Set<String> element){
        List<String> elements = new ArrayList<>(element);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                for(int k = j+1; k<elements.size();k++){
                    for(int l = k+1; l<elements.size();l++){
                        //System.out.println("Elements are :"+ elements.get(i)+elements.get(j));
                        List<String> elem = new ArrayList<>();
                        elem.add(elements.get(i));
                        elem.add(elements.get(j));
                        elem.add(elements.get(k));
                        elem.add(elements.get(l));

                        confidenceCombinations.add(elem);
                    }

                }



            }
        }
    }

    public   void supFive(Set<String> element){
        List<String> elements = new ArrayList<>(element);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                for(int k = j+1; k<elements.size();k++){
                    for(int l = k+1; l<elements.size();l++){
                        for(int n = l+1;n<elements.size();n++){
                            //System.out.println("Elements are :"+ elements.get(i)+elements.get(j));
                            List<String> elem = new ArrayList<>();
                            elem.add(elements.get(i));
                            elem.add(elements.get(j));
                            elem.add(elements.get(k));
                            elem.add(elements.get(l));
                            elem.add(elements.get(n));



                            confidenceCombinations.add(elem);
                        }

                    }

                }



            }
        }
    }

    public  void dataCleaning(List<UserData> database){
        //Verifying the empty objects
        int countNull =0;
        List<UserData> toDelete = new ArrayList<>();
        List<UserData> toDelete2 = new ArrayList<>();

        for(UserData ud : database){
            if(ud.getUserID().isEmpty() || ud.getTimeStamp().isEmpty() ||
                    ud.getItemDescription().isEmpty()){
                countNull= countNull+1;
                toDelete.add(ud);
            }
        }
        for(UserData td: toDelete){
            database.remove(td);
        }



        for(UserData user : database){
            try {
                Date date = df.parse(user.getTimeStamp());

                /*
                System.out.println(date);
                LocalDateTime local_date_time =
                        date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                System.out.println(local_date_time);

                 */


            }
            catch (Exception e){
                toDelete2.add(user);
            }
        }

        for(UserData td2: toDelete2){
            database.remove(td2);
        }
        int sum = toDelete2.size() + toDelete.size();

        System.out.println("This amount of user Data was deleted:"+ sum);
        stringBuilder.append("This amount of rows user Data was deleted:"+ sum+"\n");




    }

    public  void isolateRespetiveUsers(){
        if(!confidenceObjects.isEmpty()){
            for(Confidence c : confidenceObjects){
                for(UserBasket ub : userBasketDatabase){
                    if(ub.allItemsEverBought.containsAll(c.prefAndPos)){
                        c.prefAndPosUsers.add(ub);
                    }
                }
            }
        }
    }

    public  void getPrefixTimeStamp(){
        for(Confidence c : confidenceObjects){
            for(String s : c.prefix){
                for(UserBasket ub : c.prefAndPosUsers){
                    for (Map<String, List<String>> map : ub.itemToAllTimeStamp) {
                        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                            String key = entry.getKey();
                            List<String> value = entry.getValue();

                            if(key.equals(s)){
                                c.prefTimeFrame.addAll(value);
                            }
                        }
                    }

                }
            }
        }
    }

    public  void getPostfixTimeStamp(){
        for(Confidence c : confidenceObjects){
            for(String s : c.postfix){
                for(UserBasket ub : c.prefAndPosUsers){
                    for (Map<String, List<String>> map : ub.itemToAllTimeStamp) {
                        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                            String key = entry.getKey();
                            List<String> value = entry.getValue();

                            if(key.equals(s)){
                                c.posTimeFrame.addAll(value);

                            }
                        }
                    }

                }
            }
        }


    }

    public  LocalDateTime getMinDateTime(List<String> dates){
        LocalDateTime minDateTime = LocalDateTime.now();
        try{
            Date min = df.parse(dates.getFirst());
            minDateTime = min.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

        }catch (Exception e){
            System.out.println("Error wile trying to get minimum date");

        }
        for(String s : dates){
            try {
                Date date = df.parse(s);
                LocalDateTime currentDateTime = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                if(currentDateTime.isBefore(minDateTime)){
                    minDateTime = currentDateTime;

                }

            }catch (Exception e){
                System.out.println("Error while finding minimum date");
            }

        }
        return minDateTime;

    }

    public  void saveMinTimeDifference() {
        for (Confidence c : confidenceObjects) {
            LocalDateTime minPos = getMinDateTime(c.posTimeFrame);
            LocalDateTime minPref = getMinDateTime(c.prefTimeFrame);
            c.minTimeDifference = abs(ChronoUnit.DAYS.between(minPref, minPos));
        }

    }

    public  void pruneTimeDiff(List<Confidence> sequences){
        List<Confidence> toDelete = new ArrayList<>();
        for(Confidence x : sequences){

            if(x.minTimeDifference > minTimeDiff){
                toDelete.add(x);

            }
        }
        for(Confidence y : toDelete){
            //System.out.println("Element Deleted is: "+ y.itemsJoined);
            sequences.remove(y);
        }

    }


}