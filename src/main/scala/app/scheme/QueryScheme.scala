package app.scheme

import app.controller.FeedController
import cats.effect.implicits._
import cats.effect.{Effect, LiftIO}
import sangria.schema.{ObjectType, _}

object QueryScheme {
  import ActivitySchemes._

  def apply[F[_]: Effect: LiftIO](
      implicit
      feedController: FeedController[F]
  ): ObjectType[Unit, Unit] = {

    ObjectType(
      name = "Query",
      fields = fields(
        Field(
          name = "feeds",
          fieldType = ListType(ActivityScheme),
          description = Some("Get Feeds"),
          arguments = Nil,
          resolve = _ => feedController.allFeeds.toIO.unsafeToFuture
        )
      )
    )
  }
}
