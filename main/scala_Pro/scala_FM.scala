package scala_Pro

import breeze.linalg._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

//import org.apache.spark.ml.classification.LogisticRegression
object scala_FM {
  var max_list: ArrayBuffer[Double] = ArrayBuffer[Double]()
  var min_list: ArrayBuffer[Double] = ArrayBuffer[Double]()
  val trainData = "E:\\train.txt"
  val testData = "E:\\test.txt"


  def main(args: Array[String]): Unit = {
    val (dataTrain, labelTrain) = loadTrainDataSet(trainData)
    val (dataTest, labelTest) = loadTestDataSet(testData)

    println("开始训练!")
    val (w_0, w, v) = stocGradAscent(dataTrain, labelTrain.toArray, 20, 200)
    val train_ret = getAccuracy(dataTrain, labelTrain.toArray, w_0, w, v)
    println("训练准确率为：" + (1.0 - train_ret))
    println("开始测试!")
    val test_ret = getAccuracy(dataTest, labelTest.toArray, w_0, w, v)
    println("测试准确性为：" + (1.0 - test_ret))
    println("pass!")
  }

  def stocGradAscent(dataMatrix:DenseMatrix[Double] ,classLabels:Array[Int],k:Int,iter:Int)={
    //same
    //def sigmoid_func(intx:Double)=1.0/(1.0+exp(-1 * max(min(intx,15.0)),-15.0))
    //val sigmoid_func_2 = (intx:Double)=>1.0/(1.0+math.exp(-1 * max(min(intx,15.0),-15.0)))
    val sigmoid_func_2 =(intx:Double)=>1.0/(1.0+math.exp(-1 * intx))
    val (m,n) = (dataMatrix.rows,dataMatrix.cols)
    val alpha = 0.01
    var w_0:Double = 1
    val w:DenseVector[Double] = DenseVector.rand(n)
    val v:DenseMatrix[Double] = DenseMatrix.rand[Double](n,k)
    for(it <- 0 to iter){
      println("迭代到: 第 "+it+" 轮 ")
      println("w_0:"+w_0)
      println("w:"+w)
      println("v(1,::):"+v(1,::))
      for(x<- 0 until m){
        val inter_1 = dataMatrix(x,::) * v //访问某行

        var inter_2 = (dataMatrix(x,::):*dataMatrix(x,::))*(v:*v)
        var interaction = 0.5 * sum(inter_1:*inter_1:-inter_2)
        val pred:Double = w_0 + (dataMatrix(x,::) * w) + interaction

        val loss:Double = sigmoid_func_2(classLabels(x) * pred) - 1
        w_0 = w_0 - alpha * loss * classLabels(x)
        for(i<-0 until n){
          if(dataMatrix(x,i)!=0){
            //w(i) = w(i) - alpha*loss*classLabels(x)*dataMatrix(x,i)
            w.update(i,w(i) - alpha*loss*classLabels(x)*dataMatrix(x,i))
            for(j<-0 until k){
              v.update(i,j,v(i,j)-alpha * loss * classLabels(x) *(dataMatrix(x,i)*inter_1(j)-v(i,j)*dataMatrix(x,i)*dataMatrix(x,i)))
              //v(i,j) = v(i,j)-alpha * loss * classLabels(x) *(dataMatrix(x,i)*inter_1(j)-v(i,j)*dataMatrix(x,i)*dataMatrix(x,i))
            }
          }
        }
      }
    }
    (w_0,w,v)
  }
  def normalize(x_list:DenseVector[Double],max_list:ArrayBuffer[Double],min_list:ArrayBuffer[Double])={
    var index:Int = 0
    val normalize_func = (x_max:Double,x_min:Double,x:Double)=>((x-x_min)/(x_max-x_min)).toDouble
    val scalar_list = ArrayBuffer[Double]()
    for(x<-x_list){
      if(max_list(index)!=min_list(index))
        scalar_list += normalize_func(max_list(index),min_list(index),x)
      else
        scalar_list+=1.0
      index+=1
    }
    (scalar_list)
  }

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
    val ret_scalar_dataMat = new DenseMatrix(scalar_dataMat.length/8,8,scalar_dataMat.toArray)

    (ret_scalar_dataMat,labelMat)
  }

  def loadTestDataSet(data: String)={
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
    var scalar_dataMat= Array[Double]()
    for(i<-0 until data_array.rows){
      scalar_dataMat++=normalize(data_array(i,::).t,max_list,min_list)
    }
    val ret_scalar_dataMat = new DenseMatrix(scalar_dataMat.length/8,8,scalar_dataMat)

    (ret_scalar_dataMat,labelMat)
  }

  def getAccuracy(dataMatrix:DenseMatrix[Double],classLabels:Array[Int],w_0:Double,w:DenseVector[Double],v:DenseMatrix[Double])={
    val (m ,n) = (dataMatrix.rows,dataMatrix.cols)
    val sigmoid_func_2 =(intx:Double)=>(1.0/(1.0+math.exp(-1 * intx)))
     // (intx:Double)=>1.0/(1.0+math.exp(-1 * max(min(intx,15.0),-15.0)))
    var all_item = 0
    var error = 0
    var result = ArrayBuffer[Double]()
    for(x<-0 until m){
      all_item+=1
      val inter_1:Transpose[DenseVector[Double]] = dataMatrix(x,::) * v //访问某行
      val inter_2:Transpose[DenseVector[Double]] = (dataMatrix(x,::):*dataMatrix(x,::))*(v:*v)
      val interaction:Double = 0.5 * sum(inter_1:*inter_1:-inter_2)
      val pred:Double = sigmoid_func_2(w_0 + (dataMatrix(x,::) * w) + interaction)
      result += pred
      if(pred<0.5 && classLabels(x)==1)
        error+=1
      if(pred>=0.5 && classLabels(x)== -1)
        error+=1
    }
    println("Error:"+error)
    (error.toDouble/all_item.toDouble)
  }


}
