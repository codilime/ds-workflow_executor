/**
 * Copyright 2015, deepsense.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.deepsense.deeplang.doperables.dataframe

import java.sql.Timestamp

import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.{Vectors, Vector, VectorUDT}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.joda.time.DateTime

import io.deepsense.commons.datetime.DateTimeConverter
import io.deepsense.commons.types.ColumnType
import io.deepsense.deeplang.doperables.dataframe.types.SparkConversions

trait DataFrameTestFactory {

  def testDataFrame(dataFrameBuilder: DataFrameBuilder, sparkContext: SparkContext): DataFrame =
    dataFrameBuilder.buildDataFrame(
      testSchema,
      testRDD(sparkContext),
      Seq(DataFrameTestFactory.categoricalColumnName))

  def oneValueDataFrame(
      dataFrameBuilder: DataFrameBuilder,
      sparkContext: SparkContext): DataFrame =
    dataFrameBuilder.buildDataFrame(
      testSchema,
      sameValueRDD(sparkContext),
      Seq(DataFrameTestFactory.categoricalColumnName))

  val testSchema: StructType = StructType(Array(
    StructField(DataFrameTestFactory.stringColumnName, StringType),
    StructField(DataFrameTestFactory.booleanColumnName, BooleanType),
    StructField(DataFrameTestFactory.doubleColumnName, DoubleType),
    StructField(DataFrameTestFactory.timestampColumnName, TimestampType),
    StructField(DataFrameTestFactory.categoricalColumnName, StringType),
    StructField(DataFrameTestFactory.vectorColumnName,
      SparkConversions.columnTypeToSparkColumnType(ColumnType.vector))
  ))

  def testRDD(sparkContext: SparkContext): RDD[Row] = sparkContext.parallelize(Seq(
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name2", false, 1.95, timestamp(1990, 2, 11, 0, 43), "summer", Vectors.dense(3.0, 4.0)),
    Row("Name3", false, 1.87, timestamp(1999, 7, 2, 0, 43), "winter", Vectors.dense(5.0, 6.0)),
    Row("Name4", false, 1.7, timestamp(1954, 12, 18, 0, 43), "spring", Vectors.dense(7.0, 8.0)),
    Row("Name5", false, 2.07, timestamp(1987, 4, 27, 0, 43), null, Vectors.dense(9.0, 0.0)),
    Row(null, true, 1.307, timestamp(2010, 1, 7, 0, 0), "autumn", Vectors.dense(0.1, 0.2)),
    Row("Name7", null, 2.132, timestamp(2000, 4, 27, 0, 43), "summer", null),
    Row("Name8", true, 1.777, timestamp(1996, 10, 24, 0, 43), "summer", Vectors.dense(0.3, 0.4)),
    Row("Name9", true, null, timestamp(1999, 1, 6, 0, 0), "spring", Vectors.dense(0.5, 0.6)),
    Row("Name10", true, 1.99, null, "summer", Vectors.dense(0.7, 0.8))
  ))

  def sameValueRDD(sparkContext: SparkContext): RDD[Row] = sparkContext.parallelize(Seq(
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0)),
    Row("Name1", false, 1.67, timestamp(1970, 1, 20, 0, 43), "summer", Vectors.dense(1.0, 2.0))
  ))

  private def timestamp(
      year: Int,
      month: Int,
      day: Int,
      hour: Int,
      minutes: Int): Timestamp =
    new Timestamp(new DateTime(year, month, day, hour, minutes, DateTimeConverter.zone).getMillis)
}

object DataFrameTestFactory extends DataFrameTestFactory {
  val stringColumnName = "Name"
  val booleanColumnName = "BusinessAccount"
  val doubleColumnName = "Whatever"
  val timestampColumnName = "AccountCreationDate"
  val categoricalColumnName = "Season"
  val vectorColumnName = "Vector"
}
