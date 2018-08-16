package main.java;



import java.util.Iterator;
import java.util.Vector;

import static main.java.FF_p_norm.vector_print;

public class FF_zscore_norm {
    public static Vector normalize(Vector in_vector){
        if (in_vector.isEmpty()) {
            System.out.println("Empty vector!");
            return null;
        }

        if(in_vector.firstElement() instanceof String||in_vector.firstElement() instanceof Character){
            System.out.println("Error: Non-numeric element contained!");
            return null;
        }

        Iterator iterator1 = in_vector.iterator();
        Iterator iterator2 = in_vector.iterator();
        Iterator iterator3 = in_vector.iterator();
        Vector<Double> ret_vector = new Vector<>(in_vector.size());
        double sum = 0;

        while(iterator1.hasNext()){
            sum += Double.parseDouble(iterator1.next().toString());
        }

        double mean_val = sum/in_vector.size();
        double var_val = 0;
        while(iterator2.hasNext()){
            var_val += Math.pow(Double.parseDouble(iterator2.next().toString())-mean_val,2);
        }
        double std_val = Math.pow(var_val/in_vector.size(),0.5);
        while(iterator3.hasNext()){
            ret_vector.add((Double.parseDouble(iterator3.next().toString())-mean_val)/std_val);
        }

        return ret_vector;

    }
    public static void main(String[] args){
        Vector<Double> test_vector = new Vector<>();
        test_vector.add(1.0);
        test_vector.add(1.0);
        test_vector.add(5.0);
        test_vector.add(5.0);
//        test_vector.add(5.0);
//        test_vector.add(6.0);
//        test_vector.add(7.0);
//        test_vector.add(8.0);
//        test_vector.add(9.0);
//        test_vector.add(10.0);
        Vector<Double> ret = normalize(test_vector);
        vector_print(ret);

    }
}
