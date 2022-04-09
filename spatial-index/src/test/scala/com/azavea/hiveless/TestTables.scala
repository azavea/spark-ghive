/*
 * Copyright 2022 Azavea
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

package com.azavea.hiveless

import java.io.File

trait TestTables { self: HiveTestEnvironment =>
  // core/src/test/resources/polygons.csv when JVM is not forked
  val uriCSV     = new File("src/test/resources/polygons.csv").toURI.toString
  val uriParquet = new File("src/test/resources/polygons.snappy.parquet").toURI.toString

  // create tmp views
  ssc.read
    .option("delimiter", ",")
    .option("header", "true")
    .csv(uriCSV)
    .createOrReplaceTempView("polygons_csv")

  ssc.read
    .parquet(uriParquet)
    .createOrReplaceTempView("polygons_parquet")

  // create view with a parsed geometry and bbox columns
  ssc.sql(
    """
      |CREATE TEMPORARY VIEW polygons_csv_view AS (
      |  SELECT *, ST_GeomFromWKT(wkt) AS geom, ST_ExtentFromGeom(ST_GeomFromWKT(wkt)) as bbox FROM polygons_csv
      |);
      |""".stripMargin
  )

  // Parquet generation
  /*ssc.sql("DROP TABLE polygons_parquet;")
  ssc.sql(
    """
      |CREATE TABLE polygons_parquet
      |USING PARQUET LOCATION '/tmp/polygons_parquet'
      |AS (SELECT * FROM polygons_csv_view);
      |""".stripMargin
  )*/
}
