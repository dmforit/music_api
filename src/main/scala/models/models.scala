package object models {

  // Enum class for each genre from database
  case object Genres {
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

  final case class Person(
    id: Int,
    auditionsNumber: Int,
    songs: List[Song],
    albums: List[Album],
    photoCover: String,
    firstName: String,
    secondName: String
  )  extends MusicianInterface

  final case class MusicBand(
    id: Int,
    auditionsNumber: Int,
    songs: List[Song],
    albums: List[Album],
    photoCover: String,
    name: String,
    members: List[Person]
  ) extends MusicianInterface

  case object Roles {
    val user: String = "User"
    val admin: String = "Admin"
  }

  case class Album(
    id: Int,
    name: String,
    cover: String,
    auditions: Int,
    songs: List[Song])

  case class AlbumDescription(
    name: String,
    cover: String,
    auditions: Int,
    songs: List[SongDescriptionInput])

  case class PersonDescription()

  case class SongDescriptionInput(
    name: String,
    duration: Int,
    file: String,
    cover: String,
    genre: String)

  case class MusicBandDescription(
    auditionsNumber: Int,
    songs: List[SongDescriptionInput],
    albums: List[AlbumDescription],
    photoCover: String,
    name: String,
    members: List[PersonDescription])

  case class User(
    id: Int,
    role: Roles.type,
    nickname: String,
    email: String,
    password: String)

  case class Entity(
    id: Int,
    name: String,
    cover: String)

  case class Song(
    id: Int,
    name: String,
    cover: String,
    length: Double,
    genre: Genres.type,
    file: String,
    albumId: Option[Int])

  case class ActivityList(
    genres: List[String],
    musicians: List[MusicianInterface],
    songs: List[Song]
  )
}