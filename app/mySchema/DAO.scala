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
}

object DAO {
  val songs: List[Song] = List(
    Song(1, "Shape of You", Option(""), 4.24, Option(Genres.pop), "", Some(2)),
    Song(2, "Carnival of Rust", Option(""), 4.34, Option(Genres.rock), "", Some(3)),
    Song(3, "Numb", Option(""), 3.08, Option(Genres.rock), "", Some(1)),
    Song(4, "Wake me up", Option(""), 4.33, Option(Genres.country), "", None)
  )
}