package app.repo

import app.model.{Activity, Contents, ContentsKind, Permission}
import cats._, cats.implicits._

trait FeedRepo[F[_]] {
  def fetchById(id: String): F[Option[Activity]]
  def fetchAll: F[List[Activity]]
}

object FeedRepo {

  private val list: Map[String, Activity] = Map(
    "1" -> Activity("1", List(Contents(ContentsKind.text, "1")), Permission.all),
    "2" -> Activity("2", List(Contents(ContentsKind.text, "2")), Permission.all),
    "3" -> Activity("3", List(Contents(ContentsKind.text, "3")), Permission.all),
    "4" -> Activity("4", List(Contents(ContentsKind.image, "4")), Permission.all)
  )

  def apply[F[_]: Applicative]: FeedRepo[F] = new FeedRepo[F] {
    def fetchById(id: String): F[Option[Activity]] = list.get(id).pure[F]
    def fetchAll: F[List[Activity]] = list.values.toList.pure[F]
  }
}