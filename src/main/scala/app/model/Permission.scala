package app.model

import enumeratum._

import scala.collection.immutable

sealed abstract class Permission(override val entryName: String) extends EnumEntry

object Permission extends Enum[Permission] with CirceEnum[Permission] {
  final case object Me extends Permission("me")
  final case object Friend extends Permission("friend")
  final case object All extends Permission("all")

  def me: Permission = Me
  def friend: Permission = Friend
  def all: Permission = All

  val values: immutable.IndexedSeq[Permission] = findValues
}
