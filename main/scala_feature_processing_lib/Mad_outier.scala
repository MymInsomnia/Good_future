package main.scala_feature_processing_lib

import main.scala_feature_processing.mad_outier
import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.types.DataType

class Mad_outier (override val uid: String) extends UnaryTransformer[Vector, Vector, Mad_outier]{

  def this() = this(Identifiable.randomUID("min_max_normalizer"))


  override protected def createTransformFunc: Vector => Vector = {
    val mad = new mad_outier()
    mad.transform
  }

  override protected def outputDataType: DataType = new VectorUDT()

}
