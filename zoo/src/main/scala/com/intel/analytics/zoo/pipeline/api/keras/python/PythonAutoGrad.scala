/*
 * Copyright 2018 Analytics Zoo Authors.
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

package com.intel.analytics.zoo.pipeline.api.keras.python

import java.util.{List => JList}

import com.intel.analytics.bigdl.nn.abstractnn.{AbstractModule, Activity, TensorCriterion}
import com.intel.analytics.bigdl.nn.keras.KerasLayer
import com.intel.analytics.bigdl.tensor.TensorNumericMath.TensorNumeric
import com.intel.analytics.bigdl.utils.Node
import com.intel.analytics.zoo.pipeline.api.autograd
import com.intel.analytics.zoo.pipeline.api.autograd._
import com.intel.analytics.zoo.pipeline.api.keras.layers.Input

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

object PythonAutoGrad {

  def ofFloat(): PythonAutoGrad[Float] = new PythonAutoGrad[Float]()

  def ofDouble(): PythonAutoGrad[Double] = new PythonAutoGrad[Double]()
}

class PythonAutoGrad[T: ClassTag](implicit ev: TensorNumeric[T]) extends PythonZooKeras[T] {

  def createZooKerasCustomLoss(inputs: JList[Variable[T]],
      loss: Variable[T]): TensorCriterion[T] = {
    new CustomLossWithVariable[T](inputs.asScala.toArray, loss)
  }

  def createZooKerasLambdaLayer(inputs: JList[Variable[T]],
      outVar: Variable[T],
      inputShape: JList[JList[Int]] = null): KerasLayer[Activity, Activity, T] = {
    LambdaLayer[T](inputs.asScala.toArray, outVar, toScalaMultiShape(inputShape))
  }

  def createZooKerasVariable(a: Node[AbstractModule[Activity, Activity, T]],
      name: String): Variable[T] = {
    new Variable[T](a, name)
  }


  def createZooKerasVariable(inputShape: JList[JList[Int]],
      name: String): Variable[T] = {
    new Variable[T](Input[T](toScalaMultiShape(inputShape), name), name)
  }

  def varGetInputShape(v: Variable[T]): JList[JList[Int]] = {
    shapeToJList(v.getInputShape())
  }

  def varGetOutputShape(v: Variable[T]): JList[JList[Int]] = {
    shapeToJList(v.getOutputShape())
  }

  def add(a: Variable[T], b: Variable[T]): Variable[T] = {
    a + b
  }

  def add(a: Variable[T], b: Double): Variable[T] = {
    a + b
  }

  def add(a: Double, b: Variable[T]): Variable[T] = {
    b + a
  }

  def sub(a: Variable[T], b: Variable[T]): Variable[T] = {
    a - b
  }

  def sub(a: Variable[T], b: Double): Variable[T] = {
    a - b
  }

  def sub(a: Double, b: Variable[T]): Variable[T] = {
    -b + a
  }

  def mul(a: Variable[T], b: Variable[T]): Variable[T] = {
    a * b
  }

  def mul(a: Variable[T], b: Double): Variable[T] = {
    a * b
  }

  def mul(a: Double, b: Variable[T]): Variable[T] = {
    b * a
  }

  def div(a: Variable[T], b: Variable[T]): Variable[T] = {
    a / b
  }

  def div(a: Variable[T], b: Double): Variable[T] = {
    a / b
  }

  def div(a: Double, b: Variable[T]): Variable[T] = {
    autograd.AutoGrad.pow(b, -1.0) * a
  }

  def abs(a: Variable[T]): Variable[T] = {
    autograd.AutoGrad.abs(a)
  }

  def sum(a: Variable[T], axis: Int = 0, keepdims: Boolean = false): Variable[T] = {
    autograd.AutoGrad.sum(a, axis, keepdims)
  }

  def clip(a: Variable[T], min: Double, max: Double): Variable[T] = {
    autograd.AutoGrad.clip(a, min, max)
  }

  def square(a: Variable[T]): Variable[T] = {
    autograd.AutoGrad.square(a)
  }

  def sqrt(a: Variable[T]): Variable[T] = {
    autograd.AutoGrad.sqrt(a)
  }

  def exp(a: Variable[T]): Variable[T] = {
    autograd.AutoGrad.exp(a)
  }

  def maximum(a: Variable[T], b: Variable[T]): Variable[T] = {
    autograd.AutoGrad.maximum(a, b)
  }

  def maximum(a: Variable[T], b: Double): Variable[T] = {
    autograd.AutoGrad.maximum(a, b)
  }

  def mean(a: Variable[T], axis: Int = 0, keepDims: Boolean = false): Variable[T] = {
    autograd.AutoGrad.mean(a, axis, keepDims)
  }

  def log(a: Variable[T]): Variable[T] = {
    autograd.AutoGrad.log(a)
  }

  def epsilon(): Double = {
    autograd.AutoGrad.epsilon()
  }

  def squeeze(a: Variable[T], dim: Int): Variable[T] = {
    a.squeeze(dim)
  }

  def slice(a: Variable[T], dim: Int, startIndex: Int, length: Int): Variable[T] = {
    a.slice(dim, startIndex, length)
  }

  def indexSelect(a: Variable[T], dim: Int, index: Int): Variable[T] = {
    a.indexSelect(dim, index)
  }

  def neg(a: Variable[T]): Variable[T] = {
    a.unary_-()
  }

  def pow(a: Variable[T], b: Double): Variable[T] = {
    autograd.AutoGrad.pow(a, b)
  }

  def softsign(a: Variable[T]): Variable[T] = {
    autograd.AutoGrad.softsign(a)
  }

  def softplus(a: Variable[T]): Variable[T] = {
    autograd.AutoGrad.softplus(a)
  }

  def expandDims(a: Variable[T], axis: Int): Variable[T] = {
    autograd.AutoGrad.expandDims(a, axis)
  }

  def stack(inputs: JList[Variable[T]], axis: Int): Variable[T] = {
    autograd.AutoGrad.stack(inputs.asScala.toList, axis)
  }

  def contiguous(input: Variable[T]): Variable[T] = {
    autograd.AutoGrad.contiguous(input)
  }

  def mm(x: Variable[T], y: Variable[T], axes: JList[Int]): Variable[T] = {
    autograd.AutoGrad.mm(x, y, axes.asScala.toList)
  }

  def l2Normalize(x: Variable[T], axis: Int): Variable[T] = {
    autograd.AutoGrad.l2Normalize(x, axis)
  }

  def batchDot(x: Variable[T], y: Variable[T], axes: JList[Int],
          normalize: Boolean = false): Variable[T] = {
    autograd.AutoGrad.batchDot(x, y, axes.asScala.toList, normalize)
  }
}
