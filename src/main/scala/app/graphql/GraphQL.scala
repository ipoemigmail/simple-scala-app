package app.graphql

import _root_.sangria.ast._
import _root_.sangria.execution.{WithViolations, _}
import _root_.sangria.marshalling.circe._
import _root_.sangria.parser.{QueryParser, SyntaxError}
import _root_.sangria.schema._
import _root_.sangria.validation._
import cats._
import cats.effect._
import cats.implicits._
import io.circe.optics.JsonPath.root
import io.circe.{Json, JsonObject}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/** An algebra of operations in F that evaluate GraphQL requests. */
trait GraphQL[F[_]] {
  /**
    * Executes a JSON-encoded request in the standard POST encoding, described thus in the spec:
    *
    * A standard GraphQL POST request should use the application/json content type, and include a
    * JSON-encoded body of the following form:
    *
    * {
    * "query": "...",
    * "operationName": "...",
    * "variables": { "myVariable": "someValue", ... }
    * }
    *
    * `operationName` and `variables` are optional fields. `operationName` is only required if
    * multiple operations are present in the query.
    *
    * @return either an error Json or result Json
    */
  def query(request: Json): F[Either[Json, Json]]

  /**
    * Executes a request given a `query`, optional `operationName`, and `varianbles`.
    *
    * @return either an error Json or result Json
    */
  def query(query: String, operationName: Option[String], variables: JsonObject): F[Either[Json, Json]]
}

object GraphQL {
  // Some circe lenses
  private val queryStringLens = root.query.string
  private val operationNameLens = root.operationName.string
  private val variablesLens = root.variables.obj

  // Partially-applied constructor
  def apply[F[_]] = new Partial[F]

  // Format a SyntaxError as a GraphQL `errors`
  private def formatSyntaxError(e: SyntaxError): Json =
    Json.obj(
      "errors" -> Json.arr(
        Json.obj(
          "message" -> Json.fromString(e.getMessage),
          "locations" -> Json.arr(
            Json.obj(
              "line" -> Json.fromInt(e.originalError.position.line),
              "column" -> Json.fromInt(e.originalError.position.column)
            )
          )
        )
      )
    )

  // Format a WithViolations as a GraphQL `errors`
  private def formatWithViolations(e: WithViolations): Json =
    Json.obj("errors" -> Json.fromValues(e.violations.map {
      case v: AstNodeViolation =>
        Json.obj(
          "message" -> Json.fromString(v.errorMessage),
          "locations" -> Json.fromValues(
            v.locations.map(loc => Json.obj("line" -> Json.fromInt(loc.line), "column" -> Json.fromInt(loc.column)))
          )
        )
      case v => Json.obj("message" -> Json.fromString(v.errorMessage))
    }))

  // Format a String as a GraphQL `errors`
  private def formatString(s: String): Json = Json.obj("errors" -> Json.arr(Json.obj("message" -> Json.fromString(s))))

  // Format a Throwable as a GraphQL `errors`
  private def formatThrowable(e: Throwable): Json =
    Json.obj(
      "errors" -> Json
        .arr(Json.obj("class" -> Json.fromString(e.getClass.getName), "message" -> Json.fromString(e.getMessage)))
    )

  final class Partial[F[_]] {
    // The rest of the constructor
    def apply[A](
        schema: Schema[A, Unit],
        userContext: F[A],
        blockingExecutionContext: ExecutionContext
    )(
        implicit
        F: MonadError[F, Throwable],
        L: LiftIO[F],
        cs: ContextShift[IO]
    ): GraphQL[F] =
      new GraphQL[F] {
        // Destructure `request` and delegate to the other overload.
        def query(request: Json): F[Either[Json, Json]] = {
          val queryString = queryStringLens.getOption(request)
          val operationName = operationNameLens.getOption(request)
          val variables = variablesLens.getOption(request).getOrElse(JsonObject())
          queryString match {
            case Some(qs) => query(qs, operationName, variables)
            case None => fail(formatString("No 'query' property was present in the request."))
          }
        }

        // Parse `query` and execute.
        def query(query: String, operationName: Option[String], variables: JsonObject): F[Either[Json, Json]] =
          QueryParser.parse(query) match {
            case Success(ast) => exec(schema, userContext, ast, operationName, variables)(blockingExecutionContext, cs)
            case Failure(e @ SyntaxError(_, _, pe)) => fail(formatSyntaxError(e))
            case Failure(e) => fail(formatThrowable(e))
          }

        // Lift a `Json` into the error side of our effect.
        def fail(j: Json): F[Either[Json, Json]] =
          F.pure(j.asLeft)

        // Execute a GraphQL query with Sangria, lifting into IO for safety and sanity.
        def exec(
            schema: Schema[A, Unit],
            userContext: F[A],
            query: Document,
            operationName: Option[String],
            variables: JsonObject
        )(implicit ec: ExecutionContext, cs: ContextShift[IO]): F[Either[Json, Json]] =
          userContext
            .flatMap { ctx =>
              IO.fromFuture {
                  IO {
                    Executor.execute(
                      schema = schema,
                      queryAst = query,
                      userContext = ctx,
                      variables = Json.fromJsonObject(variables),
                      operationName = operationName,
                      exceptionHandler = ExceptionHandler {
                        case (_, e) ⇒ HandledException(e.getMessage)
                      }
                    )
                  }
                }
                .to[F]
            }
            .attempt
            .flatMap {
              case Right(json) => F.pure(json.asRight)
              case Left(err: WithViolations) => fail(formatWithViolations(err))
              case Left(err) => fail(formatThrowable(err))
            }
      }
  }
}
