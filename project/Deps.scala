import sbt.librarymanagement.ModuleID
import sbt.librarymanagement.syntax._

object Deps {
  lazy val scalaVersion = "2.12.9"

  lazy val catsEffect = "org.typelevel" %% "cats-effect" % "2.0.0"
  lazy val circeVersion = "0.12.2"
  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-generic-extras" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-optics" % "0.12.0"
  )

  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.1"
  lazy val simulacrum = "org.typelevel" %% "simulacrum" % "1.0.0"
  lazy val macrosParadise = "org.scalamacros" % "paradise" % "2.1.0"
  lazy val enumeratumVersion = "1.5.13"
  lazy val enumeratum = Seq(
    "com.beachape" %% "enumeratum" % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumVersion
  )


  lazy val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-circe",
    "org.http4s" %% "http4s-blaze-server",
    "org.http4s" %% "http4s-blaze-client"
  ).map(_ % "0.20.11")

  lazy val sangria: Seq[ModuleID] = Seq(
    "org.sangria-graphql" %% "sangria" % "1.4.2",
    "org.sangria-graphql" %% "sangria-circe" % "1.2.1"
  )

}
