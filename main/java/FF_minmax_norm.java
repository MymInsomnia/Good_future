package main.java;

import java.util.Iterator;
import java.util.Vector;

import static main.java.FF_p_norm.vector_print;

public class FF_minmax_norm {
    public static Vector normalize(Vector in_vector){
        if (in_vector.isEmpty()) {
            System.out.println("Empty vector!");
            return null;
        }

        if(in_vector.firstElement() instanceof String||in_vector.firstElement() instanceof Character){
            System.out.println("Error: Non-numeric element contained!");
            return null;
        }
        double min_sample = Double.parseDouble(in_vector.firstElement().toString());
        double max_sample = min_sample;
        Iterator iterator1 = in_vector.iterator();
        Iterator iterator2 = in_vector.iterator();
        while(iterator1.hasNext()){
            Object next_obj = iterator1.next();
            double next_obj_val = Double.parseDouble(next_obj.toString());
            min_sample = next_obj_val<min_sample?next_obj_val:min_sample;
            max_sample = next_obj_val>max_sample?next_obj_val:max_sample;
        }
        if(min_sample == max_sample){
            System.out.println("Cannot operate min_max_norm: min==max");
            return null;
        }
        Vector<Double> ret_vector = new Vector<>(in_vector.size());
        while(iterator2.hasNext()){
            Object next_obj = iterator2.next();
            double val = Double.parseDouble(next_obj.toString());
            ret_vector.add((val-min_sample)/(max_sample-min_sample));
        }

        return ret_vector;
    }
    public static void main(String[] args){
        Vector<Integer> v_test_1 = new Vector(10);
        Vector<Double> v_test_2 = new Vector(10);
        Vector<String> v_test_3 = new Vector(10);
        Vector<Character> v_test_4 = new Vector(10);

    }


}



