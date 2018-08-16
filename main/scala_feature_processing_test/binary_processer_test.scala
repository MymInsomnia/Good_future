package main.scala_feature_processing_test

import org.apache.spark.{SparkConf, SparkContext}
import main.scala_feature_processing_lib.Binarizer
import org.apache.spark.sql.hive.HiveContext

object binary_processer_test {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("scala_spark_test")
    val spark = new SparkContext(conf)
    val context = new HiveContext(spark)

    val data = Array((0, 0.1), (1, 0.8), (2, 0.2))
    val dataFrame = context.createDataFrame(data).toDF("id", "feature")

    val binarizer: Binarizer = new Binarizer()
      .setInputCol("feature")
      .setOutputCol("binarized_feature")
      .setThreshold(0.5)

    val binarizedDataFrame = binarizer.transform(dataFrame)

    println(s"Binarizer output with Threshold = ${binarizer.getThreshold}")
    binarizedDataFrame.show()
  }

}
