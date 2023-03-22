package mySchema
import slick.jdbc.PostgresProfile.api._

class DAO(db: Database) {
  def getSongs = db.run(DBSchema.Songs.result)
}