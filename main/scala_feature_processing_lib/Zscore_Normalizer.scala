package main.scala_feature_processing_lib


import main.scala_feature_processing.zscore_Normalizer
import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.types.DataType

class Zscore_Normalizer (override val uid: String) extends UnaryTransformer[Vector, Vector, Zscore_Normalizer]{

  def this() = this(Identifiable.randomUID("zscore_normalizer"))


  override protected def createTransformFunc: Vector => Vector = {
    val z_normalizer = new zscore_Normalizer()
    z_normalizer.transform
  }

  override protected def outputDataType: DataType = new VectorUDT()

}
