package app.service

import app.model.Activity
import app.repo.FeedRepo
import cats.Applicative

trait FeedService[F[_]] {
  def allFeeds: F[List[Activity]]
  def getFeed(id: String): F[Option[Activity]]
}

object FeedService {

  def apply[F[_]: Applicative](implicit feedRepo: FeedRepo[F]): FeedService[F] = {

    new FeedService[F] {
      def allFeeds: F[List[Activity]] = feedRepo.fetchAll
      def getFeed(id: String): F[Option[Activity]] = feedRepo.fetchById(id)
    }

  }
}
