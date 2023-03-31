package controllers

//import context.MyContext
import play.api.libs.json._
import play.api.mvc._
import sangria.execution._
import sangria.parser.{QueryParser, SyntaxError}
import sangria.marshalling.playJson._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import javax.inject._
import scala.concurrent.ExecutionContext
import graphql.graphqlSchema
import mySchema.DAO
//import mySchema.DBSchema.createDatabase


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)(implicit val ec: ExecutionContext) extends BaseController {

//  private val dao: DAO = createDatabase

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj() else Json.parse(variables).as[JsObject]

  lazy val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case (_, error @ TooComplexQueryError) => HandledException(error.getMessage)
    case (_, error@MaxQueryDepthReachedError(_)) => HandledException(error.getMessage)
  }

  case object TooComplexQueryError extends Exception("Query is too expensive.")

  def graphqlBody: Action[JsValue] = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) => Some(parseVariables(vars))
      case obj: JsObject => Some(obj)
      case _ => None
    }

    executeQuery(query, variables, operation)
  }

  private def executeQuery(query: String, variables: Option[JsObject], operation: Option[String]) =
    QueryParser.parse(query) match {
      case Success(queryAst) =>
        Executor.execute(
          graphqlSchema.SchemaDefinition,
          queryAst,
          new DAO,
          operationName = operation,
          variables = variables getOrElse Json.obj(),
          exceptionHandler = exceptionHandler,
          queryReducers = List(
            QueryReducer.rejectMaxDepth[DAO](15),
            QueryReducer.rejectComplexQueries[DAO](4000, (_, _) => TooComplexQueryError)),
          ).map(Ok(_)).recover {
            case error: QueryAnalysisError => BadRequest(error.resolveError)
            case error: ErrorWithResolver => InternalServerError(error.resolveError)
          }

      // can't parse GraphQL query, return error
      case Failure(error: SyntaxError) =>
        Future.successful(BadRequest(Json.obj(
          "syntaxError" -> error.getMessage,
          "locations" -> Json.arr(Json.obj(
            "line" -> error.originalError.position.line,
            "column" -> error.originalError.position.column)))))

      case Failure(error) =>
        throw error
    }
}
