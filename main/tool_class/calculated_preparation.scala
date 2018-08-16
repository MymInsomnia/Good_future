package main.tool_class

import org.apache.spark.mllib.linalg.Vector

object calculated_preparation {

  /**
    * estimate if the vector could be discretized into d pieces.
    * if the number of distinct values smaller than d, the discretization cannot be applied.
    * @param vector vector to be calculated.
    * @param d discretize the vector into d pieces.
    * @return Boolean true:could be discretized.
    */

  def discretizer_able(vector: Vector,d:Int): Boolean ={
    val unique_arr = vector.toArray.distinct
    if(unique_arr.length < d)
      false
    else
      true
  }

  /**
    * Calculate the std value of the vector.
    *
    * @param vector vector to be calculated.
    * @return the mean-value,std-value of the vector.
    */

  def cal_mean_std(vector: Vector): (Double,Double) = {

    val values = vector.toArray
    val size = values.length
    var i = 0
    var sum = 0.0
    var sum_var_val = 0.0
    while(i < size){
      sum += values(i)
      i += 1
    }
    val mean_val = sum/size
    i = 0
    while(i < size){
      sum_var_val += math.pow(values(i) - mean_val,2.0)
      i += 1
    }
    val std_val = math.pow(sum_var_val/size,0.5)
    (mean_val,std_val)
  }

}
