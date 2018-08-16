package main.scala_feature_processing

import breeze.linalg.{max, min}
import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}
import main.tool_class.calculated_preparation.discretizer_able


class equidistant_discretizer (d:Int,bias:Double) extends VectorTransformer {

  def this() = this(2,0.001)

  require(d > 1,bias > 0)

  /**
    * Calculate the min value & per_distance of the vector in d pieces.
    *
    * @param vector vector to be calculated.
    * @return (Double,Double) the min_val and per_distance of the vector.
    */

  def cal_min_per_distance(vector: Vector, d: Int) = {
    val values = vector.toArray
    val min_val = min(values)
    val max_val = max(values)
    (min_val, (max_val - min_val) / d)
  }

  /**
    * Applies discretization on a vector.
    *
    * @param vector vector to be discretize.
    * @return discretized vector. If the per_val of the input vector is zero or
    *         the split amount larger than the distinct value of vector,
    *         it will return the input vector.
    */
  override def transform(vector: Vector): Vector = {

    val (min_val, per_val):(Double,Double) = cal_min_per_distance(vector, d)
    if ((per_val != 0.0) && discretizer_able(vector,d)) {
      // For dense vector, we've to allocate new memory for new output vector.
      // However, for sparse vector, the `index` array will not be changed,
      // so we can re-use it to save memory.
      vector match {
        case DenseVector(vs) =>
          val values = vs.clone()
          val size = values.length
          var i = 0
          while (i < size) {
            values(i) = math.floor((values(i) - min_val) / (per_val + bias))
            i += 1
          }
          Vectors.dense(values)
        case SparseVector(size, ids, vs) =>
          val values = vs.clone()
          val nnz = values.length
          var i = 0
          while (i < nnz) {
            values(i) = math.floor((values(i) - min_val) / (per_val + bias))
            i += 1
          }
          Vectors.sparse(size, ids, values)
        case v => throw new IllegalArgumentException("Do not support vector type " + v.getClass)
      }
    } else {
      // As the per_distance is zero, return the input vector object itself.
      // Note that it's safe since we always assume that the data in RDD should be immutable.
      vector
    }
  }
}
