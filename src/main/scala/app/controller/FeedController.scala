package app.controller

import app.model.{Activity, Contents, ContentsType, Permission}
import cats.Monad
import cats.implicits._

trait FeedController[F[_]] {
  def allFeeds: F[List[Activity]]
  def getFeed(id: String): F[Option[Activity]]
}

object FeedController {

  val list: Map[String, Activity] = Map(
    "1" -> Activity("1", List(Contents(ContentsType.text, "1")), Permission.all),
    "2" -> Activity("2", List(Contents(ContentsType.text, "2")), Permission.all),
    "3" -> Activity("3", List(Contents(ContentsType.text, "3")), Permission.all),
    "4" -> Activity("4", List(Contents(ContentsType.image, "4")), Permission.all)
  )

  def apply[F[_]: Monad]: FeedController[F] = {

    new FeedController[F] {
      def allFeeds: F[List[Activity]] = list.values.toList.pure[F]
      def getFeed(id: String): F[Option[Activity]] = list.get(id).pure[F]
    }

  }
}
