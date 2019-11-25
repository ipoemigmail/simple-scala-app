package app

import cats.effect._
import cats.implicits._
import app.routes.{HelloRoutes, HiRoutes}
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object App extends IOApp {

  val helloRoutes: HttpRoutes[IO] = new HelloRoutes[IO].routes
  val hiRoutes: HttpRoutes[IO] = new HiRoutes[IO].routes

  val rootRoutes: HttpRoutes[IO] = helloRoutes <+> hiRoutes

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- BlazeServerBuilder[IO]
        .bindHttp(8080, "")
        .withHttpApp(Router("/" -> rootRoutes).orNotFound)
        .serve
        .compile
        .drain
    } yield ExitCode.Success

}
