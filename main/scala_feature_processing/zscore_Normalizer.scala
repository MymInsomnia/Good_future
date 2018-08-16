package main.scala_feature_processing

import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}
import main.tool_class.calculated_preparation.cal_mean_std


class zscore_Normalizer(unit: Unit) extends VectorTransformer  {



  /**
    * transform
    * Applies zscore normalization on a vector.
    *
    * @param vector input vector.
    * @return vector after zscore-normalized.
    * */

  override def transform(vector: Vector): Vector = {
    val (mean_val,std_val) = cal_mean_std(vector)
    if (std_val != 0.0) {
      // For dense vector, we've to allocate new memory for new output vector.
      // However, for sparse vector, the `index` array will not be changed,
      // so we can re-use it to save memory.
      vector match {
        case DenseVector(vs) =>
          val values = vs.clone()
          val size = values.length
          var i = 0
          while (i < size) {
            values(i) = (values(i)-mean_val) / std_val
            i += 1
          }
          Vectors.dense(values)
        case SparseVector(size, ids, vs) =>
          val values = vs.clone()
          val nnz = values.length
          var i = 0
          while (i < nnz) {
            values(i) = (values(i)-mean_val) / std_val
            i += 1
          }
          Vectors.sparse(size, ids, values)
        case v => throw new IllegalArgumentException("Do not support vector type " + v.getClass)
      }
    } else {
      // As the std is zero, return the input vector object itself.
      // Note that it's safe since we always assume that the data in RDD should be immutable.
      vector
    }

  }

}
