/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package main.scala_feature_processing_lib

import main.scala_feature_processing.p_Normalizer
import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.param.{DoubleParam, ParamValidators}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.mllib.feature
import org.apache.spark.mllib.linalg.{Vector, VectorUDT}
import org.apache.spark.sql.types.DataType


class P_Normalizer(override val uid: String) extends UnaryTransformer[Vector, Vector, P_Normalizer] {

  def this() = this(Identifiable.randomUID("normalizer"))

  /**
    * Normalization in L^p^ space.  Must be >= 1.
    * (default: p = 2)
    */

  val p = new DoubleParam(this, "p", "the p norm value", ParamValidators.gtEq(1))
  //DoubleParam对Double做了封装，因为val不可修改
  setDefault(p -> 2.0)

  def getP: Double = $(p)

  def setP(value: Double): this.type = set(p, value)

  override protected def createTransformFunc: Vector => Vector = {
    val normalizer = new p_Normalizer($(p))
    normalizer.transform
  }

  override protected def outputDataType: DataType = new VectorUDT()
}
