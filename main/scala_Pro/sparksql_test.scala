package main.scala_Pro

import org.apache.spark.sql.{DataFrame, GroupedData}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object sparksql_test {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("scala_spark_test")
    val spark = new SparkContext(conf)
    val context = new HiveContext(spark)
    context.sql("use testdb").show()
    context.sql("show tables").show()
    val data = context.sql("select distinctid,time from one_day_test")
    data.show(20)


  }

}
