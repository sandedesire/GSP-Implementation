import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Testing {
    public static void main(String[] args) {
        List<String> one = new ArrayList<>();
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
        //combineTwo(one);
        //combineFive(one);

    }
    public static  void combineTwo(Set<String> element){
        List<String> elements = new ArrayList<>(element);
        for(int i =0; i<elements.size() ; i++){
            for(int j = i+1; j<elements.size();j++){
                System.out.println("Elements are :"+ elements.get(i)+elements.get(j));
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

}