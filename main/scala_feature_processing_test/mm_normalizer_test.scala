package main.scala_feature_processing_test

import main.scala_feature_processing_lib.Min_max_Normalizer
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.hive.HiveContext

object mm_normalizer_test {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("scala_spark_test")
    val spark = new SparkContext(conf)
    val context = new HiveContext(spark)

    val normalizer = new Min_max_Normalizer()

    val dataFrame = context.createDataFrame(Seq(
      (0, Vectors.dense(1.0, 2.0, 3.0,5.0,8.0)),
      (1, Vectors.dense(2.0, 6.0, 8.0,10,12)),
      (2, Vectors.dense(4.0, 10.0, 2.0,12,17))
    )).toDF("id", "features")


    // Normalize each Vector using $L^1$ norm.
    val normaliz = new Min_max_Normalizer()
      .setInputCol("features")
      .setOutputCol("normFeatures")


    val l1NormData = normaliz.transform(dataFrame)
    println("Normalized using L^1 norm")
    l1NormData.show()

    // Normalize each Vector using $L^\infty$ norm.
    val lInfNormData = normaliz.transform(dataFrame)
    println("Normalized using L^inf norm")
    lInfNormData.show()
  }

}
