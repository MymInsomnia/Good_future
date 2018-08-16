package main.scala_feature_processing_lib

import main.scala_feature_processing.min_max_Normalizer
import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.types.DataType

class Min_max_Normalizer (override val uid: String) extends UnaryTransformer[Vector, Vector, Min_max_Normalizer]{

  def this() = this(Identifiable.randomUID("min_max_normalizer"))


  override protected def createTransformFunc: Vector => Vector = {
    val mm_normalizer = new min_max_Normalizer()
    mm_normalizer.transform
  }

  override protected def outputDataType: DataType = new VectorUDT()

}
