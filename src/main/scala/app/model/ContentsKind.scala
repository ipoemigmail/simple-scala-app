package app.model

import enumeratum._

import scala.collection.immutable

sealed abstract class ContentsKind(override val entryName: String) extends EnumEntry

object ContentsKind extends Enum[ContentsKind] with CirceEnum[ContentsKind] {
  case object Text extends ContentsKind("text")
  case object Image extends ContentsKind("image")
  case object Video extends ContentsKind("video")
  case object Link extends ContentsKind("link")

  def text: ContentsKind = Text
  def image: ContentsKind = Image
  def video: ContentsKind = Video
  def link: ContentsKind = Link

  val values: immutable.IndexedSeq[ContentsKind] = findValues
}
