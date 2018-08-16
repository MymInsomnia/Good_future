package main.scala_feature_processing

import breeze.linalg.{max, min}
import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}

class min_max_Normalizer(unit: Unit) extends VectorTransformer{

  /**
    * Calculate the min,max value of the vector.
    *
    * @param vector vector to be calculated.
    * @return the min_val and max_val of the vector.
    */

  def cal_min_max(vector: Vector)={
    val values = vector.toArray
    val min_val = min(values)
    val max_val = max(values)
    (min_val,max_val)
  }

  /**
    * Applies normalization on a vector.
    *
    * @param vector vector to be normalized.
    * @return normalized vector. If the max-val is equal to min-val of the vector, it will return the input vector.
    */
  override def transform(vector: Vector): Vector = {

    val (min_val,max_val) = cal_min_max(vector)
    if (min_val!=max_val) {
      // For dense vector, we've to allocate new memory for new output vector.
      // However, for sparse vector, the `index` array will not be changed,
      // so we can re-use it to save memory.
      vector match {
        case DenseVector(vs) =>
          val values = vs.clone()
          val size = values.length
          var i = 0
          while (i < size) {
            values(i) = (max_val-values(i))/(max_val-min_val)
            i += 1
          }
          Vectors.dense(values)
        case SparseVector(size, ids, vs) =>
          val values = vs.clone()
          val nnz = values.length
          var i = 0
          while (i < nnz) {
            values(i) = (max_val-values(i))/(max_val-min_val)
            i += 1
          }
          Vectors.sparse(size, ids, values)
        case v => throw new IllegalArgumentException("Do not support vector type " + v.getClass)
      }
    } else {
      // As the min-val is equal to the max-val, return the input vector object itself.
      // Note that it's safe since we always assume that the data in RDD should be immutable.
      println("Min_max_normalize cannot be implemented cause the min-val==max-val")
      vector
    }

  }
}
