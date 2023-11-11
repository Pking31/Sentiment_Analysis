package com.polyval.syntax

import com.polyval.syntax.row.*

import org.apache.commons.csv.CSVRecord

trait RowDecoder[A] extends Serializable {
  def decode(row: CSVRecord): Option[A]
}

object RowDecoder {
  def apply[A](implicit ev: RowDecoder[A]): RowDecoder[A] = implicitly[RowDecoder[A]]

  def instance[A](f: CSVRecord => Option[A]): RowDecoder[A] = new RowDecoder[A] {
    def decode(row: CSVRecord): Option[A] = f(row)
  }

  implicit def intRowDecode(cell: Int): RowDecoder[Int] = RowDecoder.instance(_.fromInt(cell))

  implicit def doubleRowDecode(cell: Int): RowDecoder[Double] = RowDecoder.instance(_.fromDouble(cell))

  implicit def stringRowDecode(cell: Int): RowDecoder[String] = RowDecoder.instance(_.fromString(cell))

  implicit def booleanRowDecode(cell: Int): RowDecoder[Boolean] = RowDecoder.instance(_.fromBoolean(cell))

  implicit def longRowDecode(cell: Int): RowDecoder[Long] = RowDecoder.instance(_.fromLong(cell))

  implicit def timestampRowDecode(cell: Int): RowDecoder[java.time.ZonedDateTime] =
    RowDecoder.instance(_.fromTimestamp(cell))

  implicit def optionRowDecode[A](implicit ev: RowDecoder[A]): RowDecoder[Option[A]] =
    RowDecoder.instance(_.fromOption(ev))

}
