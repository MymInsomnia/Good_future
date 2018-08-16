package main.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class session_count_test {
    public static List<Integer> test(int dateSpan) {
        int one_day = 1000*3600*24;
        List<String> studentList = Arrays.asList("1","1","1","1","1","1","1","1");
        List<String> dateList = Arrays.asList("2018-08-01","2018-08-03","2018-08-16","2018-08-20","2018-09-01","2018-10-21","2018-10-25","2018-12-01");
        List<Integer> sessionList = new ArrayList<>();
        sessionList.add(1);
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        long temp_time = 0;
        try {
            temp_time = format1.parse(dateList.get(0)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int session_id = 1;
        for(int i=1;i<studentList.size();i++){
            try{
                //Date d1 = format1.parse(dateList.get(i-1));
                Date d2 = format1.parse(dateList.get(i));
                session_id = (d2.getTime()-temp_time)/one_day>dateSpan?session_id+1:session_id;
                sessionList.add(session_id);
                temp_time = d2.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return sessionList;
    }

    public static void main(String[] args){
        List<Integer> result = test(5);
        System.out.println(result);
    }
}
