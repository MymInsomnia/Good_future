package main.tool_class

import org.apache.spark.ml.param.{Param, Params, StringArrayParam}

/**
  * Trait for shared param inputCol.
  */
trait HasInputCol extends Params{

  /**
    * Param for input column name.
    * param
    */
  final val inputCol: Param[String] = new Param[String](this, "inputCol", "input column name")

  /** getParam */
  final def getInputCol: String = $(inputCol)
}

trait HasInputCols extends Params {

  /**
    * Param for input column names.
    * param
    */
  final val inputCols: StringArrayParam = new StringArrayParam(this, "inputCols", "input column names")

  /** getParam */
  final def getInputCols: Array[String] = $(inputCols)
}

  /**
    * Trait for shared param outputCol (default: uid + "_output").
    */

trait HasOutputCol extends Params {

  /**
    * Param for output column name.
    * param
    */
  final val outputCol: Param[String] = new Param[String](this, "outputCol", "output column name")

  setDefault(outputCol, uid + "__output")

  /** getParam */
  final def getOutputCol: String = $(outputCol)
}
