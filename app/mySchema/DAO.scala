package mySchema

import slick.jdbc.PostgresProfile.api._
import models._
import scala.concurrent.Future

class DAO(db: Database) {
  def getSongs: Future[Seq[Song]] = db.run(DBSchema.Songs.result)
  def getSongs(ids: Seq[Long]): Future[Seq[Song]] = db.run(
    DBSchema.Songs.filter(_.id inSet ids).result
  )
  def getSong(id: Long): Future[Option[Song]] = db.run(
    DBSchema.Songs.filter(_.id === id).result.headOption
  )

  def getUsers: Future[Seq[User]] = db.run(DBSchema.Users.result)
  def getSingers: Future[Seq[Entity]] = db.run(DBSchema.Singers.result)
  def getMusicBands: Future[Seq[Entity]]  = db.run(DBSchema.MusicBands.result)
  def getAlbums: Future[Seq[Entity]]  = db.run(DBSchema.Albums.result)
}