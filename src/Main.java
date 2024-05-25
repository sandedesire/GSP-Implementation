package GSPImplementation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static GSPImplementation.Main.info;
import static java.lang.Math.abs;
import GSPImplementation.UserBasket;
import GSPImplementation.UserData;
import GSPImplementation.Confidence;
import GSPImplementation.SubSequence;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



//import GSPImplementation.GSP;


public class Main extends Application {
    public  static File resultFile;

    public static StringProperty userInfo = new SimpleStringProperty();


    public  String pathToResult;
    public  static StringBuilder stringBuilder = new StringBuilder();
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






    //End of GSP
    public static StringProperty info = new SimpleStringProperty();

    Label running = new Label("Running!!!!");
    Label finished = new Label("Finished!!!");
    TextArea result = new TextArea();
    FileChooser fileChooser = new FileChooser();
    File resourceFile;
    TextField minSupFld = new TextField();
    TextField minConfTfld = new TextField();
    TextField minTimDiffTfld = new TextField();
    Label minTimDiff = new Label("Days");
    List<String> comboIndexes;
    public    TextArea textArea = new TextArea();
    int idIndex;
    int descIndex;
    int timeStampIndex;

    ObservableList<String> listForContent = FXCollections.observableArrayList();






    ComboBox<String> customerID = new ComboBox<>();
    ComboBox<String> description = new ComboBox<>();
    ComboBox<String> invoiceDate = new ComboBox<>();
    ArrayList<List<String>> data;
    ObservableList<String> items = FXCollections.observableArrayList();
    HashSet<String> uniqueItems = new HashSet<>();

    Button startBtn = new Button("Start");
    Button stopBtn = new Button("Stop");
    Button saveBtn = new Button("Save Result to File");


    // Set the ComboBox to use the items list
    //comboBox.setItems(items);

    // Allow the user to update the items in the list
    //items.add("A new String");
    public static void main(String[] args) {
        Application.launch(args);

    }

    @Override
    public void start(Stage stage){
        //info.addListener(Main::onChanged);

        //Setting all the controls disabled
         minSupFld.setDisable(true);
         minTimDiffTfld.setDisable(true);
        minTimDiff.setDisable(true);
        customerID.setDisable(true);
        description.setDisable(true);
        invoiceDate.setDisable(true);
        minConfTfld.setDisable(true);

        BorderPane root = new BorderPane();
        ToolBar toolBar = new ToolBar();
        HBox statusbar = new HBox();
        

        //*** START OF ELEMENTS OF THE HEADER****
        Button importCSV = new Button("Import .CSV");
        toolBar.getItems().add(importCSV);

        toolBar.getItems().add(new Separator());

        Button importTXT = new Button("Import .TXT");

        //FUTURE IMPLEMENTATION FOR TEXT MINING
        //toolBar.getItems().add(importTXT);

        //toolBar.getItems().add(new Separator());

        Button saveToFile = new Button("Save Results");
        toolBar.getItems().add(saveToFile);



        toolBar.getItems().add(new Separator());
        //*** END OF ELEMENTS OF THE HEADER****

        //*** START OF ELEMENTS OF THE FOOTER****
        Text luxUni = new Text("Luxembourg University");
        statusbar.setAlignment(Pos.BASELINE_CENTER);
        statusbar.getChildren().addAll(luxUni);

        //*** END OF ELEMENTS OF THE FOOTER****

        //*** START OF ELEMENTS OF RIGHT****
        VBox right  = new VBox();
        right.setPrefWidth(700);
        right.setPrefHeight(1300);
        right.setPadding(new Insets(10));
        Group rightElements = new Group();
        Label minSupLabel = new Label("Min. Support");
        Label minConfLabel = new Label("Min. Confidence");
        HBox suppConfHBox = new HBox();
        suppConfHBox.setSpacing(10);
        minSupFld.setPrefWidth(40);
        minConfTfld.setPrefWidth(40);
        suppConfHBox.getChildren().addAll(minSupLabel,minSupFld,
                minConfLabel,minConfTfld);

        VBox comboBoxesVBox = new VBox();
        comboBoxesVBox.setSpacing(10);
        Label customerIDLabel = new Label("Customer ID");
        Label descriptionLabel = new Label("Description");
        Label invoiceDateLabel = new Label("Invoice Date");
        comboBoxesVBox.getChildren().addAll(new HBox(customerIDLabel,customerID),
                new HBox(descriptionLabel, description),
                new HBox(invoiceDateLabel, invoiceDate));

        Text advancedControlText = new Text("Advanced Controls");

        Label minTimDiffLabel = new Label("Minimum Time Difference");
        minTimDiffTfld.setPrefWidth(40);
        minTimDiff.setPrefWidth(80);



        HBox timDifHBox = new HBox(10);
        timDifHBox.getChildren().addAll(minTimDiffLabel,minTimDiffTfld,minTimDiff);


        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(startBtn,stopBtn,saveBtn);









        //rightElements.getChildren().addAll(suppConfHBox,comboBoxesHBox);
        right.setSpacing(10);
        right.getChildren().addAll(suppConfHBox,comboBoxesVBox,
                advancedControlText,timDifHBox,buttons,result,running,finished);
        running.setVisible(false);
        finished.setVisible(false);
        //*** END  OF ELEMENTS OF RIGHT****

        //*** START  OF ELEMENTS OF LEFT****
        VBox left  = new VBox();

        left.setPrefWidth(1000);
        textArea.setWrapText(true);
        textArea.setWrapText(true);
        textArea.setPrefHeight(1300);
        textArea.setPadding(new Insets(5));

        left.getChildren().addAll(textArea);
        //*** END  OF ELEMENTS OF LEFT****



        root.setTop(toolBar);
        //borderPane.setCenter(appContent);
        root.setBottom(statusbar);
        root.setRight(right);
        root.setLeft(left);


        Scene scene = new Scene(root,1400,1200);
        stage.setScene(scene);
        stage.setTitle("GSP Retail Mining");
        stage.show();



        importCSV.setOnAction(e -> handleImportCSV(e,stage));
        startBtn.setOnAction(e -> startTask());
        saveToFile.setOnAction(e -> handleSaveResult(e,stage));
        saveBtn.setOnAction(e -> handleSaveResult(e,stage));
        stopBtn.setOnAction(e -> Platform.exit());





    }

    public void startTask(){
        //Create a Runnable
        Runnable task = () -> handleStartBtn();

        //Run the task in a backgroud thread
        Thread backgroundThread = new Thread(task);

        //Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);

        //Start the thread
        backgroundThread.start();
    }


    public void handleStartBtn() {

        //Ensuring all parameters are set
        if (minSupFld.getText().trim().equals("") ||
                minConfTfld.getText().trim().equals("") ||
                customerID.getValue().trim().equals("") ||
                description.getValue().trim().equals("") ||
                invoiceDate.getValue().trim().equals("") ||
                minTimDiffTfld.getText().trim().equals("") ){
            showErrorDialog();
        } else {
            //Creating an Array list ot serve as our DATABASE
            //ie it will hold ALL THE DATA ITEMS
            //data = new ArrayList<>();


            //Declaring all variables to hold data
            float minSupValue;
            float minConfValue;
            String descriptionStr;
            String customerIDStr;
            String invoiceDateStr;
            int minTimeDiffValue;


            //Getting all the parameter values
            try {
                minSupValue = Float.valueOf(minSupFld.getText().trim().toString());
                minConfValue = Float.valueOf(minConfTfld.getText().trim().toString());
                descriptionStr = description.getValue().trim().toString();
                customerIDStr = customerID.getValue().trim().toString();
                invoiceDateStr = invoiceDate.getValue().trim().toString();
                minTimeDiffValue = Integer.valueOf(
                        minTimDiffTfld.getText().trim().toString());

            } catch (NumberFormatException numberError) {
                showErrorDialog2();
                return;
            }

            /*
            //Printing all parameters for checking
            System.out.println("Min. Support:" + minSupValue);
            System.out.println("Min. Confidence:" + minConfValue);
            System.out.println("Customer ID:" + customerIDStr);
            System.out.println("Description:" + descriptionStr);
            System.out.println("Invoice Date:" + invoiceDateStr);


             */

            //Getting the index values for customer ID, Description and InvoiceDate
            idIndex = comboIndexes.indexOf(customerIDStr);
            descIndex = comboIndexes.indexOf(descriptionStr);
            timeStampIndex = comboIndexes.indexOf(invoiceDateStr);


            //CHECK THAT MIN SUP AND MIN CONF HAS A BASIC VALUE
            if(minSupValue < 0.15 || minConfValue < 0.3){
                showErrorDialog3();

            }else {
                running.setVisible(true);
                startBtn.setDisable(true);

                textArea.setText("");



                info.bind(userInfo);

                GSP(
                        resourceFile,idIndex,descIndex,timeStampIndex,minSupValue,
                        minConfValue,minTimeDiffValue
                );
                finished.setVisible(true);

                running.setVisible(false);
                startBtn.setDisable(false);







            }










        }


    }



    public void handleImportCSV(javafx.event.ActionEvent e,Stage stage) {
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Excel", "*.xls"),
                new FileChooser.ExtensionFilter("Excel2", "*.xlsx"),
                new FileChooser.ExtensionFilter("ODS", "*.ods")

        );
         resourceFile = fileChooser.showOpenDialog(stage);

        String line = "";
        String splitBy = ",";
        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(resourceFile));

            //Adding the CSV Headers to the items list for the combo boxes
            String[] header = br.readLine().split(splitBy);
            for (String item : header) {
                items.add(item);
            }
            comboIndexes= Arrays.asList(header);
            customerID.setItems(items);
            description.setItems(items);
            invoiceDate.setItems(items);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //Setting all the controls enabled
        minSupFld.setDisable(false);
        minTimDiffTfld.setDisable(false);
        minTimDiff.setDisable(false);
        customerID.setDisable(false);
        description.setDisable(false);
        invoiceDate.setDisable(false);
        minConfTfld.setDisable(false);


    }

    public void handleSaveResult(ActionEvent e, Stage stage){
        result.setWrapText(true);
        result.setText(resultFile.getAbsolutePath().toString());

    }



    public void showErrorDialog(){
        Stage stage = new Stage();
        VBox root = new VBox(new Text("All Parameters not set."),
                new Text("Ensure Min. Support and Min Confidence are Real Numbers"),
                new Text("Real numbers in the range 0 and 1"));

        Scene scene = new Scene(root,700,700);
        stage.setScene(scene);
        stage.setTitle("Error");
        stage.show();

    }

    public void showErrorDialog2(){
        Stage stage = new Stage();
        VBox root = new VBox(new Text("Not an appropriate real number entered for " +
                "Min Suport or Min Confidence"));

        Scene scene = new Scene(root,700,700);
        stage.setScene(scene);
        stage.setTitle("Error");
        stage.show();

    }

    public void showErrorDialog3(){
        Stage stage = new Stage();
        VBox root = new VBox(new Text("Min Sup >= 0.15 AND Min Conf >=0.3 "));

        Scene scene = new Scene(root,700,700);
        stage.setScene(scene);
        stage.setTitle("Error");
        stage.show();

    }






    public  void GSP(File resourceFile,int UserIDColumn,int ItemColumn,int TimeStampColumn,
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
        informeUser();

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

        informeUser();





        //Data Cleaning. TO BE IMPLEMENTED
        System.out.println("Number of rows Before data cleaning:"+database.size());
        stringBuilder.append("Number of rows Before data cleaning:"+database.size()+"\n");
        dataCleaning(database);
        System.out.println("Number of rows After data cleaning:"+database.size());
        stringBuilder.append("Number of rows After data cleaning:"+database.size()+"\n");
        stringBuilder.append("-----------------------------------\n");


        informeUser();



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
        stringBuilder.append("-----------------------------------\n");

        System.out.println("SIZE OF USER BASKET LIST:" +userBasketDatabase.size());
        stringBuilder.append("SIZE OF USER BASKET LIST:"
                +userBasketDatabase.size()+"\n");
        stringBuilder.append("-----------------------------------\n");

        stringBuilder.append("Now Running the GSP " +
                "Algorithm...Might take some time!"+"\n");
        stringBuilder.append("-----------------------------------\n");




        //Setting all items each user ever bought and All Timestamp
        setAllItemsEachUserEverboughtAndAllTimeStamp(database,
                userBasketDatabase);
        System.out.println("VERIFICATION AFTER UNIQUENESS ITEMS AND TIMESTAMP");
        System.out.println(userBasketDatabase.get(2).getAllItemsEverBought());
        System.out.println(userBasketDatabase.get(2).getUsersTimeStamp());
        informeUser();

        setAllUsersMap(database,userBasketDatabase);

        informeUser();





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









        informeUser();

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





        informeUser();
        System.out.println("------------------GSP----------------------------------------");

        round1(logs.getLast());
        round2(logs.getLast());
        round3(logs.getLast());
        round4(logs.getLast());
        round5(logs.getLast());

        informeUser();

        getWinnerSubSequenceForm(logs);
        //Will Finish it's implementation in future versions
        //processWinnerSubsequences(winners);

        //Calculate Confidence

        calculateConfidence(winners);
        System.out.println(confidenceCombinations);
        stringBuilder.append(confidenceCombinations+"\n");
        informeUser();

        //DETERMINING THE MINIMUM TIMEDIFFERENCE AMONG THE RULES
        isolateRespetiveUsers();
        getPrefixTimeStamp();
        getPostfixTimeStamp();







        saveMinTimeDifference();
        pruneTimeDiff(confidenceObjects);

        informeUser();

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
        informeUser();

        saveResultsToFile(stringBuilder);

        informeUser();


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
            c.minTimeDifference = ChronoUnit.DAYS.between(minPref, minPos);
        }

    }

    public  void pruneTimeDiff(List<Confidence> sequences){
        List<Confidence> toDelete = new ArrayList<>();
        for(Confidence x : sequences){

            if(x.minTimeDifference > minTimeDiff || x.minTimeDifference < 0){
                toDelete.add(x);

            }
        }
        for(Confidence y : toDelete){
            //System.out.println("Element Deleted is: "+ y.itemsJoined);
            sequences.remove(y);
        }

    }

    public  void informeUser(){
        userInfo.set(stringBuilder.toString());
        //textArea.setText(info.get().toString());
        Platform.runLater(() -> textArea.setText(info.get().toString()));

    }










}