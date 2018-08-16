package main.scala_feature_processing

import breeze.linalg.{max, min}
import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}

class mad_outier (unit: Unit) extends VectorTransformer{

  /**
    * Calculate the mean_value,MAD value of the vector.
    *
    * @param vector vector to be calculated.
    * @return the mean_val and MAD_val of the vector.
    */

  def cal_mean_mad_median(vector: Vector) = {

    val sorted_vec = vector.toArray.sortBy(identity).zipWithIndex.map{
      case (v,idx)=>(idx,v)
    }
    val count = sorted_vec.length
    val median: Double = if (count % 2 == 0) {
      val l = count / 2 - 1
      val r = l + 1
      (sorted_vec(l)._2 + sorted_vec(r)._2) / 2
    } else sorted_vec(count / 2)._2.toDouble

    val mad_arr:Array[Double] = Array(count)
    var i = 0
    var sum_val:Double = 0.0
    for(a<-sorted_vec){
      mad_arr(i) = math.abs(a._2 - median)
      sum_val+=a._2
      i+=1
    }

    val sorted_mad = mad_arr.sortBy(identity).zipWithIndex.map{
      case (v,idx)=>(idx,v)
    }
    val mad_median: Double = if (sorted_mad.length % 2 == 0) {
      val l = count / 2 - 1
      val r = l + 1
      (sorted_mad(l)._2 + sorted_mad(r)._2) / 2
    } else sorted_mad(count / 2)._2.toDouble

    (sum_val/sorted_vec.length,mad_median)
  }

  /**
    * Judge if a value of the vector is an outier.
    * By computing if the value is between the mean_value-3*MAD and mean_value+3*MAD.
    * @param vector vector to be estimated.
    * @return vector only contains 0,1 . 1:outier 0:non-outier
    */
  override def transform(vector: Vector): Vector = {

    val (mean,mad_median) = cal_mean_mad_median(vector)
    if (mad_median != 0) {
      // For dense vector, we've to allocate new memory for new output vector.
      // However, for sparse vector, the `index` array will not be changed,
      // so we can re-use it to save memory.
      vector match {
        case DenseVector(vs) =>
          val values = vs.clone()
          val size = values.length
          var i = 0
          while (i < size) {
            values(i) = if(values(i)>mean+3*mad_median || values(i)<mean-3*mad_median) 1 else 0
            i += 1
          }
          Vectors.dense(values)
        case SparseVector(size, ids, vs) =>
          val values = vs.clone()
          val nnz = values.length
          var i = 0
          while (i < nnz) {
            values(i) = if(values(i)>mean+3*mad_median || values(i)<mean-3*mad_median) 1 else 0
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
