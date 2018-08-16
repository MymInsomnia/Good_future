package main.java;

import org.apache.commons.collections.IteratorUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;
import scala.Tuple2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class test {
    public static void main(String[] args){

        SparkConf conf = new SparkConf().setAppName("HelloSpark").setMaster("local[*]");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        HiveContext context = new HiveContext(sparkContext.sc());
        //String url = "jdbc:mysql://localhost:3306/spark?useUnicode=true&characterEncoding=utf8";
        //Properties connectionProperties = new Properties();
        //connectionProperties.setProperty("user", "root");// 设置用户名
        //connectionProperties.setProperty("password", "111111");// 设置密码
        //String table = "success_order";

        context.sql("use ods").show();
        context.sql("show tables").show();

        DataFrame dataFrame =  context.sql("select * from xes_stu_course where dt='2018-07-20'");

        //        DataFrame dataFrame = context.sql("select * from xes_stu_user_order where dt='2018-07-29' and orderstatus='2'");
//        dataFrame.createJDBCTable(url,"success_order",true);
//        System.out.println("fetch success!");
        DataFrame dataFrame1 = dataFrame.select("distinctid","orderstatus");
        dataFrame.show(3);
      //  flatSession();
/*
        try{
            dataFrame.write().mode(SaveMode.Overwrite).jdbc(url,table,connectionProperties);

            //write().mode(SaveMode.Overwrite).jdbc(url,table,connectionProperties);
        }catch (Exception e){
            System.out.println("fail!");
            System.out.println(e);
        }finally {
            sparkContext.stop();
        }
*/
       // dataFrame.createJDBCTable();
       // context.sql("select count(*) from event_test where dt='2018-07-19'").show();
      //  context.sql("select count(*) from user_info").show();
      //  context.sql("select count(*) from xes_stu_course").show();

    }

//    public static void flatSession() {
//        SparkConf conf = new SparkConf().setAppName("HelloSpark").setMaster("local[*]");
//        JavaSparkContext sparkContext = new JavaSparkContext(conf);
//        HiveContext context = new HiveContext(sparkContext.sc());
//        //long one_day = 1000*3600*24;
//        int timeSpan = 1000*60*5;
//
//        JavaPairRDD<String, Iterable<Row>> udGroupKeyRDD = getUserDateRDD(context);
//        JavaRDD<Tuple2<String, Integer>> results = udGroupKeyRDD.map(tuple -> {
//
//            String vid = tuple._1;
//            Iterator<Row> iter = tuple._2.iterator();
//            //先按日期排序
//            List<Row> timeList = IteratorUtils.toList(iter);
//            Long tempTime = 0L;
//            int session_id = 1;
//            if (timeList.size() > 0) {
//                tempTime = timeList.get(0).getAs("time");
//                for (int i = 1; i < timeList.size(); i++) {
//                    Long currTime = timeList.get(i).getAs("time");
//                    session_id = (currTime-tempTime)>timeSpan?session_id+1:session_id;
//                    tempTime = currTime;
//                }
//            }
//            return  new Tuple2<String, Integer>(vid, session_id);
//        });
//
//        results.foreach(tuple2 -> System.out.printf("sid :" + tuple2._1 + ", count :" + tuple2._2));
//    }
//
//    private static JavaPairRDD<String, Iterable<Row>> getUserDateRDD(HiveContext hiveContext) {
//        return hiveContext.sql("select * from testdb.one_day_test sort by time asc ")
//                .javaRDD().mapToPair(row -> new Tuple2<String, Row>(row.getAs("distinctid"), row)).groupByKey();
//    }

}
