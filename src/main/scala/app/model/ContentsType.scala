package app.model

import enumeratum._

import scala.collection.immutable

sealed abstract class ContentsType(override val entryName: String) extends EnumEntry

object ContentsType extends Enum[ContentsType] with CirceEnum[ContentsType] {
  case object Text extends ContentsType("text")
  case object Image extends ContentsType("image")
  case object Video extends ContentsType("video")
  case object Link extends ContentsType("link")

  def text: ContentsType = Text
  def image: ContentsType = Image
  def video: ContentsType = Video
  def link: ContentsType = Link

  val values: immutable.IndexedSeq[ContentsType] = findValues
}
