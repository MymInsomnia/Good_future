package scala_Pro

import java.util.Random

import breeze.linalg.{DenseMatrix, DenseVector, Vector, max, min}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.math.exp
import scala_FM.{normalize}


object scala_LR {
  var max_list: ArrayBuffer[Double] = ArrayBuffer[Double]()
  var min_list: ArrayBuffer[Double] = ArrayBuffer[Double]()
  val N = 10000  // Number of data points
  val D = 8   // Numer of dimensions
  val R = 0.7  // Scaling factor
  val learning_rate = 0.01
  val ITERATIONS = 200
  val rand = new Random(42)
  val shold = 0.5

  case class DataPoint(x: breeze.linalg.Vector[Double], y: Double)

  def loadTrainDataSet(data: String)={
    var dataFlatMat: Array[Double] = Array[Double]() //二维还是一维后面再转化
    var labelMat: ArrayBuffer[Int] = ArrayBuffer[Int]()
    val file = Source.fromFile(data)
    for(line <- file.getLines())
    {
      val currLine: Array[Double] = line.split(",").map(key=>key.toDouble)
      labelMat += (currLine.last*2-1).toInt
      dataFlatMat ++= currLine.dropRight(1)

    }
    file.close
    val data_array: DenseMatrix[Double] = new DenseMatrix(dataFlatMat.length/8,8,dataFlatMat)
    println(data_array)



    for(i <- 0 until data_array.cols){
      max_list += max(data_array(::,i))
      min_list += min(data_array(::,i))
    }
    var scalar_dataMat = ArrayBuffer[Double]()
    for(i<-0 until data_array.rows){
      scalar_dataMat++=normalize(data_array(i,::).t,max_list,min_list)
    }

    val ret_scalar_dataMat = new DenseMatrix(scalar_dataMat.length/D,D,scalar_dataMat.toArray)
    var ret = Array[DataPoint]()
    for(i<-0 until ret_scalar_dataMat.rows){
      val x = ret_scalar_dataMat(i,::).t
      val y = labelMat(i)
      ret:+=DataPoint(x, y)
    }
    ret
  }

  def get_acc(data:Array[DataPoint],w:DenseVector[Double])={
    val sigmoid_func_2 =(intx:Double)=>(1.0/(1.0+math.exp(-1 * intx)))
    val ret_pred_error = (p:DataPoint)=>{
      if(sigmoid_func_2(w.dot(p.x))>shold && p.y==(-1))
        1.0
      else if(sigmoid_func_2(w.dot(p.x))<shold && p.y==(1))
        1.0
      else
        0.0
    }
    val error = data.map(ret_pred_error).reduce(_ + _)
    error
  }

  def main(args: Array[String]): Unit = {
    val trainData = "E:\\train.txt"
    val testData = "E:\\test.txt"

    val train_points = loadTrainDataSet(trainData)
    val test_points = loadTrainDataSet(testData)
    var w = DenseVector.fill(D){2 * rand.nextDouble() - 1}
    println("Initial w: " + w)
    for (i <- 1 to ITERATIONS) {
      println("On iteration " + i)
      val gradient = train_points.map { p =>
        p.x * (1 / (1 + exp(-p.y * (w.dot(p.x)))) - 1) * p.y //没毛病 alpha*loss*label*x
      }.reduce(_ + _)
      w -= learning_rate*gradient
      println("G:"+gradient)
      println("W:"+w)
    }
    println("Final w: " + w)

    val train_error = get_acc(train_points,w)
    val test_error = get_acc(test_points,w)
    println("Train_error: "+train_error)
    println("Train acc rate: "+( 1 - train_error/train_points.length))
    println("Test_error: "+test_error)
    println("Test acc rate: "+ (1 - test_error/test_points.length))
  }
}
