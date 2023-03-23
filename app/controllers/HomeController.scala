package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.parser.QueryParser

import scala.util.{Failure, Success}
import sangria.marshalling.playJson._
import sangria.parser.SyntaxError
import sangria.ast.Document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import graphql.graphqlSchema.SchemaDefinition
import context.MyContext
import mySchema.DBSchema.createDatabase

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


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

  def graphQLAction: Action[JsValue] = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]
    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) => Some(parseVariables(vars))
      case obj: JsObject => Some(obj)
      case _ => None
    }

    def parseVariables(variables: String) =
      if (variables.trim == "" || variables.trim == "null") Json.obj() else Json.parse(variables).as[JsObject]

    QueryParser.parse(query) match {
      // query parsed successfully, time to execute it!
      case Success(queryAst) =>
        executeGraphQLQuery(queryAst, operation, variables)

      // can't parse GraphQL query, return error
      case Failure(error: SyntaxError) =>
        Future.successful(BadRequest(Json.obj("error" -> error.getMessage)))
    }
  }

  val dao = createDatabase

  def executeGraphQLQuery(query: Document, op: Option[String], vars: Option[JsObject]): Future[Result] = {
    Executor.execute(
      SchemaDefinition,
      query, MyContext(dao),
      operationName = op,
      variables = vars getOrElse Json.obj())
      .map(Ok(_))
      .recover {
        case error: QueryAnalysisError => BadRequest(error.resolveError)
        case error: ErrorWithResolver => InternalServerError(error.resolveError)
      }
  }
}
