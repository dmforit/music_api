package graphql

import models._
import mySchema.DAO
import sangria.schema._
import sangria.macros.derive._
import sangria.marshalling.FromInput
import sangria.util.tag.@@
import sangria.validation.Violation

object graphqlSchema {
//  lazy val RolesEnum = EnumType(
//    "Roles",
//    Some("Enum type for users' roles"),
//    List(
//      EnumValue("ADMIN",
//        value = Roles.admin),
//      EnumValue("USER",
//        value = Roles.user))
//  )
//
//  implicit lazy val MusicBandDescriptionType: InputObjectType[MusicBandDescription] =
//    deriveInputObjectType[MusicBandDescription](InputObjectTypeName("MusicBandDescription"))

//  implicit lazy val SongDescriptionType: InputObjectType[SongDescriptionInput] =
//    deriveInputObjectType[SongDescriptionInput](InputObjectTypeName("SongDescription"))
//
//  implicit lazy val AlbumType: ObjectType[MyContext, Album] = deriveObjectType[MyContext, Album](ObjectTypeName("Album"))
//
//  implicit lazy val AlbumDescriptionType: InputObjectType[AlbumDescription] =
//    deriveInputObjectType[AlbumDescription](InputObjectTypeName("AlbumDescription"))
//
//  implicit lazy val Musician: InterfaceType[MyContext, MusicianInterface] =
//    InterfaceType[MyContext, MusicianInterface](
//      "Musician",
//      "A musician entity for the api",
//      fields[MyContext, MusicianInterface](
//        Field("id", IntType,
//          resolve = _.value.id),
//        Field("auditionsNumber", IntType,
//          resolve = _.value.auditionsNumber),
//        Field("songs", ListType(SongType),
//          resolve = _.value.songs),
//        Field("albums", OptionType(ListType(AlbumType)),
//          resolve = _.value.albums),
//        Field("photoCover", StringType, resolve = _.value.photoCover),
//      )
//    )
//
//  implicit lazy val PersonType: ObjectType[MyContext, Person] =
//    deriveObjectType[MyContext, Person](
//      ObjectTypeName("Person"),
//      AddFields(
//        Field("firstName", StringType, resolve = _.value.firstName),
//        Field("secondName", StringType, resolve = _.value.secondName)
//      ),
//      Interfaces(Musician)
//    )
//
//  implicit lazy val MusicBand: ObjectType[MyContext, MusicBand] =
//    deriveObjectType[MyContext, MusicBand](
//      ObjectTypeName("MusicBand"),
//      AddFields(
//        Field("name", StringType, resolve = _.value.name),
//        Field("members", ListType(PersonType), resolve = _.value.members)
//      ),
//      Interfaces(Musician)
//    )
//
//  implicit lazy val PersonDescriptionType: InputObjectType[PersonDescription] =
//    deriveInputObjectType[PersonDescription](
//      InputObjectTypeName("PersonDescription"))
//
//  implicit lazy val ActivityListType: ObjectType[MyContext, ActivityList] = ObjectType[MyContext, ActivityList](
//    "ActivityList",
//    fields[MyContext, ActivityList](
//      Field("genre", OptionType(ListType(StringType)),
//        resolve = _.value.genres),
//      Field("musicians", OptionType(ListType(Musician)),
//        resolve = _.value.musicians),
//      Field("songs", OptionType(ListType(SongType)),
//        resolve = _.value.songs)
//    )
//  )

//  val songsFetcher = Fetcher[MyContext, Song, Int](
//    (ctx: MyContext, ids) => ctx.dao.getSongsByID(ids)
//  )(Identifiable.hasId)

//  val Resolver = DeferredResolver.fetchers(songsFetcher)

  case object ByteArrayCoerceViolation extends Violation {
    override def errorMessage: String = "Error during parsing ByteArray"
  }

//
//  implicit val ByteArray: ScalarType[Array[Byte]] = ScalarType[Array[Byte]]( //1
//    "ByteArray",
//    coerceOutput = (dt, _) => dt.map(_.toChar).mkString,
//    coerceInput = {
//      case StringValue(s, _, _, _, _) => Right(s.getBytes())
//      case _ => Left(ByteArrayCoerceViolation)
//    },
//    coerceUserInput = {
//      case s: String => Right(s.getBytes())
//      case _ => Left(ByteArrayCoerceViolation)
//    }
//  )

  implicit val ByteArray: ScalarAlias[Array[Byte], String] = ScalarAlias[Array[Byte], String](
    StringType, _.map(_.toChar).mkString, bytea => Right(bytea.getBytes()))


  implicit lazy val SongType: ObjectType[DAO, Song] = deriveObjectType[DAO, Song](
    ObjectTypeName("Song"),
    ReplaceField("cover", Field("cover", OptionType(ByteArray), resolve = _.value.cover)),
    ReplaceField("file", Field("file", ByteArray, resolve = _.value.file))
  )

  implicit lazy val UserType: ObjectType[DAO, User] =
    deriveObjectType[DAO, User](ObjectTypeName("User"))

  implicit lazy val EntityType: ObjectType[DAO, Entity] = deriveObjectType[DAO, Entity](
    ObjectTypeName("Entity"),
    ReplaceField("cover", Field("cover", OptionType(ByteArray), resolve = _.value.cover))
  )


  val Id: Argument[Long] = Argument("id", LongType)
  val Ids: Argument[Seq[Long @@ FromInput.CoercedScalaResult]] = Argument("ids", ListInputType(LongType))

  val QueryType: ObjectType[DAO, Unit] = ObjectType[DAO, Unit](
    name = "Query",
    fields = fields[DAO, Unit](
      Field("allSongs", ListType(SongType), resolve = c => c.ctx.getSongs),
      Field("song",
        OptionType(SongType),
        arguments = Id :: Nil,
        resolve = c => c.ctx.getSong(c arg Id)
      ),
      Field("songs",
        ListType(SongType),
        arguments = Ids :: Nil,
        resolve = c => c.ctx.getSongs(c arg Ids)
      ),

      Field("users", ListType(UserType), resolve = c => c.ctx.getUsers),
      Field("singers", ListType(EntityType), resolve = c => c.ctx.getSingers),
      Field("musicBands", ListType(EntityType), resolve = c => c.ctx.getMusicBands),
      Field("albums", ListType(EntityType), resolve = c => c.ctx.getAlbums),
    )
  )

  val SchemaDefinition: Schema[DAO, Unit] = Schema(QueryType)
}