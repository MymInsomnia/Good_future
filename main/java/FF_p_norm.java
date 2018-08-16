package main.java;

import jodd.util.collection.ArrayEnumeration;

import java.util.Iterator;
import java.util.Vector;

public class FF_p_norm {
    public static Vector normalize(Vector in_vector, int norm){
        if(in_vector.isEmpty()){
            System.out.println("Error: Empty vector!");
            return null;
        }
        if(in_vector.firstElement() instanceof String||in_vector.firstElement() instanceof Character){
            System.out.println("Error: Non-numeric element contained!");
            return null;
        }


        Vector<Double> ret_vector = new Vector<>(in_vector.size());
        double sum = 0;
        Iterator iterator1 = in_vector.iterator();
        Iterator iterator2 = in_vector.iterator();
        switch (norm){
            case 1:
                while(iterator1.hasNext()){
                        sum = sum + Math.abs(Double.parseDouble(iterator1.next().toString()));
                }
                while(iterator2.hasNext()){
                    ret_vector.add((Double.parseDouble(iterator2.next().toString())/sum));
                }
                return ret_vector;
            case 2:
                while(iterator1.hasNext()){
                    sum = sum + Math.pow(Double.parseDouble(iterator1.next().toString()),2);
                }
                while(iterator2.hasNext()){
                    ret_vector.add(Double.parseDouble(iterator2.next().toString())/sum);
                }
                return ret_vector;
            default:
                System.out.println("Error:norm error");
                break;
        }
        return null;
    }

    public static void vector_print(Vector v){
        if(v==null){
            System.out.println("NULL");
            return;
        }
        Iterator iter = v.iterator();
        while(iter.hasNext()){
            System.out.print(iter.next()+" ");
        }
        System.out.println();
    }



  /*  public static void main(String[] args){
        Vector<Integer> v_test_1 = new Vector(10);
        Vector<Double> v_test_2 = new Vector(10);
        Vector<String> v_test_3 = new Vector(10);
        Vector<Character> v_test_4 = new Vector(10);
        int i=0;
        while(i<10){
            v_test_1.add(2);
            v_test_2.add(2.0);
            v_test_3.add("2.0");
            v_test_4.add('C');
            i++;
        }
        Vector v1_1 = normalize(v_test_1,1);
        Vector v1_2 = normalize(v_test_1,2);
        Vector v2_1 = normalize(v_test_2,1);
        Vector v2_2 = normalize(v_test_2,2);
        Vector v2_3 = normalize(v_test_2,3);
        Vector v3_1 = normalize(v_test_3,1);
        Vector v4_1 = normalize(v_test_4,1);

        vector_print(v_test_1);
        vector_print(v1_1);
        vector_print(v1_2);
        vector_print(v_test_2);
        vector_print(v2_1);
        vector_print(v2_2);
        vector_print(v3_1);
        vector_print(v4_1);

    }
*/
}
