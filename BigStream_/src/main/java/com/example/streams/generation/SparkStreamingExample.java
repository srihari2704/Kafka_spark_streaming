package com.example.streams.generation;

import org.apache.spark.*;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.util.Arrays;

public class SparkStreamingExample{
    public static void main(String [] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Spark-Streaming-Example");
        JavaSparkContext sc =new JavaSparkContext(conf);
        JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(10));
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9090);
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());

        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);
        wordCounts.print();
        jssc.start();              // Start the computation
        jssc.awaitTermination();
    }
}
// Create a local StreamingContext with two working thread and batch interval of 1 second
