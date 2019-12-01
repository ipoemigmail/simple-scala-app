package app.routes

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HiRoutes {

  def apply[F[_]: Sync]: HttpRoutes[F] = {
    object dsl extends Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "hi" / name =>
        Ok(s"Hi, $name")
    }
  }
}
