package GSPImplementation;

import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class LoggingFile {


    public static void main(String[] args) throws IOException{
        String csvFile = "logging.csv";
        CSVWriter cw = new CSVWriter(new FileWriter(csvFile));
        String header[] = {"Dataset", "Number of entries", "Min. Support",
        "Min. Confidence","Min. TimeDiff","Number of Rules Generated",
                "TransactionalToSequenceTime", "TotalRunningTime"};

        //Writing data to the csv file
        cw.writeNext(header);

        //close the file
        cw.close();

    }
}
