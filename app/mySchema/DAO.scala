package mySchema

import slick.jdbc.PostgresProfile.api._
import models._
import scala.concurrent.Future

//class DAO(db: Database) {
//  def getSongs: Future[Seq[Song]] = db.run(DBSchema.Songs.result)
//
//  def getSongsByID(ids: Seq[Int]): Future[Seq[Song]] = db.run(
//    DBSchema.Songs.filter(_.id inSet ids).result
//  )
//}

class DAO {
  def getSongs: List[Song] = DAO.songs
  def getUsers: List[User] = DAO.users
  def getSingers: List[Entity] = DAO.singers
  def getMusicBands: List[Entity] = DAO.musicBands
  def getAlbums: List[Entity] = DAO.albums
}

object DAO {
  val songs: List[Song] = List(
    Song(1, "Shape of You", Option(""), 4.24, Option(Genres.pop), "", Some(2)),
    Song(2, "Carnival of Rust", Option(""), 4.34, Option(Genres.rock), "", Some(3)),
    Song(3, "Numb", Option(""), 3.08, Option(Genres.rock), "", Some(1)),
    Song(4, "Wake me up", Option(""), 4.33, Option(Genres.country), "", None)
  )

  val users: List[User] = List(
    User(1, Roles.user, "user", "user@gmail.com", "password"),
    User(2, Roles.admin, "admin", "admin@gmail.com", "password")
  )

  val singers: List[Entity] = List(
    Entity(1, "Ed Sheeran", ""),
    Entity(2, "Marko Saaresto", ""),
    Entity(3, "Avicii", "")
  )

  val musicBands: List[Entity] = List(
    Entity(1, "Poets of the Fall", ""),
    Entity(2, "Linkin Park", ""),
    Entity(3, "Falling in Reverse", "")
  )

  val albums: List[Entity] = List(
    Entity(1, "Meteora", ""),
    Entity(2, "Divide", ""),
    Entity(3, "Carnival of Rust", "")
  )
}