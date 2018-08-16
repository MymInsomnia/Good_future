package main.scala_feature_processing_test

import main.scala_feature_processing_lib.Equidistant_discretizer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.hive.HiveContext

object equidistant_discretize_test {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("scala_spark_test")
    val spark = new SparkContext(conf)
    val context = new HiveContext(spark)

    val dataFrame = context.createDataFrame(Seq(
      (0, Vectors.dense(1.0, 1.0, 7.0,3.0,5)),
      (1, Vectors.dense(2.0, 6.0, 7.0,8,10)),
      (2, Vectors.dense(4.0, 10.0,32,12,24))
    )).toDF("id", "features")


    // Discretize data in 2 pieces with eqidistancet.
    val discretizer = new Equidistant_discretizer()
      .setInputCol("features")
      .setOutputCol("discretized_Features")



    val equidistant_Data = discretizer.transform(dataFrame)
    println("discretized in 2")
    equidistant_Data.show()

    // Discretize data in 4 pieces with eqidistancet.

    val equidistant_Data_4 = discretizer.setDis_num(4).transform(dataFrame)
    println("discretized in 4")
    equidistant_Data_4.show()
  }

}
