package mySchema
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class DAO(db: Database) {
  def getSongs: Future[Seq[models.Song]] = db.run(DBSchema.Songs.result)
}