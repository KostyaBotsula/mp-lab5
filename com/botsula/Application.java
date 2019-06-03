package com.botsula;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Objects;

import static org.apache.spark.sql.functions.*;

public class Application {
    private static final String COUNT = "count";

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Expected hdfs host name and logs dir path");
        }

        SparkSession session = SparkSession.builder().master("local").appName("lab5").getOrCreate();

        JavaRDD<LogUtil> data = session.read().textFile("hdfs://" + args[0] + ":9000" + args[1] + "/NASA_access_log_Jul95")
                .javaRDD()
                .map(LogUtil::parseLogLine)
                .filter(Objects::nonNull);

        Dataset<Row> dataSet = session.createDataFrame(data, LogUtil.class);

        taskOne(dataSet, "hdfs://" + args[0] + ":9000/task1");
        taskTwo(dataSet, "hdfs://" + args[0] + ":9000/task2");
        taskThree(dataSet, "hdfs://" + args[0] + ":9000/task3");
    }

    private static void taskOne(Dataset<Row> dataSet, String outDir) {
        dataSet.filter(col(LogUtil.RESPONSE_CODE).between(500, 599))
                .groupBy(LogUtil.ENDPOINT)
                .count()
                .select(LogUtil.ENDPOINT, COUNT)
                .coalesce(1)
                .toJavaRDD()
                .saveAsTextFile(outDir);
    }

    private static void taskTwo(Dataset<Row> dataSet, String outDir) {
        dataSet.groupBy(LogUtil.METHOD, LogUtil.RESPONSE_CODE, LogUtil.DATE_STRING)
                .count()
                .filter(col(COUNT).geq(10))
                .select(LogUtil.DATE_STRING, LogUtil.METHOD, LogUtil.RESPONSE_CODE, COUNT)
                .sort(LogUtil.DATE_STRING, LogUtil.METHOD, LogUtil.RESPONSE_CODE)
                .coalesce(1)
                .toJavaRDD()
                .saveAsTextFile(outDir);
    }

    private static void taskThree(Dataset<Row> dataSet, String outDir) {
        dataSet.filter(col(LogUtil.RESPONSE_CODE).between(400, 599))
                .groupBy(window(to_date(col(LogUtil.DATE_STRING), LogUtil.OUT_DATE_FORMAT), "1 week", "1 day"))
                .count()
                .select(date_format(col("window.start"), LogUtil.OUT_DATE_FORMAT),
                        date_format(col("window.end"), LogUtil.OUT_DATE_FORMAT),
                        col(COUNT))
                .coalesce(1)
                .toJavaRDD()
                .saveAsTextFile(outDir);
    }
}