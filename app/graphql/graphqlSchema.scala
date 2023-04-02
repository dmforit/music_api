package graphql

import models._
import mySchema.DAO
import sangria.schema._
import sangria.macros.derive._
import sangria.marshalling.{CoercedScalaResultMarshaller, FromInput}
import sangria.util.tag.@@
import sangria.validation.Violation

object graphqlSchema {
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

  case object ByteArrayCoerceViolation extends Violation {
    override def errorMessage: String = "Error during parsing ByteArray"
  }

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

  implicit val AuthProviderInputType: InputObjectType[AuthProvider] =
    InputObjectType[AuthProvider](
      name = "AUTH_PROVIDER",
      List(
        InputField("email", StringType),
        InputField("password", StringType)
      )
  )

  implicit val manual: FromInput[AuthProvider] = new FromInput[AuthProvider] {
    val marshaller: CoercedScalaResultMarshaller = CoercedScalaResultMarshaller.default

    def fromResult(node: marshaller.Node): AuthProvider = {
      val ad = node.asInstanceOf[Map[String, Any]]

      AuthProvider(
        email = ad("email").asInstanceOf[String],
        password = ad("password").asInstanceOf[String],
      )
    }
  }

//  implicit val AuthProviderInputType: InputObjectType[AuthProvider] = deriveInputObjectType[AuthProvider](
//    InputObjectTypeName("AUTH_PROVIDER")
//  )
//  implicit lazy val AuthProviderDataInputType: InputObjectType[AuthProviderData] = deriveInputObjectType[AuthProviderData]()


  val Id: Argument[Long] = Argument("id", LongType)
  val Ids: Argument[Seq[Long @@ FromInput.CoercedScalaResult]] = Argument("ids", ListInputType(LongType))
  val NameArg: Argument[String] = Argument("name", StringType)
  val RoleArg: Argument[String] = Argument("role", StringType)
  val AuthProviderDataArg: Argument[AuthProvider] = Argument("authProvider", AuthProviderInputType)

  val QueryType: ObjectType[DAO, Unit] = ObjectType[DAO, Unit](
    name = "Query",
    fields = fields[DAO, Unit](
      Field("allSongs",
        ListType(SongType),
        resolve = c => c.ctx.getSongs),
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

      Field("allUsers",
        ListType(UserType),
        resolve = c => c.ctx.getUsers),
      Field("user",
        OptionType(UserType),
        arguments = Id :: Nil,
        resolve = c => c.ctx.getUser(c arg Id)
      ),
      Field("users",
        ListType(UserType),
        arguments = Ids :: Nil,
        resolve = c => c.ctx.getUsers(c arg Ids)
      ),

      Field("allSingers",
        ListType(EntityType),
        resolve = c => c.ctx.getSingers),
      Field("singer",
        OptionType(EntityType),
        arguments = Id :: Nil,
        resolve = c => c.ctx.getSinger(c arg Id)
      ),
      Field("singers",
        ListType(EntityType),
        arguments = Ids :: Nil,
        resolve = c => c.ctx.getSingers(c arg Ids)
      ),

      Field("allMusicBands",
        ListType(EntityType),
        resolve = c => c.ctx.getMusicBands),
      Field("musicBand",
        OptionType(EntityType),
        arguments = Id :: Nil,
        resolve = c => c.ctx.getMusicBand(c arg Id)
      ),
      Field("musicBands",
        ListType(EntityType),
        arguments = Ids :: Nil,
        resolve = c => c.ctx.getMusicBands(c arg Ids)
      ),

      Field("allAlbums",
        ListType(EntityType),
        resolve = c => c.ctx.getAlbums),
      Field("album",
        OptionType(EntityType),
        arguments = Id :: Nil,
        resolve = c => c.ctx.getAlbum(c arg Id)
      ),
      Field("albums",
        ListType(EntityType),
        arguments = Ids :: Nil,
        resolve = c => c.ctx.getAlbums(c arg Ids)
      ),
    )
  )

  val MutationType: ObjectType[DAO, Unit] = ObjectType[DAO, Unit](
    "Mutation",
    fields[DAO, Unit](
      Field("addUser",
        UserType,
        arguments = RoleArg :: NameArg :: AuthProviderDataArg :: Nil,
        resolve = c => c.ctx.addUser(c.args.arg(RoleArg), c.args.arg(NameArg), c.args.arg(AuthProviderDataArg))
      )
    )
  )

  val SchemaDefinition: Schema[DAO, Unit] = Schema(QueryType, Some(MutationType))
}