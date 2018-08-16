package main.scala_feature_processing

import org.apache.spark.annotation.{DeveloperApi, Experimental}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan


object CustomDF extends DataFrame(sqlContext,logicalPlan){
  def apply(sqlContext: SQLContext, logicalPlan: LogicalPlan): DataFrame = {
    new DataFrame(sqlContext, logicalPlan)
  }
}

@Experimental
class CustomDF extends DataFrame{

}