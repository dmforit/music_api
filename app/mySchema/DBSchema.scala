package mySchema

import models._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object DBSchema {

  class UsersTable(tag: Tag) extends Table[User](tag, "music_app.user") {
    def id: Rep[Int] = column[Int]("id")
    def role: Rep[String] = column[String]("role")
    def nickname: Rep[String] = column[String]("nickname")
    def email: Rep[String] = column[String]("email")
    def password: Rep[String] = column[String]("password")
    def * : ProvenShape[User] = (id, role, nickname, email, password).mapTo[User]
  }

  class SingersTable(tag: Tag) extends Table[Entity](tag, "singer") {
    def id: Rep[Int] = column[Int]("id")
    def name: Rep[String] = column[String]("name")
    def cover: Rep[String] = column[String]("cover")
    def * : ProvenShape[Entity] = (id, name, cover).mapTo[Entity]
  }

  class MusicBandsTable(tag: Tag) extends Table[Entity](tag, "music_band") {
    def id: Rep[Int] = column[Int]("id")
    def name: Rep[String] = column[String]("name")
    def cover: Rep[String] = column[String]("cover")
    def * : ProvenShape[Entity] = (id, name, cover).mapTo[Entity]
  }

  class AlbumsTable(tag: Tag) extends Table[Entity](tag, "album") {
    def id: Rep[Int] = column[Int]("id")
    def name: Rep[String] = column[String]("name")
    def cover: Rep[String] = column[String]("cover")
    def * : ProvenShape[Entity] = (id, name, cover).mapTo[Entity]
  }

  class AuthorToAlbumTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "author_to_album") {
    def album_id: Rep[Int] = column[Int]("album_id")
    def singer_id: Rep[Int] = column[Int]("singer_id")
    def music_band_id: Rep[Int] = column[Int]("music_band_id")
    def * : ProvenShape[(Int, Int, Int)] = (album_id, singer_id, music_band_id)
  }

  class SongsTable(tag: Tag) extends Table[Song](tag, "song") {
    def id: Rep[Int] = column[Int]("id")
    def name: Rep[String] = column[String]("name")
    def cover: Rep[String] = column[String]("cover")
    def length: Rep[Double] = column[Double]("length")
    def genre: Rep[String] = column[String]("genre")
    def file: Rep[String] = column[String]("file")
    def album_id: Rep[Option[Int]] = column[Option[Int]]("album_id")
    def * : ProvenShape[Song] = (id, name, cover, length, genre, file, album_id).mapTo[Song]
  }

  class AuthorToSongTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "author_to_song") {
    def song_id: Rep[Int] = column[Int]("song_id")
    def singer_id: Rep[Int] = column[Int]("singer_id")
    def music_band_id: Rep[Int] = column[Int]("music_band_id")
    def * : ProvenShape[(Int, Int, Int)] = (song_id, singer_id, music_band_id)
  }

  class LikedSongsTable(tag: Tag) extends Table[(Int, Int)](tag, "liked_song") {
    def user_id: Rep[Int] = column[Int]("user_id")
    def song_id: Rep[Int] = column[Int]("song_id")
    def * : ProvenShape[(Int, Int)] = (user_id, song_id)
  }

  class LikedBandsTable(tag: Tag) extends Table[(Int, Int)](tag, "liked_band") {
    def user_id: Rep[Int] = column[Int]("user_id")
    def band_id: Rep[Int] = column[Int]("band_id")
    def * : ProvenShape[(Int, Int)] = (user_id, band_id)
  }

  class LikedAlbumsTable(tag: Tag) extends Table[(Int, Int)](tag, "liked_album") {
    def user_id: Rep[Int] = column[Int]("user_id")
    def album_id: Rep[Int] = column[Int]("album_id")
    def * : ProvenShape[(Int, Int)] = (user_id, album_id)
  }

  class LikedSingersTable(tag: Tag) extends Table[(Int, Int)](tag, "liked_singer") {
    def user_id: Rep[Int] = column[Int]("user_id")
    def singer_id: Rep[Int] = column[Int]("singer_id")
    def * : ProvenShape[(Int, Int)] = (user_id, singer_id)
  }

  val Users = TableQuery[UsersTable]
  val Singers = TableQuery[SingersTable]
  val MusicBands = TableQuery[MusicBandsTable]
  val Albums = TableQuery[AlbumsTable]
  val AuthorToAlbums = TableQuery[AuthorToAlbumTable]
  val Songs = TableQuery[SongsTable]
  val AuthorToSongs = TableQuery[AuthorToAlbumTable]
  val LikedSongs = TableQuery[LikedSongsTable]
  val LikedBands = TableQuery[LikedBandsTable]
  val likedAlbums = TableQuery[LikedAlbumsTable]
  val LikedSingers = TableQuery[LikedSingersTable]

  val databaseSetup = DBIO.seq(
    Users forceInsertAll Seq(
//      User(152452, Roles.user, "user", "user@gmail.com", "password"),
//      User(2, Roles.admin, "admin", "admin@gmail.com", "password")
    ),

//    Songs forceInsertAll Seq(
//      Song(3, "Shape of You", "", 4.24, Genres.pop, "", Some(14)),
//      Song(4, "Carnival of Rust", "", 4.34, Genres.rock, "", Some(15)),
//      Song(5, "Numb", "", 3.08, Genres.rock, "", Some(13)),
//      Song(6, "Wake me up", "", 4.33, Genres.country, "", None),
//    ),
//
//    Singers forceInsertAll Seq(
//      Entity(7, "Ed Sheeran", ""),
//      Entity(8, "Marko Saaresto", ""),
//      Entity(9, "Avicii", "")
//    ),
//
//    MusicBands forceInsertAll Seq(
//      Entity(10, "Poets of the Fall", ""),
//      Entity(11, "Linkin Park", ""),
//      Entity(12, "Falling in Reverse", "")
//    ),
//
//    Albums forceInsertAll Seq(
//      Entity(13, "Meteora", ""),
//      Entity(14, "Divide", ""),
//      Entity(15, "Carnival of Rust", "")
//    ),
  )
  def createDatabase: DAO = {
    val db = Database.forConfig("db")

    Await.result(db.run(databaseSetup), 10 seconds)

    new DAO(db)
  }
}


