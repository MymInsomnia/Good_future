package main.scala_feature_processing_lib

import main.scala_feature_processing.{equidistant_discretizer, p_Normalizer}
import main.tool_class.{HasInputCol, HasOutputCol, SchemaUtils}
import org.apache.spark.ml.{Transformer, UnaryTransformer}
import org.apache.spark.ml.attribute.BinaryAttribute
import org.apache.spark.ml.param.{DoubleArrayParam, DoubleParam, IntParam, ParamMap}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.types.{DataType, DoubleType, StructType}

class Equidistant_discretizer (override val uid: String) extends UnaryTransformer[Vector, Vector, Equidistant_discretizer] {

  def this() = this(Identifiable.randomUID("Equidistant_discretizer"))

  /**
    * Param for d used to control divide data into pieces.
    * Param for bias used to fix the error when calculating the max-value
    * If d = 3,then the output label will only contains 0,1,2.
    * Default: d -> 2, bias -> 0.001
    */
  val d: IntParam =
    new IntParam(this, "divide_n", "divide the data into divide_n pieces")

  val bias: DoubleParam =
    new DoubleParam(this, "bias", "enlarge the per_distant very little")

  /** getParam */
  def getDis_num: Int = $(d)

  def getBias: Double = $(bias)

  /** setParam */
  def setDis_num(value: Int): this.type = set(d, value)

  def setBias(value: Double): this.type = set(bias, value)

  setDefault(d -> 2,bias->0.001)

  override protected def createTransformFunc: Vector => Vector = {
    val equidistant_disperse = new equidistant_discretizer($(d),$(bias))
    equidistant_disperse.transform
  }

  override protected def outputDataType: DataType = new VectorUDT()
}
