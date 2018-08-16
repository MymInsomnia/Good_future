package main.scala_feature_processing_test

import main.scala_feature_processing_lib.Equpro_discretizer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.hive.HiveContext

object equpro_discretizer_test {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("scala_spark_test")
    val spark = new SparkContext(conf)
    val context = new HiveContext(spark)


    val dataFrame = context.createDataFrame(Seq(
      (0, Vectors.dense(1.0, 2.0, 3.0,4.0,5,6)),
      (1, Vectors.dense(2.0, 6.0, 7.0,8,10,15)),
      (2, Vectors.dense(1.0, 2,3,4,100,200))
    )).toDF("id", "features")


    val discretizer = new Equpro_discretizer()
      .setInputCol("features")
      .setOutputCol("discretized_Features")



    val l1NormData = discretizer.transform(dataFrame)
    println("discretized in 2")
    l1NormData.show()

    val lInfNormData = discretizer.setDis_num(3).transform(dataFrame)
    println("discretized in 3")
    lInfNormData.show()
  }

}
