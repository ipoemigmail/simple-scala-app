package app.routes

import app.Routes
import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final class HelloRoutes[F[_]: Sync] extends Routes[F] {

  private val dsl: Http4sDsl[F] = new Http4sDsl[F]{}
  import dsl._

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name")
  }
}
