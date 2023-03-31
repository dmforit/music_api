package graphql

import context._
import models._
import sangria.schema._
import sangria.macros.derive._


object graphqlSchema extends App {
  val users = List(
    User(1, Roles.user, "user", "user@gmail.com", "password"),
    User(2, Roles.admin, "admin", "admin@gmail.com", "password"))

  implicit val UserType: ObjectType[MyContext, User] =
    deriveObjectType[MyContext, User](ObjectTypeName("User"))

  val RolesEnum = EnumType(
    "Roles",
    Some("Enum type for users' roles"),
    List(
      EnumValue("ADMIN",
        value = Roles.admin),
      EnumValue("USER",
        value = Roles.user))
  )

  implicit val MusicBandDescriptionType: InputObjectType[MusicBandDescription] =
    deriveInputObjectType[MusicBandDescription](InputObjectTypeName("MusicBandDescription"))

  implicit val SongType: ObjectType[MyContext, Song] =
    deriveObjectType[MyContext, Song](
      ObjectTypeName("Song"),
      ReplaceField(fieldName = "cover", field =
        Field("cover", OptionType(StringType),
          Some("the cover of the song"),
          resolve = _.value.cover)),
      ReplaceField(fieldName = "album_id", field =
        Field("album_id", OptionType(IntType),
          Some("the cover of the song"),
          resolve = _.value.album_id)),
      ReplaceField(fieldName = "genre", field =
        Field("genre", OptionType(StringType),
          Some("the genre of the song"),
          resolve = _.value.genre)))

  implicit val SongDescriptionType: InputObjectType[SongDescriptionInput] =
    deriveInputObjectType[SongDescriptionInput](InputObjectTypeName("SongDescription"))

  implicit val AlbumType: ObjectType[MyContext, Album] = deriveObjectType[MyContext, Album](ObjectTypeName("Album"))

  implicit val AlbumDescriptionType: InputObjectType[AlbumDescription] =
    deriveInputObjectType[AlbumDescription](InputObjectTypeName("AlbumDescription"))

  implicit val Musician: InterfaceType[MyContext, MusicianInterface] =
    InterfaceType[MyContext, MusicianInterface](
      "Musician",
      "A musician entity for the api",
      fields[MyContext, MusicianInterface](
        Field("id", IntType,
          resolve = _.value.id),
        Field("auditionsNumber", IntType,
          resolve = _.value.auditionsNumber),
        Field("songs", ListType(SongType),
          resolve = _.value.songs),
        Field("albums", OptionType(ListType(AlbumType)),
          resolve = _.value.albums),
        Field("photoCover", StringType, resolve = _.value.photoCover),
      )
    )

  implicit val PersonType: ObjectType[MyContext, Person] =
    deriveObjectType[MyContext, Person](
      ObjectTypeName("Person"),
      AddFields(
        Field("firstName", StringType, resolve = _.value.firstName),
        Field("secondName", StringType, resolve = _.value.secondName)
      ),
      Interfaces(Musician)
    )

  implicit val MusicBand: ObjectType[MyContext, MusicBand] =
    deriveObjectType[MyContext, MusicBand](
      ObjectTypeName("MusicBand"),
      AddFields(
        Field("name", StringType, resolve = _.value.name),
        Field("members", ListType(PersonType), resolve = _.value.members)
      ),
      Interfaces(Musician)
    )

  implicit val PersonDescriptionType: InputObjectType[PersonDescription] =
    deriveInputObjectType[PersonDescription](
      InputObjectTypeName("PersonDescription"))

  implicit val ActivityListType: ObjectType[MyContext, ActivityList] = ObjectType[MyContext, ActivityList](
    "ActivityList",
    fields[MyContext, ActivityList](
      Field("genre", OptionType(ListType(StringType)),
        resolve = _.value.genres),
      Field("musicians", OptionType(ListType(Musician)),
        resolve = _.value.musicians),
      Field("songs", OptionType(ListType(SongType)),
        resolve = _.value.songs)
    )
  )

  val Genre = Argument("genre", StringType, description = "genre of the argument")

  val QueryType = ObjectType(
    name = "Query",
    fields = fields[MyContext, Unit](
      Field("songs", OptionType(ListType(SongType)),
        arguments = Genre :: Nil,
        resolve = c => c.ctx.dao.getSongs),
    )
  )

  val SchemaDefinition = Schema(QueryType)
}