package main.scala_feature_processing_lib

import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, UnaryNode}

case class Sample(
                   lowerBound: Double,
                   upperBound: Double,
                   withReplacement: Boolean,
                   seed: Long,
                   child: LogicalPlan) extends UnaryNode {

  override def output: Seq[Attribute] = child.output
}
