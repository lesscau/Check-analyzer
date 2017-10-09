import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Руся on 28.09.2017.
 */
public class Main {
    public static void main(String[] args) {
        int n = 3;
        float[] prices = {100, 240, 90, 450};
        Map<Integer,List<Integer>> checklist = new HashMap<Integer, List<Integer>>();
        List<Integer> a = new ArrayList<Integer>();
        List<Integer> b = new ArrayList<Integer>();
        List<Integer> c = new ArrayList<Integer>();
        List<Integer> d = new ArrayList<Integer>();
        b.add(0,1);
        b.add(1,2);
        c.add(0,0);
        d.add(0,0);
        d.add(1,2);
        d.add(2,1);
        checklist.put(0,a);
        checklist.put(1,b);
        checklist.put(2,c);
        checklist.put(3,d);
        float[] Itog = Summary.Calculate(n, prices, checklist);
        for(int i = 0; i < Itog.length; i++){
            System.out.println(Itog[i]);
        }
    }
}
