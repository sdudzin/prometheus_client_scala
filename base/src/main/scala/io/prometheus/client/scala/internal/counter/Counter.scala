package io.prometheus.client.scala.internal.counter

import java.util.concurrent.atomic.DoubleAdder

import io.prometheus.client.scala._

/** This represents a Prometheus counter with no labels.
  *
  * A Prometheus counter should be used for values which only increase in value.
  *
  * @param name The name of the counter
  * @tparam N The singleton type for the counter's name
  */
final class Counter0[N <: String](val name: N)() extends Collector[N] {
  private[scala] val adder = new DoubleAdder

  def incBy(v: Double): Unit = {
    assert(v >= 0)
    adder.add(v)
  }

  def inc(): Unit = adder.add(1d)

  override def collect(): List[RegistryMetric] =
    RegistryMetric(name, List.empty, adder.sum()) :: Nil

  override def toString(): String =
    s"Counter0($name)()"
}

/** This represents a Prometheus counter with 1 label.
  *
  * A Prometheus .counter should be used for values which only increase in value.
  *
  * @param name The name of the counter
  * @tparam N The singleton type for the counter's name
  * @tparam L1 The singleton string type for label 1
  */
final class Counter1[N <: String, L1 <: String](val name: N)(val label: String) extends Collector[N] {
  private[scala] val adders = new Adders[String](None)

  def incBy(l1: String)(v: Double): Unit = {
    assert(v >= 0)
    adders(l1).add(v)
  }

  def inc(l1: String): Unit =
    adders(l1).add(1d)

  def collect(): List[RegistryMetric] =
    adders.getAll.map({
      case (labelValue, value) => RegistryMetric(name, List(label -> labelValue), value)}
    )

  override def toString(): String =
    s"Counter1($name)($label)"
}
