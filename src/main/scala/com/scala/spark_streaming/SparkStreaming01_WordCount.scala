package com.scala.spark_streaming

import org.apache.spark.SparkConf
import org.apache.spark.deploy.SparkSubmit
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @Author: Lei
 * @E-mail: 843291011@qq.com
 * @Date: Created in 11:38 下午 2020/3/10
 * @Version: 1.0
 * @Modified By:
 * @Description: sparkStreaming版的wordCount
 */

/*
  添加pom.xml文件依赖
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-streaming_2.11</artifactId>
            <version>2.1.1</version>
        </dependency>
 */
object SparkStreaming01_WordCount {
  def main(args: Array[String]): Unit = {

    //使用SparkStreaming 完成WordCount
    SparkSubmit
    //SparkSQL
    val config: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming01_WordCount")

    var streamingContext: StreamingContext = new StreamingContext(config, Seconds(3)) //3 秒钟，伴生对象，不需要new

    //从指定的端口中采集数据
    var socketLineStreaming: ReceiverInputDStream[String] = streamingContext.socketTextStream("leizuquandeMacBook-Air.local", 9999) //一行一行的接受

    //将采集的数据进行分解（偏平化）
    var WordDstream: DStream[String] = socketLineStreaming.flatMap(line => line.split(" ")) //偏平化后，按照空格分割

    //将我们的数据进行转换方便分析
    var mapDstream: DStream[(String, Int)] = WordDstream.map((_, 1))

    //将转换后的数据聚合在一起处理
    var wordToSumStream: DStream[(String, Int)] = mapDstream.reduceByKey(_ + _)

    //打印结果
    wordToSumStream.print()

    //streamingContext.stop()  //不能停止我们的采集功能

    //启动采集器
    streamingContext.start()

    //Drvier等待采集器停止，
    streamingContext.awaitTermination()

    //nc -lc 9999   linux 下往9999端口发数据。

  }

}
