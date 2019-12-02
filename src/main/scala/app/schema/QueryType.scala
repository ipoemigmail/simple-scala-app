package app.schema

import app.service.FeedService
import cats.effect.implicits._
import cats.effect.{Effect, LiftIO}
import sangria.schema.{ObjectType, _}

object QueryType {
  import ActivityType._

  def apply[F[_]: Effect: LiftIO](
      implicit
      feedController: FeedService[F]
  ): ObjectType[Unit, Unit] = {

    ObjectType(
      name = "Query",
      fields = fields(
        Field(
          name = "feeds",
          fieldType = ListType(ActivityType),
          description = Some("Get Feeds"),
          arguments = Nil,
          resolve = _ => feedController.allFeeds.toIO.unsafeToFuture
        ),
        Field(
          name = "feed",
          fieldType = OptionType(ActivityType),
          description = Some("Get Feeds"),
          arguments = Argument("id", StringType) :: Nil,
          resolve = c => feedController.getFeed(c.arg(Argument("id", StringType))).toIO.unsafeToFuture
        )
      )
    )
  }
}
