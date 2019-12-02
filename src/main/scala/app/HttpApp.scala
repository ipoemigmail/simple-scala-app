package app

import app.service.FeedService
import app.graphql.GraphQL
import app.repo.FeedRepo
import app.routes.{GraphQLRoutes, HelloRoutes, HiRoutes}
import app.scheme.QueryType
import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import sangria.schema.Schema

import scala.concurrent.ExecutionContext._
import scala.concurrent.ExecutionContextExecutor

trait HttpApp extends IOApp {

  implicit val bec: ExecutionContextExecutor = fromExecutor(java.util.concurrent.Executors.newFixedThreadPool(10))
  implicit val feedRepo: FeedRepo[IO] = FeedRepo[IO]
  implicit val feedService: FeedService[IO] = FeedService[IO]

  val graphQL: GraphQL[IO] = GraphQL[IO].apply(
    Schema(
      query = QueryType[IO]
    ),
    ().pure[IO],
    bec
  )

  val helloRoutes: HttpRoutes[IO] = HelloRoutes[IO]
  val hiRoutes: HttpRoutes[IO] = HiRoutes[IO]
  val graphQLRoutes: HttpRoutes[IO] = GraphQLRoutes[IO](graphQL)

  val rootRoutes: HttpRoutes[IO] = helloRoutes <+> hiRoutes

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- BlazeServerBuilder[IO]
        .bindHttp(8080, "")
        .withHttpApp(Router("/" -> graphQLRoutes).orNotFound)
        .serve
        .compile
        .drain
    } yield ExitCode.Success

}
