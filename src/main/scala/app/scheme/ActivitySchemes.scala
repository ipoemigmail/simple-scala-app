package app.scheme

import app.model.{Activity, Contents, ContentsType, Permission}
import sangria.macros.derive._
import sangria.schema.{EnumType, EnumValue, ObjectType}

object ActivitySchemes {

  implicit val ActivityContentsScheme: ObjectType[Unit, Contents] =
    deriveObjectType[Unit, Contents](
      ObjectTypeDescription("Contents"),
      DocumentField("type", "Contents Type"),
      DocumentField("text", "Contents Text")
    )

  implicit val ActivityScheme: ObjectType[Unit, Activity] =
    deriveObjectType[Unit, Activity](
      ObjectTypeDescription("Activity"),
      DocumentField("id", "Id"),
      DocumentField("contents", "Contents"),
      DocumentField("permission", "Permission"),
    )

  implicit val ContentsTypeScheme: EnumType[ContentsType] =
    EnumType(
      "ContentsType",
      Some("Contents Type"),
      ContentsType.values.map(x => EnumValue(x.entryName, value = x)).toList
    )

  implicit val PermissionScheme: EnumType[Permission] =
    EnumType("Permission", Some("Permission"), Permission.values.map(x => EnumValue(x.entryName, value = x)).toList)
}
