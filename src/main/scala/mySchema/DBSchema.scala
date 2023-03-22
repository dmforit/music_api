package mySchema

import models._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.language.postfixOps

object DBSchema {

  class UsersTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def role = column[String]("ROLE")

    def nickname = column[String]("NICKNAME")

    def email = column[String]("EMAIL")

    def password = column[String]("PASSWORD")

    def * : ProvenShape[User] = (id, role, nickname, email, password).mapTo[User]
  }

  val Users = TableQuery[UsersTable]

  class SingersTable(tag: Tag) extends Table[Entity](tag, "SINGERS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")

    def cover = column[String]("COVER")

    def * = (id, name, cover).mapTo[Entity]
  }

  val Singers = TableQuery[SingersTable]

  class MusicBandsTable(tag: Tag) extends Table[Entity](tag, "MUSIC_BANDS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")

    def cover = column[String]("COVER")

    def * = (id, name, cover).mapTo[Entity]
  }

  val MusicBands = TableQuery[MusicBandsTable]

  class AlbumsTable(tag: Tag) extends Table[Entity](tag, "ALBUMS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")

    def cover = column[String]("COVER")

    def * = (id, name, cover).mapTo[Entity]
  }

  val Albums = TableQuery[AlbumsTable]

  class AuthorToAlbumTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "AUTHOR_TO_ALBUM") {
    def albumId = column[Int]("albumID", O.PrimaryKey)

    def singerId = column[Int]("singerID", O.PrimaryKey)

    def musicBandId = column[Int]("musicBandID", O.PrimaryKey)

    def * = (albumId, singerId, musicBandId)
  }

  val AuthorToAlbums = TableQuery[AuthorToAlbumTable]

  class SongsTable(tag: Tag) extends Table[Song](tag, "SONGS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")

    def cover = column[String]("COVER")

    def length = column[Double]("LENGTH")

    def genre = column[String]("GENRE")

    def file = column[String]("FILE")

    def albumId = column[Int]("albumID", O.PrimaryKey)

    def * = (id, name, cover, length, genre, file, albumId).mapTo[Song]
  }

  val Songs = TableQuery[SongsTable]

  class AuthorToSongTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "AUTHOR_TO_SONG") {
    def songId = column[Int]("songId", O.PrimaryKey)

    def singerId = column[Int]("singerID", O.PrimaryKey)

    def musicBandId = column[Int]("musicBandID", O.PrimaryKey)

    def * = (songId, singerId, musicBandId)
  }

  val AuthorToSongs = TableQuery[AuthorToAlbumTable]

  class LikedSongsTable(tag: Tag) extends Table[(Int, Int)](tag, "LIKED_SONGS") {
    def userId = column[Int]("userId", O.PrimaryKey)

    def songId = column[Int]("songId", O.PrimaryKey)

    def * = (userId, songId)
  }

  val LikedSongs = TableQuery[LikedSongsTable]

  class LikedBandsTable(tag: Tag) extends Table[(Int, Int)](tag, "LIKED_BANDS") {
    def userId = column[Int]("userId", O.PrimaryKey)

    def bandId = column[Int]("bandId", O.PrimaryKey)

    def * = (userId, bandId)
  }

  val LikedBands = TableQuery[LikedBandsTable]

  class LikedAlbumsTable(tag: Tag) extends Table[(Int, Int)](tag, "LIKED_ALBUMS") {
    def userId = column[Int]("userId", O.PrimaryKey)

    def albumId = column[Int]("albumId", O.PrimaryKey)

    def * = (userId, albumId)
  }

  val likedAlbums = TableQuery[LikedAlbumsTable]

  class LikedSingersTable(tag: Tag) extends Table[(Int, Int)](tag, "LIKED_SINGERS") {
    def userId = column[Int]("userId", O.PrimaryKey)

    def singerId = column[Int]("singerId", O.PrimaryKey)

    def * = (userId, singerId)
  }

  val LikedSingers = TableQuery[LikedSingersTable]


  // Schema creating and adding three entities to the database
  val databaseSetup = DBIO.seq(
    Users.schema.create,

    Songs.schema.create,

    //  Users forceInsertAll Seq(
    //    User(1, Roles.user, "user", "user@gmail.com", "password"),
    //    User(2, Roles.admin, "admin", "admin@gmail.com", "password")
    //  )
    //
    Songs forceInsertAll Seq(
      Song(1, "Shape of You", "", 4.24, Genres.pop, "", 11),
      Song(2, "Numb", "", 3.08, Genres.rock, "", 21),
      Song(3, "Wake me up", "", 4.33, Genres.pop, "", 31)
    )
  )

  def createDatabase: DAO = {
    val db = Database.forConfig("mydb")

    Await.result(db.run(databaseSetup), 10 seconds)

    new DAO(db)
  }
}


