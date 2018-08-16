package main.scala_feature_processing_lib

import main.scala_feature_processing.percentage_outier
import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.param.{DoubleParam, IntParam}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.types.DataType

class Percentage_outier (override val uid: String) extends UnaryTransformer[Vector, Vector, Three_sigma_outier]{

  def this() = this(Identifiable.randomUID("three_sigma_outier"))

  /**
    * Param for bottom used to definite the minimum_percentage of a non-outier.
    * Default: 0.1
    * param
    */
  val bottom: DoubleParam =
    new DoubleParam(this, "bottom_per", "data under bottom_per would be labeled as outier")

  /**
    * Param for upper used to definite the maximum_percentage of a non-outier.
    * Default: 0.9
    * param
    */
  val upper: DoubleParam =
    new DoubleParam(this, "upper_per", "data above up_per would be labeled as outier")

  /** getParam */
  def getBottom_val: Double = $(bottom)

  def getUpper_val: Double = $(upper)

  /** setParam */
  def setBottomm_val(value: Double): this.type = set(bottom, value)

  def setUpper_val(value: Double): this.type = set(upper, value)

  setDefault(bottom -> 0.1 , upper->0.9)

  override protected def createTransformFunc: Vector => Vector = {
    val per_outier = new percentage_outier()
    per_outier.transform
  }

  override protected def outputDataType: DataType = new VectorUDT()

}
