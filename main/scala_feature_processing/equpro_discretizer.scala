package main.scala_feature_processing

import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vector, Vectors}
import main.tool_class.calculated_preparation.discretizer_able

class equpro_discretizer (d:Int) extends VectorTransformer {

  def this() = this(2)

  require(d > 1)

  /**
    * Calculate the split point of the vector in d pieces.
    * e.g discretize (1,2,3,4,5,6) into 2 pieces,the return result would be (4)
    * @param vector vector to be calculated.
    * @param d divide the vector into d pieces.
    * @return the split point list with length of (d-1).
    */

  def cal_array(vector: Vector, d: Int) = {

    val sorted_vec = vector.toArray.sortBy(identity).zipWithIndex.map{
      case (v,idx)=>(idx,v)
    }
    val count = sorted_vec.size
    val step:Double = count/d
    val ret_arr : Array[Double] = new Array(d-1)
    var i:Double = step
    var j:Double = 0
    while(i<count){
      ret_arr(j.toInt) = sorted_vec(math.floor(i).toInt)._2
      i+=step
      j+=1
    }
    ret_arr
  }

  /**
    * By check the split point list(split_arr),the function applies discretization on a vector,
    * following the rule of [,).
    * e.g discretize (1,2,3,4,5,6) into 2 pieces,the return result would be (0,0,0,1,1,1)
    *
    * @param vector vector to be discretize.
    * @return discretized vector. If d larger than the vector length, it will return the input vector.
    */
  override def transform(vector: Vector): Vector = {
    if(discretizer_able(vector,d)){
      var split_arr = cal_array(vector, d)

      vector match {
        case DenseVector(vs) =>
          val values = vs.clone()
          val size = values.length
          split_arr = split_arr.:+(Double.MaxValue)
          var i = 0
          while (i < size) {
            var j = 0
            while((j < split_arr.length)&&(values(i) > split_arr(j) )){
              j += 1
            }
            values(i) = j
            i += 1
          }
          Vectors.dense(values)
        case SparseVector(size, ids, vs) =>
          val values = vs.clone()
          val size = values.length
          split_arr = split_arr.:+(Double.MaxValue)
          var i = 0
          while (i < size) {
            var j = 0
            while((j < split_arr.size)&&(values(i) > split_arr(j) )){
              j += 1
            }
            values(i) = j
            i += 1
          }
          Vectors.sparse(size, ids, values)
        case v => throw new IllegalArgumentException("Do not support vector type " + v.getClass)
      }
    }
    else{
      // As the split number larger than vector length, return the input vector object itself.
      // Note that it's safe since we always assume that the data in RDD should be immutable.
      vector
    }
  }
}
