package main.scala_feature_processing

import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}

class percentage_outier (bottom:Double, upper:Double) extends VectorTransformer{

  def this() = this(2)

  require(bottom<=1.0 , upper<=1.0)
  require(bottom>=0 , upper>=0)
  require(upper>bottom)

  /**
    * Computing the lower and upper bound value of the non-outier sample.
    * @param vector vector to be estimated.
    * @return the lower bound and upper bound value.
    */
  def cal_split_value(vector:Vector, b:Double, u:Double) = {

    val sorted_vec = vector.toArray.sortBy(identity).zipWithIndex.map{
      case (v,idx)=>(idx,v)
    }
    val count = sorted_vec.length
    val left:Int = math.floor(count*b).toInt
    val right:Int = math.floor(count*u).toInt

    (sorted_vec(left)._2,sorted_vec(right)._2)
  }

  /**
    * Judge if a value of the vector is an outier.
    * By computing if the percentage of a value if between the bottom and upper bound.
    * @param vector vector to be estimated.
    * @return vector only contains 0,1 . 1:outier 0:non-outier
    */
  override def transform(vector: Vector): Vector = {

    val (left,right) = cal_split_value(vector,bottom,upper)

    vector match {
      case DenseVector(vs) =>
        val values = vs.clone()
        val size = values.length
        var i = 0
        while (i < size) {
          values(i) = if(values(i) <= left || values(i) >= right) 1 else 0
          i += 1
        }
        Vectors.dense(values)
      case SparseVector(size, ids, vs) =>
        val values = vs.clone()
        val nnz = values.length
        var i = 0
        while (i < nnz) {
          values(i) = if(values(i)<=(left) || values(i)>=(right)) 1 else 0
          i += 1
        }
        Vectors.sparse(size, ids, values)
      case v => throw new IllegalArgumentException("Do not support vector type " + v.getClass)
    }
  }
}