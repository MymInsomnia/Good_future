package main.scala_feature_processing_test

import main.scala_feature_processing_lib.P_Normalizer
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * p_normalize_test
  * case for test the P_Normalizer class
  *
  * */

object p_normalize_test {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("scala_spark_test")
    val spark = new SparkContext(conf)
    val context = new HiveContext(spark)

    val normalizer = new P_Normalizer()

    val dataFrame = context.createDataFrame(Seq(
      (0, Vectors.dense(1.0, 11.0, 21.0)),
      (1, Vectors.dense(2.0, 17.0, 19.0)),
      (2, Vectors.dense(4.0, 10.0, 2.0))
    )).toDF("id", "features")


    // Normalize each Vector using $L^1$ norm.
    val normaliz = new P_Normalizer()
      .setInputCol("features")
      .setOutputCol("normFeatures")
      .setP(1.0)

    val l1NormData = normaliz.transform(dataFrame)
    println("Normalized using L^1 norm")
    l1NormData.show()

    // Normalize each Vector using $L^\infty$ norm.
    val lInfNormData = normaliz.transform(dataFrame, normaliz.p -> Double.PositiveInfinity)
    println("Normalized using L^inf norm")
    lInfNormData.show()
  }


}
