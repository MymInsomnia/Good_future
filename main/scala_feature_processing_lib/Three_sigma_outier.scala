package main.scala_feature_processing_lib

import main.scala_feature_processing.three_sigma_outier
import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.types.DataType

class Three_sigma_outier (override val uid: String) extends UnaryTransformer[Vector, Vector, Three_sigma_outier]{

  def this() = this(Identifiable.randomUID("three_sigma_outier"))


  override protected def createTransformFunc: Vector => Vector = {
    val three_sigma_outier = new three_sigma_outier()
    three_sigma_outier.transform
  }

  override protected def outputDataType: DataType = new VectorUDT()

}