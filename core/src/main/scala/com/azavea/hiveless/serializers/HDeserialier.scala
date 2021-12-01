/*
 * Copyright 2021 Azavea
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

package com.azavea.hiveless.serializers

import org.apache.hadoop.hive.ql.udf.generic.GenericUDF
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector

trait HDeserialier[F[_], T] extends Serializable {
  def deserialize(arguments: Array[GenericUDF.DeferredObject], inspectors: Array[ObjectInspector]): F[T]

  def deserialize(argument: GenericUDF.DeferredObject, inspector: ObjectInspector): F[T] =
    deserialize(Array(argument), Array(inspector))
}
