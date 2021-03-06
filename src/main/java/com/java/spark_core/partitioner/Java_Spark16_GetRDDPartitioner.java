package com.java.spark_core.partitioner;

import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Lei
 * @E-mail: 843291011@qq.com
 * @Date: Created in 9:04 下午 2020/3/2
 * @Version: 1.0
 * @Modified By:
 * @Description: GetRDDPartitioner
 */

/**
 * Spark目前支持Hash分区和Range分区，用户也可以自定义分区，Hash分区为当前的默认分区，Spark中分区器直接决定了RDD中分区的个数、RDD中每条数据经过Shuffle过程属于哪个分区和Reduce的个数
 * 注意：
 * (1)只有Key-Value类型的RDD才有分区器的，非Key-Value类型的RDD分区器的值是None
 * (2)每个RDD的分区ID范围：0~numPartitions-1，决定这个值是属于那个分区的。
 * <p>
 * 获取RDD分区
 * 可以通过使用RDD的partitioner 属性来获取 RDD 的分区方式。它会返回一个 scala.Option 对象， 通过get方法获取其中的值。
 */

public class Java_Spark16_GetRDDPartitioner {
    public static void main(String[] args) {
        // 1.初始化spark配置信息并建立与spark的连接
        SparkConf config = new SparkConf().setMaster("local[*]").setAppName("Spark16_GetRDDPartitioner");
        JavaSparkContext sc = new JavaSparkContext(config);


        // （1）创建一个pairRDD
        // val pairs = sc.parallelize(List((1, 1), (2, 2), (3, 3)))
        List<Tuple2<Integer, Integer>> list = new ArrayList<>();
        list.add(new Tuple2<Integer, Integer>(1, 1));
        list.add(new Tuple2<Integer, Integer>(2, 2));
        list.add(new Tuple2<Integer, Integer>(3, 3));
        JavaPairRDD<Integer, Integer> pairs = sc.parallelizePairs(list);

        // （2）查看RDD的分区器
        //println(pairs.partitioner.toString)
        System.out.println(pairs.partitioner().toString());

        System.out.println("---------------------上述查看默认分区器，下述查看HashPartitioner分区器-------------------");
        // （3）导入HashPartitioner类
        // import org.apache.spark.HashPartitioner
        // import org.apache.spark.HashPartitioner;

        // （4）使用HashPartitioner对RDD进行重新分区
        // val partitioned = pairs.partitionBy(new HashPartitioner(2))
        JavaPairRDD<Integer, Integer> partitioned = pairs.partitionBy(new HashPartitioner(2));

        // （5）查看重新分区后RDD的分区器
        // println(partitioned.partitioner.toString)
        System.out.println(partitioned.partitioner().toString());
    }
}
