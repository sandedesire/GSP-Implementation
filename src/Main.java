package GSPImplementation;

import javafx.application.Application;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import GSPImplementation.GSP;


public class Main extends Application {
    Label running = new Label();
    Label finished = new Label();
    TextArea result = new TextArea();
    FileChooser fileChooser = new FileChooser();
    File resourceFile;
    TextField minSupFld = new TextField();
    TextField minConfTfld = new TextField();
    TextField minTimDiffTfld = new TextField();
    Label minTimDiff = new Label("Days");
    List<String> comboIndexes;
    TextArea textArea = new TextArea();
    int idIndex;
    int descIndex;
    int timeStampIndex;




    ComboBox<String> customerID = new ComboBox<>();
    ComboBox<String> description = new ComboBox<>();
    ComboBox<String> invoiceDate = new ComboBox<>();
    ArrayList<List<String>> data;
    ArrayList<Map<String,List<String>>> database = new ArrayList<>();
    ObservableList<String> items = FXCollections.observableArrayList();
    HashSet<String> uniqueIDs = new HashSet<>();
    HashSet<String> uniqueItems = new HashSet<>();
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");


    // Set the ComboBox to use the items list
    //comboBox.setItems(items);

    // Allow the user to update the items in the list
    //items.add("A new String");
    public static void main(String[] args) {
        Application.launch(args);

    }

    @Override
    public void start(Stage stage){
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
        right.setPrefWidth(300);
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

        Button startBtn = new Button("Start");
        Button stopBtn = new Button("Stop");
        Button saveBtn = new Button("Save Result to File");
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(startBtn,stopBtn,saveBtn);









        //rightElements.getChildren().addAll(suppConfHBox,comboBoxesHBox);
        right.setSpacing(10);
        right.getChildren().addAll(suppConfHBox,comboBoxesVBox,
                advancedControlText,timDifHBox,buttons,result);
        //*** END  OF ELEMENTS OF RIGHT****

        //*** START  OF ELEMENTS OF LEFT****
        VBox left  = new VBox();

        left.setPrefWidth(400);
        textArea.setWrapText(true);
        textArea.setPrefHeight(800);
        textArea.setPadding(new Insets(5));

        left.getChildren().addAll(textArea);
        //*** END  OF ELEMENTS OF LEFT****



        root.setTop(toolBar);
        //borderPane.setCenter(appContent);
        root.setBottom(statusbar);
        root.setRight(right);
        root.setLeft(left);


        Scene scene = new Scene(root,700,480);
        stage.setScene(scene);
        stage.setTitle("GSP Retail Mining");
        stage.show();



        importCSV.setOnAction(e -> handleImportCSV(e,stage));
        startBtn.setOnAction(e -> handleStartBtn(e,stage));
        saveToFile.setOnAction(e -> handleSaveResult(e,stage));
        saveBtn.setOnAction(e -> handleSaveResult(e,stage));





    }


    public void handleStartBtn(javafx.event.ActionEvent e, Stage stage) {
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
                GSP gsp = new GSP(
                        resourceFile,idIndex,descIndex,timeStampIndex,minSupValue,
                        minConfValue,minTimeDiffValue
                );

                StringProperty info = new SimpleStringProperty();
                info.bind(GSP.userInfo);

                textArea.setText(info.get());

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
        result.setText(GSP.resultFile.getAbsolutePath().toString());

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










}