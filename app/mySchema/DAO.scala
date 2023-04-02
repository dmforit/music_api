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
  def getUsers(ids: Seq[Long]): Future[Seq[User]] = db.run(
    DBSchema.Users.filter(_.id inSet ids).result
  )
  def getUser(id: Long): Future[Option[User]] = db.run(
    DBSchema.Users.filter(_.id === id).result.headOption
  )
  def addUser(role: String, name: String, authData: AuthProvider): Future[User] = {
    val rand = new scala.util.Random

    val insertAndReturnUserQuery = (DBSchema.Users returning DBSchema.Users.map(_.id)) into {
      (user, id) => user.copy(id = id)
    }
    db.run {
      insertAndReturnUserQuery += User(rand.nextLong().abs, role, name, authData.email, authData.password)
    }
  }

  def getSingers: Future[Seq[Entity]] = db.run(DBSchema.Singers.result)
  def getSingers(ids: Seq[Long]): Future[Seq[Entity]] = db.run(
    DBSchema.Singers.filter(_.id inSet ids).result
  )
  def getSinger(id: Long): Future[Option[Entity]] = db.run(
    DBSchema.Singers.filter(_.id === id).result.headOption
  )

  def getMusicBands: Future[Seq[Entity]]  = db.run(DBSchema.MusicBands.result)
  def getMusicBands(ids: Seq[Long]): Future[Seq[Entity]] = db.run(
    DBSchema.MusicBands.filter(_.id inSet ids).result
  )
  def getMusicBand(id: Long): Future[Option[Entity]] = db.run(
    DBSchema.MusicBands.filter(_.id === id).result.headOption
  )

  def getAlbums: Future[Seq[Entity]]  = db.run(DBSchema.Albums.result)
  def getAlbums(ids: Seq[Long]): Future[Seq[Entity]] = db.run(
    DBSchema.Albums.filter(_.id inSet ids).result
  )
  def getAlbum(id: Long): Future[Option[Entity]] = db.run(
    DBSchema.Albums.filter(_.id === id).result.headOption
  )
}