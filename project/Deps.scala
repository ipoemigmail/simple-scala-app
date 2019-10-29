import sbt.librarymanagement.ModuleID
import sbt.librarymanagement.syntax._

object Deps {
  lazy val scalaVersion = "2.12.9"

  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "2.0.0"
  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-generic-extras",
    "io.circe" %% "circe-parser"
  ).map(_ % "0.12.2")

  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.1"
  lazy val simulacrum = "org.typelevel" %% "simulacrum" % "1.0.0"
  lazy val macrosParadise = "org.scalamacros" % "paradise" % "2.1.0"
  lazy val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-blaze-server",
    "org.http4s" %% "http4s-blaze-client"
  ).map(_ % "0.20.11")

}
