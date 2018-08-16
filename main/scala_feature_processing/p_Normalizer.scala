package main.scala_feature_processing

import breeze.linalg.support.TensorValues
import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}

/**
  * spark.DenseVector\SparseVector 等方法实现了unapply方法
  * 使其在match case时 可以进行类型判断
  * 写明了norm方法及transform方法
  * VectorTransformer方法包含对Vector、RDD、JavaRDD的transform方法
  * @param p
  */

class p_Normalizer(p:Double) extends VectorTransformer {

  def this() = this(2)

  require(p >= 1.0)

  /**
    * norm
    * Applies unit length normalization on a vector.
    *
    * @param vector input vector.
    * @param p norm.
    * @return norm in L^p^ space.
    * */
  def norm(vector: Vector, p: Double): Double = {
    if (p < 1.0) {
      println("To compute the p-norm of the vector, we require that you specify a p>=1")
      -1.0
    }

    val values = vector.toArray
    val size = values.length

    if (p == 1) {
      var sum = 0.0
      var i = 0
      while (i < size) {
        sum += math.abs(values(i))
        i += 1
      }
      sum
    } else if (p == 2) {
      var sum = 0.0
      var i = 0
      while (i < size) {
        sum += values(i) * values(i)
        i += 1
      }
      math.sqrt(sum)
    } else if (p == Double.PositiveInfinity) {
      var max = 0.0
      var i = 0
      while (i < size) {
        val value = math.abs(values(i))
        if (value > max) max = value
        i += 1
      }
      max
    } else {
      var sum = 0.0
      var i = 0
      while (i < size) {
        sum += math.pow(math.abs(values(i)), p)
        i += 1
      }
      math.pow(sum, 1.0 / p)
    }
  }

  /**
    * Applies normalization on a vector.
    *
    * @param vector vector to be normalized.
    * @return normalized vector. If the norm of the input is zero, it will return the input vector.
    */
  override def transform(vector: Vector): Vector = {

    val norm_val = norm(vector, p)
    if (norm_val != 0.0) {
      // For dense vector, we've to allocate new memory for new output vector.
      // However, for sparse vector, the `index` array will not be changed,
      // so we can re-use it to save memory.
      vector match {
        case DenseVector(vs) =>
          val values = vs.clone()
          val size = values.length
          var i = 0
          while (i < size) {
            values(i) /= norm_val
            i += 1
          }
          Vectors.dense(values)
        case SparseVector(size, ids, vs) =>
          val values = vs.clone()
          val nnz = values.length
          var i = 0
          while (i < nnz) {
            values(i) /= norm_val
            i += 1
          }
          Vectors.sparse(size, ids, values)
        case v => throw new IllegalArgumentException("Do not support vector type " + v.getClass)
      }
    } else {
      // As the norm is zero, return the input vector object itself.
      // Note that it's safe since we always assume that the data in RDD should be immutable.
      vector
    }

  }
}
