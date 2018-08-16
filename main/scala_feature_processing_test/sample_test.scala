package main.scala_feature_processing_test

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive.HiveContext

object sample_test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("scala_spark_test")
    val spark = new SparkContext(conf)
    val context = new HiveContext(spark)

    val dataFrame = context.createDataFrame(Seq(
      (0, Vectors.dense(1.0, 11.0, 21.0)),
      (1, Vectors.dense(2.0, 17.0, 19.0)),
      (2, Vectors.dense(3.0, 17.0, 2.0)),
      (3, Vectors.dense(4.0, 12.0, 6.0)),
        (4, Vectors.dense(5.0, 10.0, 5.0)),
      (5, Vectors.dense(6.0, 14.0, 8)),
      (6, Vectors.dense(7.0, 40.0, 7)),
      (7, Vectors.dense(8.0, 56.0, 50)),
      (8, Vectors.dense(9.0, 77.0, 6)),
      (9, Vectors.dense(10.0, 47.0, 2.0))
    )).toDF("id", "features")

    val sampled_df = dataFrame.sample(false,0.5,40)
    sampled_df.show()


  }

}
