package main.scala_feature_processing_lib

import main.scala_feature_processing.equpro_discretizer
import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.param.{DoubleParam, IntParam}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.types.DataType

class Equpro_discretizer (override val uid: String) extends UnaryTransformer[Vector, Vector, Equpro_discretizer] {

  def this() = this(Identifiable.randomUID("Equpro_discretizer"))

  /**
    * Param for d used to control discretization of the data
    * Default: 2.0, then the output only contains 0,1
    * param
    */
  val d: IntParam =
    new IntParam(this, "divide_n", "divide the data into divide_n pieces")

  /** getParam */
  def getDis_num: Int = $(d)

  /** setParam */
  def setDis_num(value: Int): this.type = set(d, value)

  setDefault(d -> 2)

  override protected def createTransformFunc: Vector => Vector = {
    val equpro_disperse = new equpro_discretizer($(d))
    equpro_disperse.transform
  }

override protected def outputDataType: DataType = new VectorUDT()
}
