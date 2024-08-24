package com.example.streams.generation;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class SparkSqlExample {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark-Sql-Example");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Dataset<Row> df = spark.read().json("food-delivery-data.json");
        df.printSchema();
        df.createOrReplaceTempView("food");
        Dataset<Row> res = spark.sql("select count(*) from food where personRating>4.5");
        res.write()
                .mode(SaveMode.Overwrite)
                .json("paytm-trip-info");
        Dataset<Row> res1=spark.sql("SELECT \n" +
                "    abs(unix_timestamp(concat(deliveryDate, ' ', deliveryTime), 'yyyy-MM-dd HH:mm:ss') - \n" +
                "     unix_timestamp(concat(orderDate, ' ', orderTime), 'yyyy-MM-dd HH:mm:ss')) AS timeTakenInSeconds\n" +
                "FROM \n" +
                "    food;\n");
        res1.write().mode(SaveMode.Append).json("paytm-trip-info");
    }
}
