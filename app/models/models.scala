import sangria.execution.deferred.HasId

package object models {

  // Enum class for each genre from database
  object Genres extends Enumeration  {
    val blues: String = "Blues"
    val country: String = "Country"
    val electronic: String = "Electronic"
    val hiphop: String = "HipHop"
    val jazz: String = "Jazz"
    val pop: String = "Pop"
    val rnb: String = "RnB"
    val rock: String = "Rock"
    val metal: String = "Metal"
    val punk: String = "Punk"
  }

  sealed trait MusicianInterface {
    def id: Int
    def auditionsNumber: Int
    def songs: List[Song]
    def albums: List[Album]
    def photoCover: String
  }

  trait Identifiable {
    val id: Int
  }

  object Identifiable {
    implicit def hasId[T <: Identifiable]: HasId[T, Int] = HasId(_.id)
  }

  final case class Person(
    id: Int,
    auditionsNumber: Int,
    songs: List[Song],
    albums: List[Album],
    photoCover: String,
    firstName: String,
    secondName: String
  )  extends MusicianInterface with Identifiable

  final case class MusicBand(
    id: Int,
    auditionsNumber: Int,
    songs: List[Song],
    albums: List[Album],
    photoCover: String,
    name: String,
    members: List[Person]
  ) extends MusicianInterface with Identifiable

  case object Roles {
    val user: String = "User"
    val admin: String = "Admin"
  }

  case class Album(
    id: Int,
    name: String,
    cover: String,
    auditions: Int,
    songs: List[Song]) extends Identifiable

  case class AlbumDescription(
    name: String,
    cover: String,
    auditions: Int,
    songs: List[SongDescriptionInput])

  case class PersonDescription(
    auditionsNumber: Int,
    songs: List[SongDescriptionInput],
    albums: List[AlbumDescription],
    photoCover: String,
    firstName: String,
    secondName: String)

  case class SongDescriptionInput(
    name: String,
    duration: Int,
    file: String,
    cover: Option[String],
    genre: Option[String])

  case class MusicBandDescription(
    auditionsNumber: Int,
    songs: List[SongDescriptionInput],
    albums: List[AlbumDescription],
    photoCover: String,
    name: String,
    members: List[PersonDescription])

  case class User(
    id: Int,
    role: String,
    nickname: String,
    email: String,
    password: String) extends Identifiable

  case class Entity(
    id: Int,
    name: String,
    cover: String) extends Identifiable

  case class Song(
    id: Int,
    name: String,
    cover: Option[String],
    length: Double,
    genre: Option[String],
    file: String,
    album_id: Option[Int]) extends Identifiable

  case class ActivityList(
    genres: List[String],
    musicians: List[MusicianInterface],
    songs: List[Song]
  )
}