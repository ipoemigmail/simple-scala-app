package app.routes

import app.Routes
import cats.effect.Sync
import org.http4s.{HttpRoutes, QueryParamDecoder}
import org.http4s.dsl.Http4sDsl

final class HelloRoutes[F[_]: Sync] extends Routes[F] {

  private val dsl: Http4sDsl[F] = new Http4sDsl[F] {}
  import dsl._

  object RepeatParam extends OptionalQueryParamDecoderMatcher[Int]("repeat")

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
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
