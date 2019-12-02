package app.scheme

import app.model.{Activity, Contents, ContentsKind, Permission}
import sangria.macros.derive._
import sangria.schema.{EnumType, EnumValue, ObjectType}

object ActivityType {

  implicit val ActivityContentsType: ObjectType[Unit, Contents] =
    deriveObjectType[Unit, Contents](
      ObjectTypeDescription("Contents"),
      DocumentField("kind", "Contents Kind"),
      DocumentField("text", "Contents Text")
    )

  implicit val ActivityType: ObjectType[Unit, Activity] =
    deriveObjectType[Unit, Activity](
      ObjectTypeDescription("Activity"),
      DocumentField("id", "Id"),
      DocumentField("contents", "Contents"),
      DocumentField("permission", "Permission"),
    )

  implicit val ContentsKindType: EnumType[ContentsKind] =
    EnumType(
      "ContentsKind",
      Some("Contents Kind"),
      ContentsKind.values.map(x => EnumValue(x.entryName, value = x)).toList
    )

  implicit val PermissionType: EnumType[Permission] =
    EnumType("Permission", Some("Permission"), Permission.values.map(x => EnumValue(x.entryName, value = x)).toList)
}
