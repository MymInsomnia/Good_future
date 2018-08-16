package main.scala_feature_processing

import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}
import main.tool_class.calculated_preparation.cal_mean_std

class three_sigma_outier (unit: Unit) extends VectorTransformer{

  /**
    * Judge if a value of the vector is an outier.
    * By computing if the value > mean_value+3*std_value or < mean_value-3*std_value
    * @param vector vector to be estimated.
    * @return vector only contains 0,1.1:outier 0:non-outier
    */
  override def transform(vector: Vector): Vector = {

    val (mean_val,std_val):(Double,Double) = cal_mean_std(vector)

    vector match {
      case DenseVector(vs) =>
        val values = vs.clone()
        val size = values.length
        var i = 0
        while (i < size) {
          values(i) = if(values(i)>=(mean_val+3*std_val) || values(i)<=(mean_val-3*std_val)) 1 else 0
          i += 1
        }
        Vectors.dense(values)
      case SparseVector(size, ids, vs) =>
        val values = vs.clone()
        val nnz = values.length
        var i = 0
        while (i < nnz) {
          values(i) = if(values(i)>=(mean_val+3*std_val) || values(i)<=(mean_val-3*std_val)) 1 else 0
          i += 1
        }
        Vectors.sparse(size, ids, values)
      case v => throw new IllegalArgumentException("Do not support vector type " + v.getClass)
    }
  }
}
