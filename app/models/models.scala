import play.api.libs.json.{Json, OFormat}
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
    def id: Long
    def auditionsNumber: Int
    def songs: List[Song]
    def albums: List[Album]
    def photoCover: Array[Byte]
  }

  trait Identifiable {
    val id: Long
  }

  object Identifiable {
    implicit def hasId[T <: Identifiable]: HasId[T, Long] = HasId(_.id)
  }

  final case class Person(
    id: Long,
    auditionsNumber: Int,
    songs: List[Song],
    albums: List[Album],
    photoCover: Array[Byte],
    firstName: String,
    secondName: String
  )  extends MusicianInterface with Identifiable

  final case class MusicBand(
    id: Long,
    auditionsNumber: Int,
    songs: List[Song],
    albums: List[Album],
    photoCover: Array[Byte],
    name: String,
    members: List[Person]
  ) extends MusicianInterface with Identifiable

  case object Roles {
    val user: String = "User"
    val admin: String = "Admin"
  }

  case class AuthProvider(email: String, password: String)

//  case class AuthProviderData(auth: AuthProvider)

  case class Album(
    id: Long,
    name: String,
    cover: Array[Byte],
    auditions: Int,
    songs: List[Song]) extends Identifiable

  case class AlbumDescription(
    name: String,
    cover: Array[Byte],
    auditions: Int,
    songs: List[SongDescriptionInput])

  case class PersonDescription(
    auditionsNumber: Int,
    songs: List[SongDescriptionInput],
    albums: List[AlbumDescription],
    photoCover: Array[Byte],
    firstName: String,
    secondName: String)

  case class SongDescriptionInput(
    name: String,
    duration: Int,
    file: String,
    cover: Option[Array[Byte]],
    genre: Option[String])

  case class MusicBandDescription(
    auditionsNumber: Int,
    songs: List[SongDescriptionInput],
    albums: List[AlbumDescription],
    photoCover: Array[Byte],
    name: String,
    members: List[PersonDescription])

  case class User(
    id: Long,
    role: String,
    nickname: String,
    email: String,
    password: String) extends Identifiable

  case class Entity(
    id: Long,
    name: String,
    cover: Array[Byte]) extends Identifiable

  case class Song(
    id: Long,
    name: String,
    cover: Option[Array[Byte]],
    length: Double,
    genre: String,
    file: Array[Byte],
    album_id: Long) extends Identifiable

  case class ActivityList(
    genres: List[String],
    musicians: List[MusicianInterface],
    songs: List[Song]
  )
}