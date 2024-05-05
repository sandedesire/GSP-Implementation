import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


public class Testing {
    private static String OS = null;
    public static  List<List<String>> confidenceCombinations = new ArrayList<>();
    public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");


    public static void main(String[] args) {
        Set<String> one = new HashSet<>();
        one.add("A");
        one.add("B");
        one.add("C");
        one.add("D");
        List<String> two = new ArrayList<>();
        two.add("A");
        two.add("B");

        /*
        if ((one.size() == two.size()) && !(one.getLast().equals(two.getLast()))) {
            for (int i = 0; i < one.size() - 1; i++) {
                if (!one.get(i).equals(two.get(i))) {
                    break;
                }
            }
            one.add(two.getLast());
            System.out.println(one);
        }

         */
        combineTwo(one);
        //combineFive(one);
        System.out.println(confidenceCombinations);

        //Testing the conversion of a  string to a datetime object according to formatter
        String datetime_string = "01-12-2010 09:01";
        String datetime_string_empty = "";
        String datetime_string_error = "%^^&^01-12-2010 09:01";


        try {
            Date date = df.parse(datetime_string);

            System.out.println(date);
            LocalDateTime local_date_time =
                    date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            System.out.println(local_date_time);


        }
        catch (Exception e){
            e.printStackTrace();
        }
        String os = getOsName();
        System.out.println("Operating System is:"+os);


    }
    public static  void combineTwo(Set<String> element){
        List<String> elements = new ArrayList<>(element);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                System.out.println("Elements are :"+ elements.get(i)+elements.get(j));
                List<String> elem = new ArrayList<>();
                elem.add(elements.get(i));
                elem.add(elements.get(j));

                confidenceCombinations.add(elem);


            }
        }
    }

    public static  void combineThree(Set<String> element){
        List<String> elements = new ArrayList<>(element);

        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size(); j++){
                for(int k = j+1 ; k<elements.size(); k++){
                    System.out.println("Elements are :"+ elements.get(i)+elements.get(j)
                    +elements.get(k));

                }
            }
        }
    }

    public static  void combineFour(Set<String> element){
        List<String> elements = new ArrayList<>(element);

        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size(); j++){
                for(int k = j+1 ; k<elements.size(); k++){
                    for(int l = k+1 ; l<elements.size() ; l++){
                        System.out.println("Elements are :"+ elements.get(i)+elements.get(j)
                                +elements.get(k)+elements.get(l));
                    }


                }
            }
        }
    }

    public static  void combineFive(Set<String> element){
        List<String> elements = new ArrayList<>(element);

        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size(); j++){
                for(int k = j+1 ; k<elements.size(); k++){
                    for(int l = k+1 ; l<elements.size() ; l++){
                        for(int n = l+1 ; n<elements.size(); n++){
                            System.out.println("Elements are :"+ elements.get(i)+elements.get(j)
                                    +elements.get(k)+elements.get(l)+elements.get(n));
                        }

                    }


                }
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
    public static String getOsName()
    {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }
    public static boolean isWindows()
    {
        return getOsName().startsWith("Windows");
    }

}