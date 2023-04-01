package graphql

//import context._
import models._
import mySchema.DAO
import sangria.execution.deferred.{DeferredResolver, Fetcher}
import sangria.schema._
import sangria.macros.derive._

import scala.collection.immutable.Seq

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

implicit lazy val SongType: ObjectType[DAO, Song] =
  deriveObjectType[DAO, Song](ObjectTypeName("Song"))

implicit lazy val UserType: ObjectType[DAO, User] =
  deriveObjectType[DAO, User](ObjectTypeName("User"))

implicit lazy val EntityType: ObjectType[DAO, Entity] =
  deriveObjectType[DAO, Entity](ObjectTypeName("Entity"))


  val Id: Argument[Int] = Argument("id", IntType)

  val QueryType: ObjectType[DAO, Unit] = ObjectType[DAO, Unit](
    name = "Query",
    fields = fields[DAO, Unit](
      Field("songs", ListType(SongType), resolve = c => c.ctx.getSongs),
      Field("users", ListType(UserType), resolve = c => c.ctx.getUsers),
      Field("singers", ListType(EntityType), resolve = c => c.ctx.getSingers),
      Field("musicBands", ListType(EntityType), resolve = c => c.ctx.getMusicBands),
      Field("albums", ListType(EntityType), resolve = c => c.ctx.getAlbums),
    )
  )

  val SchemaDefinition: Schema[DAO, Unit] = Schema(QueryType)
}