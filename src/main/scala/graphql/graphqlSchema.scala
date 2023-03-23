package graphql

import context._
import models._
import sangria.schema._
import sangria.macros.derive._


object graphqlSchema extends App {
  val users = List(
    User(1, Roles.user, "user", "user@gmail.com", "password"),
    User(2, Roles.admin, "admin", "admin@gmail.com", "password"))

  val UserType: ObjectType[MyContext, User] =
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

  val MusicBandDescriptionType: InputObjectType[MusicBandDescription] =
    deriveInputObjectType[MusicBandDescription](
      InputObjectTypeName("MusicBandDescription"))

  val SongType: ObjectType[MyContext, Song] =
    deriveObjectType[MyContext, Song](
      ObjectTypeName("Song"),
      ReplaceField[MyContext, Song](
        fieldName = "cover",
        field = Field[MyContext, String]("cover", OptionType(StringType))),
      ReplaceField[MyContext, Song](
        fieldName = "albumId",
        field = Field[MyContext, String]("albumId", OptionType(StringType))),
      ReplaceField[MyContext, Song](
        fieldName = "genre",
        field = Field[MyContext, String]("genre", OptionType(StringType))))

  val SongDescriptionType: InputObjectType[SongDescriptionInput] =
    deriveInputObjectType[SongDescriptionInput](
      InputObjectTypeName("SongDescription"),
      ReplaceInputField(
        fieldName = "cover",
        field = InputField[MyContext, SongDescriptionInput]("cover", OptionType(StringType))),
      ReplaceInputField(
        fieldName = "genre",
        field = InputField[MyContext, SongDescriptionInput]("genre", OptionType(StringType))))

  val AlbumType: ObjectType[MyContext, Album] =
    deriveObjectType[MyContext, Album](ObjectTypeName("Album"))

  val AlbumDescriptionType: InputObjectType[AlbumDescription] =
    deriveInputObjectType[AlbumDescription](
      InputObjectTypeName("AlbumDescription"))

  val Musician: InterfaceType[Unit, MusicianInterface] =
    InterfaceType[Unit, MusicianInterface](
      "Musician",
      "A musician entity for the api",
      fields[Unit, MusicianInterface](
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

  val PersonType: ObjectType[MyContext, Person] =
    deriveObjectType[MyContext, Person](
      ObjectTypeName("Person"),
      AddFields(
        Field("firstName", StringType, resolve = _.value.firstName),
        Field("secondName", StringType, resolve = _.value.secondName)
      ),
      Interfaces(Musician)
    )

  val MusicBand: ObjectType[MyContext, MusicBand] =
    deriveObjectType[MyContext, MusicBand](
      ObjectTypeName("MusicBand"),
      AddFields(
        Field("name", StringType, resolve = _.value.name),
        Field("members", ListType(PersonType), resolve = _.value.members)
      ),
      Interfaces(Musician)
    )

  val PersonDescriptionType: InputObjectType[PersonDescription] =
    deriveInputObjectType[PersonDescription](
      InputObjectTypeName("PersonDescription"),
    )

  val ActivityListType = ObjectType[MyContext, ActivityList](
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

  val Genre = Argument[Genres.type]("genre", OptionType(StringType))

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