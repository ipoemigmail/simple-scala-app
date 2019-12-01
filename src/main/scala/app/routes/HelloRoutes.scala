package app.routes

import cats.effect.{ContextShift, Sync}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

object HelloRoutes {
  object RepeatParam extends OptionalQueryParamDecoderMatcher[Int]("repeat")

  def apply[F[_]: Sync: ContextShift]: HttpRoutes[F] = {
    object dsl extends Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name :? RepeatParam(repeat) =>
        repeat match {
          case Some(v) =>
            val newName = (1 to v.toInt).map(_ => name).mkString("-")
            Ok(s"Hello, $newName")
          case _ =>
            Ok(s"Hello, $name")
        }
    }
  }
}
