package main.scala_feature_processing_lib

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.attribute.BinaryAttribute
import org.apache.spark.ml.param._
import main.tool_class.{HasInputCol, HasOutputCol}
import main.tool_class.SchemaUtils
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, StructType}

/**
  * Binarize a column of continuous features given a threshold.
  */

class Binarizer(override val uid: String) extends Transformer
  with HasInputCol with HasOutputCol {

  def this() = this(Identifiable.randomUID("binarizer"))

  /**
    * Param for threshold used to binarize continuous features.
    * The features greater than the threshold, will be binarized to 1.0.
    * The features equal to or less than the threshold, will be binarized to 0.0.
    * Default: 0.0
    * param
    */
  val threshold: DoubleParam =
    new DoubleParam(this, "threshold", "threshold used to binarize continuous features")

  /** getParam */
  def getThreshold: Double = $(threshold)

  /** setParam */
  def setThreshold(value: Double): this.type = set(threshold, value)

  setDefault(threshold -> 0.0)

  /** setParam */
  def setInputCol(value: String): this.type = set(inputCol, value)

  /** setParam */
  def setOutputCol(value: String): this.type = set(outputCol, value)

  override def transform(dataset: DataFrame): DataFrame = {
    transformSchema(dataset.schema, logging = true)
    val td = $(threshold)
    val binarizer = udf { in: Double => if (in > td) 1.0 else 0.0 }
    val outputColName = $(outputCol)
    val metadata = BinaryAttribute.defaultAttr.withName(outputColName).toMetadata()
    dataset.select(col("*"),
      binarizer(col($(inputCol))).as(outputColName, metadata))
  }

  override def transformSchema(schema: StructType): StructType = {
    SchemaUtils.checkColumnType(schema, $(inputCol), DoubleType)

    val inputFields = schema.fields
    val outputColName = $(outputCol)

    require(inputFields.forall(_.name != outputColName),
      s"Output column $outputColName already exists.")

    val attr = BinaryAttribute.defaultAttr.withName(outputColName)
    val outputFields = inputFields :+ attr.toStructField()
    StructType(outputFields)
  }

  override def copy(extra: ParamMap): Binarizer = defaultCopy(extra)
}