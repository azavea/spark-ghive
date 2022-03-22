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

package com.azavea.hiveless.spatial.index

import com.azavea.hiveless.HUDF
import com.azavea.hiveless.spatial._
import geotrellis.layer.{SpatialKey, ZoomedLayoutScheme}
import geotrellis.vector._
import geotrellis.proj4.{CRS, LatLng}
import geotrellis.store.index.zcurve.Z2
import org.locationtech.jts.geom.Geometry

class ST_PartitionCentroid extends HUDF[(Geometry, Int, Option[CRS], Option[Int], Option[Double], Option[Int]), Long] {
  val name: String = "st_partitionCentroid"
  def function = {
    case (geom: Geometry, zoom: Int, crsOpt: Option[CRS], tileSizeOpt: Option[Int], resolutionThresholdOpt: Option[Double], bitsOpt: Option[Int]) =>
      // set default values
      val crs                 = crsOpt.getOrElse(LatLng)
      val tileSize            = tileSizeOpt.getOrElse(ZoomedLayoutScheme.DEFAULT_TILE_SIZE)
      val resolutionThreshold = resolutionThresholdOpt.getOrElse(ZoomedLayoutScheme.DEFAULT_RESOLUTION_THRESHOLD)
      val bits                = bitsOpt.getOrElse(8)

      // compute key
      val SpatialKey(col, row) = new ZoomedLayoutScheme(crs, tileSize, resolutionThreshold)
        .levelForZoom(zoom)
        .layout
        .mapTransform(geom.extent.center)

      Z2(col, row).z >> bits
  }
}