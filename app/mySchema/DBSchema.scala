package mySchema

import models._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object DBSchema {

  class UsersTable(tag: Tag) extends Table[User](tag, "music_app.user") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    def role: Rep[String] = column[String]("role")
    def nickname: Rep[String] = column[String]("nickname")
    def email: Rep[String] = column[String]("email")
    def password: Rep[String] = column[String]("password")
    def * : ProvenShape[User] = (id, role, nickname, email, password).mapTo[User]
  }

  class SingersTable(tag: Tag) extends Table[Entity](tag, "singer") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def cover: Rep[Array[Byte]] = column[Array[Byte]]("cover")
    def * : ProvenShape[Entity] = (id, name, cover).mapTo[Entity]
  }

  class MusicBandsTable(tag: Tag) extends Table[Entity](tag, "music_band") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def cover: Rep[Array[Byte]] = column[Array[Byte]]("cover")
    def * : ProvenShape[Entity] = (id, name, cover).mapTo[Entity]
  }

  class AlbumsTable(tag: Tag) extends Table[Entity](tag, "album") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def cover: Rep[Array[Byte]] = column[Array[Byte]]("cover")
    def * : ProvenShape[Entity] = (id, name, cover).mapTo[Entity]
  }

  class AuthorToAlbumTable(tag: Tag) extends Table[(Long, Long, Long)](tag, "author_to_album") {
    def album_id: Rep[Long] = column[Long]("album_id")
    def singer_id: Rep[Long] = column[Long]("singer_id")
    def music_band_id: Rep[Long] = column[Long]("music_band_id")
    def * : ProvenShape[(Long, Long, Long)] = (album_id, singer_id, music_band_id)
  }

  class SongsTable(tag: Tag) extends Table[Song](tag, "song") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def cover: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("cover")
    def length: Rep[Double] = column[Double]("length")
    def genre: Rep[String] = column[String]("genre")
    def file: Rep[Array[Byte]] = column[Array[Byte]]("file")
    def album_id: Rep[Long] = column[Long]("album_id")
    def * : ProvenShape[Song] = (id, name, cover, length, genre, file, album_id).mapTo[Song]
  }

  class AuthorToSongTable(tag: Tag) extends Table[(Long, Long, Long)](tag, "author_to_song") {
    def song_id: Rep[Long] = column[Long]("song_id")
    def singer_id: Rep[Long] = column[Long]("singer_id")
    def music_band_id: Rep[Long] = column[Long]("music_band_id")
    def * : ProvenShape[(Long, Long, Long)] = (song_id, singer_id, music_band_id)
  }

  class LikedSongsTable(tag: Tag) extends Table[(Long, Long)](tag, "liked_song") {
    def user_id: Rep[Long] = column[Long]("user_id")
    def song_id: Rep[Long] = column[Long]("song_id")
    def * : ProvenShape[(Long, Long)] = (user_id, song_id)
  }

  class LikedBandsTable(tag: Tag) extends Table[(Long, Long)](tag, "liked_band") {
    def user_id: Rep[Long] = column[Long]("user_id")
    def band_id: Rep[Long] = column[Long]("band_id")
    def * : ProvenShape[(Long, Long)] = (user_id, band_id)
  }

  class LikedAlbumsTable(tag: Tag) extends Table[(Long, Long)](tag, "liked_album") {
    def user_id: Rep[Long] = column[Long]("user_id")
    def album_id: Rep[Long] = column[Long]("album_id")
    def * : ProvenShape[(Long, Long)] = (user_id, album_id)
  }

  class LikedSingersTable(tag: Tag) extends Table[(Long, Long)](tag, "liked_singer") {
    def user_id: Rep[Long] = column[Long]("user_id")
    def singer_id: Rep[Long] = column[Long]("singer_id")
    def * : ProvenShape[(Long, Long)] = (user_id, singer_id)
  }

  lazy val Users = TableQuery[UsersTable]

  lazy val Singers = TableQuery[SingersTable]
  lazy val MusicBands = TableQuery[MusicBandsTable]
  lazy val Albums = TableQuery[AlbumsTable]
  lazy val AuthorToAlbums = TableQuery[AuthorToAlbumTable]
  lazy val Songs = TableQuery[SongsTable]
  lazy val AuthorToSongs = TableQuery[AuthorToAlbumTable]
  lazy val LikedSongs = TableQuery[LikedSongsTable]
  lazy val LikedBands = TableQuery[LikedBandsTable]
  lazy val likedAlbums = TableQuery[LikedAlbumsTable]
  lazy val LikedSingers = TableQuery[LikedSingersTable]

  val databaseSetup = DBIO.seq(
    Users.schema.create,
    Songs.schema.create,
    Singers.schema.create,
    MusicBands.schema.create,
    Albums.schema.create,

    Users forceInsertAll Seq(
      User(1, Roles.user, "user", "user@gmail.com", "password"),
      User(2, Roles.admin, "admin", "admin@gmail.com", "password")),

    Songs forceInsertAll Seq(
      Song(1, "Shape of You", Option(Array()), 4.24, Genres.pop, Array(), 2),
      Song(2, "Carnival of Rust", Option(Array()), 4.34, Genres.rock, Array(), 3),
      Song(3, "Numb", Option(Array()), 3.08, Genres.rock, Array(), 1),
      Song(4, "Wake me up", Option(Array()), 4.33, Genres.country, Array(), 4)
    ),

    Singers forceInsertAll Seq(
      Entity(1, "Ed Sheeran", Array()),
      Entity(2, "Marko Saaresto", Array()),
      Entity(3, "Avicii", Array())
    ),

    MusicBands forceInsertAll Seq(
      Entity(1, "Poets of the Fall", Array()),
      Entity(2, "Linkin Park", Array()),
      Entity(3, "Falling in Reverse", Array())
    ),

    Albums forceInsertAll Seq(
      Entity(1, "Meteora", Array()),
      Entity(2, "Divide", Array()),
      Entity(3, "Carnival of Rust", Array()),
      Entity(4, "True", Array())
    ),
  )

  /**
   * Uncomment Await.result(db.run(databaseSetup), 10 seconds) to set up the database
   * @return
   */
  def createDatabase: DAO = {
    val db = Database.forConfig("db")

//    Await.result(db.run(databaseSetup), 10 seconds)

    Await.result(db.run(DBIO.seq()), 10 seconds)
    new DAO(db)
  }
}


