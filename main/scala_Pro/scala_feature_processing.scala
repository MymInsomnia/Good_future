package main.scala_Pro
import org.apache.spark.ml.feature.Normalizer
import breeze.linalg.{Vector, norm}
//
import org.apache.spark.ml.feature.PolynomialExpansion
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.DataFrame

object scala_feature_processing {

  def p_normalize(p:Int,data:DataFrame)={

  }

  def main(args: Array[String]): Unit = {

    val v = Vector(1,2,3,4,5)

    val v1 = v.map(x=>x to 5)
    val v2 = v1.map(x=>x.toList)
    println(v1)
    println(v2)
  }

}