import cats.effect._

object App extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("Run!!"))
    } yield ExitCode.Success
  }
}
