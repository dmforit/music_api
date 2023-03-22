package server

import sangria.parser.QueryParser
import sangria.ast.Document
import sangria.execution
import mySchema._

object GraphQLServer {
  private val dao = DBSchema.createDatabase
}